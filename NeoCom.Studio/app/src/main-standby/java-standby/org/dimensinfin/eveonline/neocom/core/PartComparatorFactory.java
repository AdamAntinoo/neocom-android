//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.core;


import org.dimensinfin.android.mvc.interfaces.INamedPart;
import org.dimensinfin.android.mvc.interfaces.IPart;

import java.util.Comparator;
import java.util.logging.Logger;

// - CLASS IMPLEMENTATION ...................................................................................
public class PartComparatorFactory {
	public enum ComparatorType{
		COMPARATOR_NAME,COMPARATOR_ITEM_TYPE
	}
	// - S T A T I C - S E C T I O N ..........................................................................
	private static Logger	logger	= Logger.getLogger("PartComparatorFactory");

	public static Comparator<IPart> createPartComparator(final ComparatorType code) {
		Comparator<IPart> comparator = new Comparator<IPart>() {
			public int compare(final IPart left, final IPart right) {
				return 0;
			}
		};
		switch (code) {
			case COMPARATOR_NAME:
				comparator = new Comparator<IPart>() {
					public int compare(final IPart left, final IPart right) {
						String leftField = null;
						String rightField = null;
						if (left instanceof INamedPart ) {
							leftField = ((INamedPart) left).getName();
						}
						if (right instanceof INamedPart) {
							rightField = ((INamedPart) right).getName();
						}
						//					if (left instanceof INamed) {
						//						leftField = ((INamed) left).getOrderingName();
						//					}
						//					if (right instanceof INamed) {
						//						rightField = ((INamed) right).getOrderingName();
						//					}

						if (null == leftField) return 1;
						if (null == rightField) return -1;
						if ("" == leftField) return 1;
						if ("" == rightField) return -1;
						return leftField.compareTo(rightField);
					}
				};
				break;
//			case AppWideConstants.comparators.COMPARATOR_GROUPNAME:
//				comparator = new Comparator<IPart>() {
//					public int compare(final IPart left, final IPart right) {
//						String leftField = null;
//						String rightField = null;
//						if (left instanceof AssetPart) {
//							leftField = ((AssetPart) left).getCastedModel().getGroupName();
//						}
//						if (right instanceof AssetPart) {
//							rightField = ((AssetPart) right).getCastedModel().getGroupName();
//						}
//
//						if (null == leftField) return 1;
//						if (null == rightField) return -1;
//						if ("" == leftField) return 1;
//						if ("" == rightField) return -1;
//						return leftField.compareTo(rightField);
//					}
//				};
//				break;
			case COMPARATOR_ITEM_TYPE:
				comparator = new Comparator<IPart>() {
					public int compare(final IPart left, final IPart right) {
						int leftField = -1;
						int rightField = -1;
						// if (left instanceof BlueprintPart) leftField = 100;
//						if (left instanceof AssetPart) {
//							leftField = 0;
//						}
//						if (left instanceof ShipPart) {
//							leftField = 200;
//						}
//						if (left instanceof Container4AssetsPart) {
//							leftField = -300;
//						}
//
//						// if (right instanceof BlueprintPart) rightField = 100;
//						if (right instanceof AssetPart) {
//							rightField = 0;
//						}
//						if (right instanceof ShipPart) {
//							rightField = 200;
//						}
//						if (right instanceof Container4AssetsPart) {
//							rightField = -300;
//						}

						if (leftField < rightField) return -1;
						if (leftField > rightField) return 1;
						return 0;
					}
				};
				break;
//			case AppWideConstants.comparators.COMPARATOR_CARD_RATIO:
//				comparator = new Comparator<IPart>() {
//					public int compare(final IPart left, final IPart right) {
//						double leftField = 0.0;
//						double rightField = 0.0;
//						if (left instanceof BlueprintPart) {
//							leftField = ((BlueprintPart) left).getProfitIndex();
//						}
//						if (right instanceof BlueprintPart) {
//							rightField = ((BlueprintPart) right).getProfitIndex();
//						}
//
//						if (leftField > rightField)
//							return -1;
//						else if (leftField == rightField) return 0;
//						return 1;
//					}
//				};
//				break;
//			case AppWideConstants.comparators.COMPARATOR_NEWESTDATESORT:
//				comparator = new Comparator<IPart>() {
//					public int compare(final IPart left, final IPart right) {
//						DateTime leftField = new DateTime(DateTimeZone.UTC);
//						DateTime rightField = new DateTime(DateTimeZone.UTC);
//						if (left instanceof IDateTimeComparator) {
//							leftField = ((IDateTimeComparator) left).getComparableDate();
//						}
//						if (right instanceof IDateTimeComparator) {
//							rightField = ((IDateTimeComparator) right).getComparableDate();
//						}
//
//						if (leftField.isAfter(rightField))
//							return -1;
//						else
//							return 1;
//					}
//				};
//				break;
		}
		return comparator;
	}

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public PartComparatorFactory() {
	}

	// - M E T H O D - S E C T I O N ..........................................................................
}

// - UNUSED CODE ............................................................................................
