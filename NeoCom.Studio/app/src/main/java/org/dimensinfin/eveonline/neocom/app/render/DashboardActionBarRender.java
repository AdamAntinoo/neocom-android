package org.dimensinfin.eveonline.neocom.app.render;

public class DashboardActionBarRender {
	private DashboardActionBarRender() {}

	// - B U I L D E R
	public static class Builder {
		private DashboardActionBarRender onConstruction;

		public Builder() {
			this.onConstruction = new DashboardActionBarRender();
		}

		public DashboardActionBarRender build() {
			return this.onConstruction;
		}
	}
}
