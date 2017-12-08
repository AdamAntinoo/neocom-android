//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API22.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. This is the Android application but shares
//                  libraries and code with other application designed for alternate platforms.
//                  The model management is shown using a generic Model View Controller that allows make the
//                  rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.model;

import org.dimensinfin.core.interfaces.ICollaboration;
import org.joda.time.Duration;
import org.joda.time.Instant;

import java.util.List;
import java.util.Vector;

//- CLASS IMPLEMENTATION ...................................................................................
public class RunnableTask extends NeoComNode implements ICollaboration {
	public enum ERunState {
		WAITING, RUNNING, COMPLETED_OK, COMPLETED_FAIL, ABORTED, INTERRUPTED
	}
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	private String description = "-DESCRIPTION-";
	private Runnable runnableBlock = null;
	private ERunState state = ERunState.WAITING;
	private Instant timeStamp = Instant.now();
	private Duration elapsedTime = null;

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public RunnableTask () {
		super();
	}

	public RunnableTask (String newDescription) {
		this();
		description = newDescription;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public List<ICollaboration> collaborate2Model (final String variant) {
		return new Vector<ICollaboration>();
	}

	public String getDescription () {
		return description;
	}

	public RunnableTask setDescription (final String description) {
		this.description = description;
		return this;
	}

	public Runnable getRunnableBlock () {
		return runnableBlock;
	}

	public RunnableTask setRunnableBlock (final Runnable runnableBlock) {
		this.runnableBlock = runnableBlock;
		return this;
	}

	/**
	 * Return the number of seconds elapsed since the creation and start of this task.
	 *
	 * @return elapsed time in seconds.
	 */
	public long getElapsed () {
		// If the task is completed the return the last elapsed.
		if ( (state == ERunState.COMPLETED_OK) || (state == ERunState.COMPLETED_FAIL) )
			return elapsedTime.getMillis() / 1000;
		else {
			elapsedTime = new Duration(timeStamp, new Instant());
		}
		return elapsedTime.getMillis() / 1000;
	}

	public ERunState getState () {
		return state;
	}

	public void setState (ERunState newState) {
		state = newState;
	}

	public void complete (ERunState newState) {
		elapsedTime = new Duration(timeStamp, new Instant());
		state = newState;
	}
}
