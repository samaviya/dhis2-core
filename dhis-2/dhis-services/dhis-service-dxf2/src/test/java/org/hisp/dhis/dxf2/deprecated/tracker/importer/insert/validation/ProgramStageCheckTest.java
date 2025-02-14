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
package org.hisp.dhis.dxf2.deprecated.tracker.importer.insert.validation;

import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.hisp.dhis.common.CodeGenerator;
import org.hisp.dhis.dxf2.deprecated.tracker.importer.shared.ImmutableEvent;
import org.hisp.dhis.dxf2.deprecated.tracker.importer.validation.BaseValidationTest;
import org.hisp.dhis.dxf2.importsummary.ImportSummary;
import org.hisp.dhis.program.Program;
import org.hisp.dhis.program.ProgramType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

/**
 * @author Luciano Fiandesio
 */
@MockitoSettings( strictness = Strictness.LENIENT )
class ProgramStageCheckTest extends BaseValidationTest
{

    private ProgramStageCheck rule;

    @BeforeEach
    void setUp()
    {
        rule = new ProgramStageCheck();
    }

    @Test
    void failOnNullProgramStage()
    {
        Program program = new Program();
        program.setUid( CodeGenerator.generateUid() );
        program.setProgramType( ProgramType.WITH_REGISTRATION );
        Map<String, Program> programMap = new HashMap<>();
        programMap.put( program.getUid(), program );
        event.setProgram( program.getUid() );
        when( workContext.getProgramsMap() ).thenReturn( programMap );
        ImportSummary summary = rule.check( new ImmutableEvent( event ), workContext );
        assertHasError( summary, event,
            "Event.programStage does not point to a valid programStage: " + event.getProgramStage() );
    }
}
