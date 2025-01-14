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
package org.hisp.dhis.tracker.imports.converter;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.program.UserInfoSnapshot;
import org.hisp.dhis.trackedentity.TrackedEntity;
import org.hisp.dhis.trackedentity.TrackedEntityType;
import org.hisp.dhis.tracker.imports.preheat.TrackerPreheat;
import org.hisp.dhis.util.DateUtils;
import org.springframework.stereotype.Service;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@Service
public class TrackedEntityTrackerConverterService
    implements TrackerConverterService<org.hisp.dhis.tracker.imports.domain.TrackedEntity, TrackedEntity>
{
    @Override
    public org.hisp.dhis.tracker.imports.domain.TrackedEntity to( TrackedEntity trackedEntity )
    {
        List<org.hisp.dhis.tracker.imports.domain.TrackedEntity> trackedEntities = to(
            Collections.singletonList( trackedEntity ) );

        if ( trackedEntities.isEmpty() )
        {
            return null;
        }

        return trackedEntities.get( 0 );
    }

    @Override
    public List<org.hisp.dhis.tracker.imports.domain.TrackedEntity> to( List<TrackedEntity> trackedEntities )
    {
        return trackedEntities.stream().map( tei -> {
            org.hisp.dhis.tracker.imports.domain.TrackedEntity trackedEntity = new org.hisp.dhis.tracker.imports.domain.TrackedEntity();
            trackedEntity.setTrackedEntity( tei.getUid() );

            return trackedEntity;
        } ).collect( Collectors.toList() );
    }

    @Override
    public TrackedEntity from( TrackerPreheat preheat,
        org.hisp.dhis.tracker.imports.domain.TrackedEntity trackedEntity )
    {
        TrackedEntity tei = preheat.getTrackedEntity(
            trackedEntity.getTrackedEntity() );
        return from( preheat, trackedEntity, tei );
    }

    @Override
    public List<TrackedEntity> from( TrackerPreheat preheat,
        List<org.hisp.dhis.tracker.imports.domain.TrackedEntity> trackedEntities )
    {
        return trackedEntities
            .stream()
            .map( te -> from( preheat, te ) )
            .collect( Collectors.toList() );
    }

    private TrackedEntity from( TrackerPreheat preheat, org.hisp.dhis.tracker.imports.domain.TrackedEntity te,
        TrackedEntity tei )
    {
        OrganisationUnit organisationUnit = preheat.getOrganisationUnit( te.getOrgUnit() );
        TrackedEntityType trackedEntityType = preheat.getTrackedEntityType( te.getTrackedEntityType() );

        Date now = new Date();

        if ( isNewEntity( tei ) )
        {
            tei = new TrackedEntity();
            tei.setUid( te.getTrackedEntity() );
            tei.setCreated( now );
            tei.setCreatedByUserInfo( UserInfoSnapshot.from( preheat.getUser() ) );
        }

        tei.setLastUpdatedByUserInfo( UserInfoSnapshot.from( preheat.getUser() ) );
        tei.setStoredBy( te.getStoredBy() );
        tei.setLastUpdated( now );
        tei.setDeleted( false );
        tei.setPotentialDuplicate( te.isPotentialDuplicate() );
        tei.setCreatedAtClient( DateUtils.fromInstant( te.getCreatedAtClient() ) );
        tei.setLastUpdatedAtClient( DateUtils.fromInstant( te.getUpdatedAtClient() ) );
        tei.setOrganisationUnit( organisationUnit );
        tei.setTrackedEntityType( trackedEntityType );
        tei.setInactive( te.isInactive() );
        tei.setGeometry( te.getGeometry() );

        return tei;
    }
}