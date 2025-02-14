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
package org.hisp.dhis.program.message;

import java.util.Date;
import java.util.Set;

import org.hisp.dhis.program.Enrollment;
import org.hisp.dhis.program.Event;

/**
 * @author Zubair <rajazubair.asghar@gmail.com>
 */
public class ProgramMessageQueryParams
{
    private Set<String> organisationUnit;

    private ProgramMessageStatus messageStatus;

    private Enrollment enrollment;

    private Event event;

    private Date afterDate;

    private Date beforeDate;

    private Integer page;

    private Integer pageSize;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramMessageQueryParams()
    {
        super();
    }

    public ProgramMessageQueryParams( Set<String> organisationUnit, ProgramMessageStatus messageStatus,
        Enrollment enrollment, Event event, Date afterDate, Date beforeDate,
        Integer page, Integer pageSize )
    {
        super();
        this.organisationUnit = organisationUnit;
        this.messageStatus = messageStatus;
        this.enrollment = enrollment;
        this.event = event;
        this.afterDate = afterDate;
        this.beforeDate = beforeDate;
        this.page = page;
        this.pageSize = pageSize;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public boolean hasOrignisationUnit()
    {
        return organisationUnit != null;
    }

    public boolean hasEnrollment()
    {
        return enrollment != null;
    }

    public boolean hasEvent()
    {
        return event != null;
    }

    public boolean hasPaging()
    {
        return page != null && pageSize != null;
    }

    // -------------------------------------------------------------------------
    // Getters and Setters
    // -------------------------------------------------------------------------

    public Enrollment getEnrollment()
    {
        return enrollment;
    }

    public void setEnrollment( Enrollment enrollment )
    {
        this.enrollment = enrollment;
    }

    public Event getEvent()
    {
        return event;
    }

    public void setEvent( Event event )
    {
        this.event = event;
    }

    public Set<String> getOrganisationUnit()
    {
        return organisationUnit;
    }

    public void setOrganisationUnit( Set<String> organisationUnit )
    {
        this.organisationUnit = organisationUnit;
    }

    public Integer getPage()
    {
        return page;
    }

    public void setPage( Integer page )
    {
        this.page = page;
    }

    public Integer getPageSize()
    {
        return pageSize;
    }

    public void setPageSize( Integer pageSize )
    {
        this.pageSize = pageSize;
    }

    public ProgramMessageStatus getMessageStatus()
    {
        return messageStatus;
    }

    public void setMessageStatus( ProgramMessageStatus messageStatus )
    {
        this.messageStatus = messageStatus;
    }

    public Date getAfterDate()
    {
        return afterDate;
    }

    public void setAfterDate( Date afterDate )
    {
        this.afterDate = afterDate;
    }

    public Date getBeforeDate()
    {
        return beforeDate;
    }

    public void setBeforeDate( Date beforeDate )
    {
        this.beforeDate = beforeDate;
    }
}
