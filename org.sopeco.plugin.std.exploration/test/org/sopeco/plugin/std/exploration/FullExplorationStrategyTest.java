package org.sopeco.plugin.std.exploration;

import org.junit.Before;
import org.junit.Test;
import org.sopeco.engine.experimentseries.IExplorationStrategy;

public class FullExplorationStrategyTest {

	IExplorationStrategy fes = null;
	
	@Before
	public void setUp() throws Exception {
		FullExplorationStrategyExtension fese = new FullExplorationStrategyExtension();
		fes = fese.createExtensionArtifact();
	}

	@Test
	public void testCanRun() {
	}

	@Test
	public void testRunExperimentSeries() {
	}

}
