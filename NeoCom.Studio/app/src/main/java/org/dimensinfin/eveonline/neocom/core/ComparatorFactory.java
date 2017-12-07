//	PROJECT:        NeoCom.model (NEOC.M)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Isolated model structures to access and manage Eve Online character data and their
//									available databases.
//									This version includes the access to the latest 6.x version of eveapi libraries to
//									download ad parse the CCP XML API data.
//									Code integration that is not dependent on any specific platform.
package org.dimensinfin.gymschedule.constants;

import java.util.Comparator;

import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;

// - CLASS IMPLEMENTATION ...................................................................................
public class ComparatorFactory {
	// - S T A T I C - S E C T I O N ..........................................................................
	public enum EComparatorField {
		NAME
	}

	public static Comparator<IPart> createComparator(final EComparatorField code) {
		Comparator<IPart> comparator = new Comparator<IPart>() {
			@Override
			public int compare(final IPart left, final IPart right) {
				return 0;
			}
		};
		switch (code) {
			case NAME:
				comparator = new Comparator<IPart>() {
					@Override
					public int compare(final IPart left, final IPart right) {
						String leftField = null;
						String rightField = null;
						if (left instanceof INamedPart) {
							leftField = ((INamedPart) left).getName();
						}
						if (right instanceof INamedPart) {
							rightField = ((INamedPart) right).getName();
						}

						if (null == leftField) return 1;
						if (null == rightField) return -1;
						if ("" == leftField) return 1;
						if ("" == rightField) return -1;
						return leftField.compareTo(rightField);
					}
				};
				break;
		}
		return comparator;
	}

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
}

// - UNUSED CODE ............................................................................................
