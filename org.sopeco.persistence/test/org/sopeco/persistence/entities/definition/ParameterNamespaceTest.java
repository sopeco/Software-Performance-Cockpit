/**
 * Copyright (c) 2013 SAP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the SAP nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL SAP BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sopeco.persistence.entities.definition;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.persistence.EntityFactory;

/**
 * Test class for the extension implementation of the parameter namespace.
 * 
 * @author Dennis Westermann
 *
 */
public class ParameterNamespaceTest {
	
	private static ParameterNamespace org;
	private static ParameterNamespace sopeco;
	private static ParameterNamespace emptyRoot;

	@Before
	public void setUp() throws Exception {
		emptyRoot = EntityFactory.createNamespace("");
		org = EntityFactory.createNamespace("org");
		sopeco = EntityFactory.createNamespace("sopeco");
		
		emptyRoot.getChildren().add(org);
		org.setParent(emptyRoot);
		org.getChildren().add(sopeco);
		sopeco.setParent(org);
		
		ParameterDefinition paramDef = EntityFactory.createParameterDefinition("dummy", "Double", ParameterRole.INPUT);
		sopeco.getParameters().add(paramDef);
		paramDef.setNamespace(sopeco);
	}

	@Test
	public void testGetFullName() {
		assertEquals("", emptyRoot.getFullName());
		assertEquals("org", org.getFullName());
		assertEquals("org.sopeco", sopeco.getFullName());
	}
	
	@Test
	public void testGetParameterByName() {
		assertNotNull(sopeco.getParameter("dummy"));
		assertEquals(null, org.getParameter("dummy"));
		assertEquals(null, sopeco.getParameter("foo"));
		
	}
	
}
