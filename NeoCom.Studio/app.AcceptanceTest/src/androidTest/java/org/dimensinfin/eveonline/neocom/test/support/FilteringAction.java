package org.dimensinfin.eveonline.neocom.test.support;

public class FilteringAction {
	private ControllerMatcher matcher;

	public FilteringAction() {}

	public FilteringAction( final ControllerMatcher matcher ) {
		this.matcher = matcher;
	}
//	public List<IAndroidController> withPanelClass ( final Class filterClass){
//		final Filter<filterClass> results = new ArrayList<>();
//		for (IAndroidController controller : this.controllerList)
//			if (type.isInstance( controller ))
//				results.add( (C) controller );
//		return results;
//	}

	// - B U I L D E R
//	public static class Builder {
//		private FilteringAction onConstruction;
//
//		public Builder() {
//			this.onConstruction = new FilteringAction();
//		}
//
//		public FilteringAction build() {
//			return this.onConstruction;
//		}
//	}
}
