/*
 * Copyright (c) 2004-2022, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.dhis.webapi.controller.tracker.imports;

import static org.hisp.dhis.dxf2.webmessage.WebMessageUtils.ok;
import static org.hisp.dhis.webapi.controller.tracker.ControllerSupport.RESOURCE_PATH;
import static org.hisp.dhis.webapi.utils.ContextUtils.setNoStore;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.io.InputStream;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.hisp.dhis.common.CodeGenerator;
import org.hisp.dhis.common.DhisApiVersion;
import org.hisp.dhis.common.OpenApi;
import org.hisp.dhis.commons.util.StreamUtils;
import org.hisp.dhis.dxf2.webmessage.WebMessage;
import org.hisp.dhis.feedback.NotFoundException;
import org.hisp.dhis.scheduling.JobConfiguration;
import org.hisp.dhis.scheduling.JobType;
import org.hisp.dhis.system.notification.Notification;
import org.hisp.dhis.system.notification.Notifier;
import org.hisp.dhis.tracker.imports.TrackerBundleReportMode;
import org.hisp.dhis.tracker.imports.TrackerImportParams;
import org.hisp.dhis.tracker.imports.TrackerImportService;
import org.hisp.dhis.tracker.imports.job.TrackerJobWebMessageResponse;
import org.hisp.dhis.tracker.imports.report.ImportReport;
import org.hisp.dhis.tracker.imports.report.Status;
import org.hisp.dhis.user.CurrentUser;
import org.hisp.dhis.user.User;
import org.hisp.dhis.webapi.controller.tracker.export.CsvService;
import org.hisp.dhis.webapi.controller.tracker.view.Event;
import org.hisp.dhis.webapi.mvc.annotation.ApiVersion;
import org.hisp.dhis.webapi.utils.ContextUtils;
import org.locationtech.jts.io.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@OpenApi.Tags( "tracker" )
@RestController
@RequestMapping( value = RESOURCE_PATH )
@ApiVersion( { DhisApiVersion.DEFAULT, DhisApiVersion.ALL } )
@RequiredArgsConstructor
public class TrackerImportController
{
    static final String TRACKER_JOB_ADDED = "Tracker job added";

    private final TrackerSyncImporter syncImporter;

    private final TrackerAsyncImporter asyncImporter;

    private final TrackerImportService trackerImportService;

    private final CsvService<Event> csvEventService;

    private final Notifier notifier;

    @PostMapping( value = "", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE )
    @ResponseBody
    public WebMessage asyncPostJsonTracker( HttpServletRequest request,
        RequestParams requestParams,
        @CurrentUser User currentUser,
        @RequestBody Body body )
    {
        String jobId = CodeGenerator.generateUid();
        TrackerImportParams trackerImportParams = TrackerImportParamsMapper
            .trackerImportParams( true, jobId, currentUser.getUid(), requestParams, body );

        asyncImporter.importTracker( trackerImportParams,
            SecurityContextHolder.getContext().getAuthentication(), jobId );

        String location = ContextUtils.getRootPath( request ) + "/tracker/jobs/" + jobId;

        return ok( TRACKER_JOB_ADDED )
            .setLocation( "/tracker/jobs/" + jobId )
            .setResponse( TrackerJobWebMessageResponse.builder().id( jobId ).location( location ).build() );
    }

    @PostMapping( value = "", consumes = APPLICATION_JSON_VALUE, params = { "async=false" } )
    public ResponseEntity<ImportReport> syncPostJsonTracker( RequestParams requestParams,
        @CurrentUser User currentUser, @RequestBody Body body )
    {
        String jobId = CodeGenerator.generateUid();
        TrackerImportParams trackerImportParams = TrackerImportParamsMapper
            .trackerImportParams( false, jobId, currentUser.getUid(), requestParams, body );

        ImportReport importReport = syncImporter.importTracker( trackerImportParams );

        ResponseEntity.BodyBuilder builder = importReport.getStatus() == Status.ERROR
            ? ResponseEntity.status( HttpStatus.CONFLICT )
            : ResponseEntity.ok();

        return builder.body( importReport );
    }

    @PostMapping( value = "", consumes = { "application/csv", "text/csv" }, produces = APPLICATION_JSON_VALUE )
    @ResponseBody
    public WebMessage asyncPostCsvTracker( HttpServletRequest request, RequestParams importRequest,
        @CurrentUser User currentUser,
        @RequestParam( required = false, defaultValue = "true" ) boolean skipFirst )
        throws IOException,
        ParseException
    {
        InputStream inputStream = StreamUtils.wrapAndCheckCompressionFormat( request.getInputStream() );

        List<Event> events = csvEventService.read( inputStream, skipFirst );

        Body body = Body.builder()
            .events( events )
            .build();

        String jobId = CodeGenerator.generateUid();
        TrackerImportParams trackerImportParams = TrackerImportParamsMapper
            .trackerImportParams( true, jobId, currentUser.getUid(), importRequest, body );

        asyncImporter.importTracker( trackerImportParams,
            SecurityContextHolder.getContext().getAuthentication(), jobId );

        String location = ContextUtils.getRootPath( request ) + "/tracker/jobs/" + jobId;

        return ok( TRACKER_JOB_ADDED )
            .setLocation( "/tracker/jobs/" + jobId )
            .setResponse( TrackerJobWebMessageResponse.builder().id( jobId ).location( location ).build() );
    }

    @PostMapping( value = "", consumes = { "application/csv",
        "text/csv" }, produces = APPLICATION_JSON_VALUE, params = { "async=false" } )
    public ResponseEntity<ImportReport> syncPostCsvTracker(
        HttpServletRequest request,
        RequestParams importRequest,
        @RequestParam( required = false, defaultValue = "true" ) boolean skipFirst,
        @RequestParam( defaultValue = "errors", required = false ) TrackerBundleReportMode reportMode,
        @CurrentUser User currentUser )
        throws IOException,
        ParseException
    {
        InputStream inputStream = StreamUtils.wrapAndCheckCompressionFormat( request.getInputStream() );

        List<Event> events = csvEventService.read( inputStream, skipFirst );
        Body body = Body.builder()
            .events( events )
            .build();

        String jobId = CodeGenerator.generateUid();
        TrackerImportParams trackerImportParams = TrackerImportParamsMapper
            .trackerImportParams( false, jobId, currentUser.getUid(), importRequest, body );

        ImportReport importReport = syncImporter.importTracker( trackerImportParams );

        ResponseEntity.BodyBuilder builder = importReport.getStatus() == Status.ERROR
            ? ResponseEntity.status( HttpStatus.CONFLICT )
            : ResponseEntity.ok();

        return builder.body( importReport );
    }

    @GetMapping( value = "/jobs/{uid}", produces = APPLICATION_JSON_VALUE )
    public Deque<Notification> getJob( @PathVariable String uid, HttpServletResponse response )
        throws HttpStatusCodeException
    {
        setNoStore( response );
        return notifier.getNotificationsByJobId( JobType.TRACKER_IMPORT_JOB, uid );
    }

    @GetMapping( value = "/jobs/{uid}/report", produces = APPLICATION_JSON_VALUE )
    public ImportReport getJobReport( @PathVariable String uid,
        @RequestParam( defaultValue = "errors", required = false ) TrackerBundleReportMode reportMode,
        HttpServletResponse response )
        throws HttpStatusCodeException,
        NotFoundException
    {
        setNoStore( response );

        return Optional.ofNullable( notifier
            .getJobSummaryByJobId( JobType.TRACKER_IMPORT_JOB, uid ) )
            .map( report -> trackerImportService.buildImportReport( (ImportReport) report, reportMode ) )
            .orElseThrow( () -> new NotFoundException( JobConfiguration.class, uid ) );
    }
}
