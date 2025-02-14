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
package org.hisp.dhis.trackedentitydatavalue;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import org.hisp.dhis.common.AuditType;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.program.Event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * @author Morten Olav Hansen <mortenoh@gmail.com>
 */
@JacksonXmlRootElement( localName = "trackedEntityDataValueAudit", namespace = DxfNamespaces.DXF_2_0 )
public class TrackedEntityDataValueAudit
    implements Serializable
{
    private long id;

    private DataElement dataElement;

    private Event event;

    private Date created;

    private String value;

    private Boolean providedElsewhere;

    private String modifiedBy;

    private AuditType auditType;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public TrackedEntityDataValueAudit()
    {
    }

    public TrackedEntityDataValueAudit( DataElement dataElement, Event event, String value, String modifiedBy,
        boolean providedElsewhere, AuditType auditType )
    {
        this.dataElement = dataElement;
        this.event = event;
        this.providedElsewhere = providedElsewhere;
        this.created = new Date();
        this.value = value;
        this.modifiedBy = modifiedBy;
        this.auditType = auditType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash( dataElement, event, created, value, providedElsewhere, modifiedBy,
            auditType );
    }

    @Override
    public boolean equals( Object obj )
    {
        if ( this == obj )
        {
            return true;
        }

        if ( obj == null || getClass() != obj.getClass() )
        {
            return false;
        }

        final TrackedEntityDataValueAudit other = (TrackedEntityDataValueAudit) obj;

        return Objects.equals( this.dataElement, other.dataElement )
            && Objects.equals( this.event, other.event )
            && Objects.equals( this.created, other.created )
            && Objects.equals( this.value, other.value )
            && Objects.equals( this.providedElsewhere, other.providedElsewhere )
            && Objects.equals( this.modifiedBy, other.modifiedBy )
            && Objects.equals( this.auditType, other.auditType );
    }

    @Override
    public String toString()
    {
        return "[dataElement: '" + dataElement.getUid() + "', " +
            "event: '" + event.getUid() + "', " +
            "value: '" + value + "']";
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Event getEvent()
    {
        return event;
    }

    public void setEvent( Event event )
    {
        this.event = event;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public DataElement getDataElement()
    {
        return dataElement;
    }

    public void setDataElement( DataElement dataElement )
    {
        this.dataElement = dataElement;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Date getCreated()
    {
        return created;
    }

    public void setCreated( Date created )
    {
        this.created = created;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getValue()
    {
        return value;
    }

    public void setValue( String value )
    {
        this.value = value;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public boolean getProvidedElsewhere()
    {
        return providedElsewhere;
    }

    public void setProvidedElsewhere( boolean providedElsewhere )
    {
        this.providedElsewhere = providedElsewhere;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public String getModifiedBy()
    {
        return modifiedBy;
    }

    public void setModifiedBy( String modifiedBy )
    {
        this.modifiedBy = modifiedBy;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public AuditType getAuditType()
    {
        return auditType;
    }

    public void setAuditType( AuditType auditType )
    {
        this.auditType = auditType;
    }
}
