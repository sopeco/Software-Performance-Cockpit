package org.sopeco.runner;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.config.Configuration;
import org.sopeco.engine.experimentseries.IExplorationStrategyExtension;
import org.sopeco.engine.processing.IProcessingStrategyExtension;
import org.sopeco.engine.registry.ExtensionRegistry;
import org.sopeco.engine.registry.IExtensionRegistry;
import org.sopeco.engine.registry.ISoPeCoExtensionArtifact;
import org.sopeco.plugin.std.exploration.full.FullExplorationStrategyExtension;

public class ExtensionsTest {

	IExtensionRegistry registry = null;
	
	@Before
	public void setUp() throws Exception {
		Configuration.getSingleton(this.getClass());
		registry = ExtensionRegistry.getSingleton();
	}

	@Test
	public void testStandardPlugin() {
		ISoPeCoExtensionArtifact fullExploration = registry.getExtensionArtifact(IExplorationStrategyExtension.class, FullExplorationStrategyExtension.NAME);
		
		assertNotNull(fullExploration);
		System.out.println(fullExploration.getProvider().getName() + " is loaded successfully.");
	}

	@Test
	public void testCustomPlugin() {
		ISoPeCoExtensionArtifact dummyStrategy = registry.getExtensionArtifact(IProcessingStrategyExtension.class, "Dummy Runner Extension");
		
		assertNotNull(dummyStrategy);
		System.out.println(dummyStrategy.getProvider().getName() + " is loaded successfully.");
	}

}
