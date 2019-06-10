package org.dimensinfin.eveonline.neocom.planetary;

import org.junit.Assert;
import org.junit.Test;

public class CommandCenterTypeTest {
	@Test
	public void build() {
		final CommandCenterType type = new CommandCenterType.Builder()
				                               .withLevel(0)
				                               .withPrefix("")
				                               .withCpuCapacity(1675)
				                               .withPowerOutput(6000)
				                               .build();
		Assert.assertNotNull(type);
	}

	@Test
	public void getTypeByLevel() {
		final CommandCenterType type = CommandCenterType.getTypeByLevel(2);
		Assert.assertEquals("Check level.", 2, type.getLevel());
		Assert.assertEquals("Check the CPU for the level.", 12136, type.getCpuCapacity());
		Assert.assertEquals("Check the Power for the level.", 12000, type.getPowerOutput());
	}
}
