package org.dimensinfin.eveonline.neocom.mining;

public class ExtractionSystem {
	private int systemId;
	private String systemName;

	public int getSystemId() {
		return this.systemId;
	}

	public String getSystemName() {
		return this.systemName;
	}

	// - B U I L D E R
	public static class Builder {
		private ExtractionSystem onConstruction;

		public Builder() {
			this.onConstruction = new ExtractionSystem();
		}

		public Builder withSystemId( final int systemId ) {
			this.onConstruction.systemId = systemId;
			return this;
		}

		public Builder withSystemName( final String systemName ) {
			this.onConstruction.systemName = systemName;
			return this;
		}

		public ExtractionSystem build() {
			return this.onConstruction;
		}
	}
}