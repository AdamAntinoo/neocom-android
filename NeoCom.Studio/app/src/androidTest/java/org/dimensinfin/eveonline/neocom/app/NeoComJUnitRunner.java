package org.dimensinfin.eveonline.neocom.app;

import androidx.test.runner.AndroidJUnitRunner;

public class NeoComJUnitRunner extends AndroidJUnitRunner {
	public NeoComJUnitRunner() {
		System.setProperty("jacoco-agent.destfile", "/sdcard/NeoCom/jacoco.exec");
	}
}
