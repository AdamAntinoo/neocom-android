package org.dimensinfin.eveonline.neocom.test.support.converters;

public class CucumberTable2CredentialConverter {
	// - B U I L D E R
	public static class Builder {
		private CucumberTable2CredentialConverter onConstruction;

		public Builder() {
			this.onConstruction = new CucumberTable2CredentialConverter();
		}

		public CucumberTable2CredentialConverter build() {
			return this.onConstruction;
		}
	}
}