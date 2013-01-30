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
//package org.sopeco.runner;
//
//
//import static org.junit.Assert.assertNotNull;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.sopeco.config.Configuration;
//import org.sopeco.config.IConfiguration;
//import org.sopeco.engine.experimentseries.IConstantAssignmentExtension;
//import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
//import org.sopeco.engine.processing.IProcessingStrategyExtension;
//import org.sopeco.engine.registry.ExtensionRegistry;
//import org.sopeco.engine.registry.IExtensionRegistry;
//import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
//import org.sopeco.plugin.std.exploration.full.FullExplorationStrategyExtension;
//
//public class ExtensionsTest {
//
//	IExtensionRegistry registry = null;
//	
//	@Before
//	public void setUp() throws Exception {
//		IConfiguration config = Configuration.getSessionUnrelatedSingleton(this.getClass());
//		config.setProperty(IConfiguration.CONF_PLUGINS_DIRECTORIES, "secondPluginDir");
//		
//		registry = ExtensionRegistry.getSingleton();
//	}
//
//	@Test
//	public void testStandardPlugin() {
//		ISoPeCoExtensionArtifact fullExploration = registry.getExtensionArtifact(IExplorationStrategyExtension.class, FullExplorationStrategyExtension.NAME);
//		
//		assertNotNull(fullExploration);
//		System.out.println(fullExploration.getProvider().getName() + " is loaded successfully.");
//	}
//
//	@Test
//	public void testCustomPlugin() {
//		ISoPeCoExtensionArtifact dummyStrategy = registry.getExtensionArtifact(IProcessingStrategyExtension.class, "Dummy Processing Extension");
//		
//		assertNotNull(dummyStrategy);
//		System.out.println(dummyStrategy.getProvider().getName() + " is loaded successfully.");
//	}
//
//
//	@Test
//	public void testCustomPluginDirectory() {
//		ISoPeCoExtensionArtifact dummyStrategy = registry.getExtensionArtifact(IConstantAssignmentExtension.class, 	"Dummy Constant Value Assignment");		
//		
//		assertNotNull(dummyStrategy);
//		System.out.println(dummyStrategy.getProvider().getName() + " is loaded successfully.");
//	}
//
//}
