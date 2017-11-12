//	PROJECT:        corebase.model (CORE.M)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2016 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Java 1.6.
//	DESCRIPTION:		Library that defines the model classes to implement the core for a GEF based
//									Model-View-Controller. Code is as neutral as possible and made to be reused
//									on all Java development projects.
package org.dimensinfin.core.model;

//- IMPORT SECTION .........................................................................................
import java.util.HashMap;

// - CLASS IMPLEMENTATION ...................................................................................
/**
 * This class defines ans stores all the events that are going to be fired by Model changes. The
 * implementation is open to new events from other classes by a registration mechanism so the new class
 * defines Enumerated values that are registered into this class for later user by any other class that may or
 * may not have access to the former enumerated. <br>
 * All event identifier is stored in its String or int codifications that can be used to compare to the
 * enumerated original values when required.
 * 
 * @author Adam Antinoo
 */
public class CEventModel {
	public enum ECoreModelEvents {
		DEFAULT_MODELEVENT, EVENT_CHILD_ADDEDPROP, EVENT_CHILD_REMOVEDPROP, EVENT_EXPANDCOLLAPSENODE, EVENT_DOWNLOADNODE, EVENT_ALTERVISIBLESTATENODE
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	//	private static Logger										logger	= Logger.getLogger("CEventModel");
	private static HashMap<Integer, String> events = new HashMap<Integer, String>();

	public static String getName4Event(final int eventCode) {
		// Search for this event id in the list of registered events.
		String event = CEventModel.events.get(eventCode);
		if (null != event)
			return event;
		else
			return ECoreModelEvents.DEFAULT_MODELEVENT.name();
	}

	public static void register(final int code, final String name) {
		CEventModel.events.put(code, name);
	}

	// - F I E L D - S E C T I O N ............................................................................
	// - C O N S T R U C T O R - S E C T I O N ................................................................
	// - M E T H O D - S E C T I O N ..........................................................................
}
// - UNUSED CODE ............................................................................................
