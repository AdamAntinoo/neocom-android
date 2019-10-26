package org.dimensinfin.eveonline.neocom.app.utilities;

public class InitializationException {
	// - B U I L D E R
	public static class Builder {
		private InitializationException onConstruction;

		public Builder() {
			this.onConstruction = new InitializationException();
		}

		public InitializationException.Builder withException( final Exception exception ) {
			return this;
		}

		public InitializationException build() {
			return this.onConstruction;
		}
	}
}