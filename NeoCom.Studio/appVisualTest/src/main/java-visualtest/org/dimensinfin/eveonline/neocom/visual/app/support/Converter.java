package org.dimensinfin.eveonline.neocom.visual.app.support;

@FunctionalInterface
public interface Converter<S, T> {
	T convert( S source );
}
