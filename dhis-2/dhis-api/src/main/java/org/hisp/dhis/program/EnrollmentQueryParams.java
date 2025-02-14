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
package org.hisp.dhis.program;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Data;
import lombok.experimental.Accessors;

import org.apache.commons.collections4.CollectionUtils;
import org.hisp.dhis.common.OrganisationUnitSelectionMode;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.trackedentity.TrackedEntity;
import org.hisp.dhis.trackedentity.TrackedEntityType;
import org.hisp.dhis.user.User;
import org.hisp.dhis.webapi.controller.event.mapper.OrderParam;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@Data
@Accessors( chain = true )
public class EnrollmentQueryParams
{
    public static final int DEFAULT_PAGE = 1;

    public static final int DEFAULT_PAGE_SIZE = 50;

    /**
     * Last updated for enrollment.
     */
    private Date lastUpdated;

    /**
     * The last updated duration filter.
     */
    private String lastUpdatedDuration;

    /**
     * Organisation units for which instances in the response were registered
     * at. Is related to the specified OrganisationUnitMode.
     */
    private Set<OrganisationUnit> organisationUnits = new HashSet<>();

    /**
     * Selection mode for the specified organisation units.
     */
    private OrganisationUnitSelectionMode organisationUnitMode;

    /**
     * Program for which instances in the response must be enrolled in.
     */
    private Program program;

    /**
     * Status of the tracked entity instance in the given program.
     */
    private ProgramStatus programStatus;

    /**
     * Indicates whether tracked entity instance is marked for follow up for the
     * specified program.
     */
    private Boolean followUp;

    /**
     * Start date for enrollment in the given program.
     */
    private Date programStartDate;

    /**
     * End date for enrollment in the given program.
     */
    private Date programEndDate;

    /**
     * Tracked entity of the instances in the response.
     */
    private TrackedEntityType trackedEntityType;

    /**
     * Tracked entity instance.
     */
    private TrackedEntity trackedEntity;

    /**
     * Page number.
     */
    private Integer page;

    /**
     * Page size.
     */
    private Integer pageSize;

    /**
     * Indicates whether to include the total number of pages in the paging
     * response.
     */
    private boolean totalPages;

    /**
     * Indicates whether paging should be skipped.
     */
    private boolean skipPaging;

    /**
     * Indicates whether to include soft-deleted enrollments
     */
    private boolean includeDeleted;

    /**
     * List of order params
     */
    private List<OrderParam> order;

    // -------------------------------------------------------------------------
    // Transient properties
    // -------------------------------------------------------------------------

    /**
     * Current user for query.
     */
    private transient User user;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public EnrollmentQueryParams()
    {
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    /**
     * Adds an organisation unit to the parameters.
     */
    public void addOrganisationUnit( OrganisationUnit unit )
    {
        this.organisationUnits.add( unit );
    }

    public void addOrganisationUnits( Set<OrganisationUnit> orgUnits )
    {
        this.organisationUnits.addAll( orgUnits );
    }

    /**
     * Indicates whether this params specifies last updated.
     */
    public boolean hasLastUpdated()
    {
        return lastUpdated != null;
    }

    /**
     * Indicates whether this parameters has a lastUpdatedDuration filter.
     */
    public boolean hasLastUpdatedDuration()
    {
        return lastUpdatedDuration != null;
    }

    /**
     * Indicates whether this params specifies any organisation units.
     */
    public boolean hasOrganisationUnits()
    {
        return organisationUnits != null && !organisationUnits.isEmpty();
    }

    /**
     * Indicates whether this params specifies a program.
     */
    public boolean hasProgram()
    {
        return program != null;
    }

    /**
     * Indicates whether this params specifies a program status.
     */
    public boolean hasProgramStatus()
    {
        return programStatus != null;
    }

    /**
     * Indicates whether this params specifies follow up for the given program.
     * Follow up can be specified as true or false.
     */
    public boolean hasFollowUp()
    {
        return followUp != null;
    }

    /**
     * Indicates whether this params specifies a program start date.
     */
    public boolean hasProgramStartDate()
    {
        return programStartDate != null;
    }

    /**
     * Indicates whether this params specifies a program end date.
     */
    public boolean hasProgramEndDate()
    {
        return programEndDate != null;
    }

    /**
     * Indicates whether this params specifies a tracked entity.
     */
    public boolean hasTrackedEntityType()
    {
        return trackedEntityType != null;
    }

    /**
     * Indicates whether this params specifies a tracked entity instance.
     */
    public boolean hasTrackedEntity()
    {
        return this.trackedEntity != null;
    }

    /**
     * Indicates whether this params is of the given organisation unit mode.
     */
    public boolean isOrganisationUnitMode( OrganisationUnitSelectionMode mode )
    {
        return organisationUnitMode != null && organisationUnitMode.equals( mode );
    }

    /**
     * Indicates whether paging is enabled.
     */
    public boolean isPaging()
    {
        return page != null || pageSize != null;
    }

    /**
     * Returns the page number, falls back to default value of 1 if not
     * specified.
     */
    public int getPageWithDefault()
    {
        return page != null && page > 0 ? page : DEFAULT_PAGE;
    }

    /**
     * Returns the page size, falls back to default value of 50 if not
     * specified.
     */
    public int getPageSizeWithDefault()
    {
        return pageSize != null && pageSize >= 0 ? pageSize : DEFAULT_PAGE_SIZE;
    }

    /**
     * Returns the offset based on the page number and page size.
     */
    public int getOffset()
    {
        return (getPageWithDefault() - 1) * getPageSizeWithDefault();
    }

    /**
     * Sets paging properties to default values.
     */
    public void setDefaultPaging()
    {
        this.page = DEFAULT_PAGE;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.skipPaging = false;
    }

    public boolean isSorting()
    {
        return !CollectionUtils.emptyIfNull( order ).isEmpty();
    }
}
