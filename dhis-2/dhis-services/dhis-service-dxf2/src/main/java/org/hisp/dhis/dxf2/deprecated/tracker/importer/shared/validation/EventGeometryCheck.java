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
package org.hisp.dhis.dxf2.deprecated.tracker.importer.shared.validation;

import static org.hisp.dhis.dxf2.importsummary.ImportSummary.success;

import java.util.Objects;

import org.hisp.dhis.common.IdScheme;
import org.hisp.dhis.dxf2.deprecated.tracker.importer.Checker;
import org.hisp.dhis.dxf2.deprecated.tracker.importer.context.WorkContext;
import org.hisp.dhis.dxf2.deprecated.tracker.importer.shared.ImmutableEvent;
import org.hisp.dhis.dxf2.importsummary.ImportStatus;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.organisationunit.FeatureType;
import org.hisp.dhis.program.ProgramStage;
import org.springframework.stereotype.Component;

/**
 * @author Luciano Fiandesio
 */
@Component
public class EventGeometryCheck implements Checker
{
    @Override
    public ImportSummary check( ImmutableEvent event, WorkContext ctx )
    {
        IdScheme scheme = ctx.getImportOptions().getIdSchemes().getProgramStageIdScheme();
        ProgramStage programStage = ctx.getProgramStage( scheme, event.getProgramStage() );

        if ( Objects.nonNull( event.getGeometry() )
            && programStageFeatureCompatibleWithEventGeometry( event, programStage ) )
        {
            return new ImportSummary( ImportStatus.ERROR,
                "Geometry (" + event.getGeometry().getGeometryType() + ") does not conform to the feature type ("
                    + programStage.getFeatureType().value() + ") specified for the program stage: "
                    + programStage.getUid() )
                .setReference( event.getEvent() ).incrementIgnored();
        }

        return success();
    }

    private boolean programStageFeatureCompatibleWithEventGeometry( ImmutableEvent event, ProgramStage programStage )
    {
        return programStage.getFeatureType().equals( FeatureType.NONE )
            || !programStage.getFeatureType().value().equals( event.getGeometry().getGeometryType() );
    }
}
