//	PROJECT:        NeoCom.Android (NEOC.A)
//	AUTHORS:        Adam Antinoo - adamantinoo.git@gmail.com
//	COPYRIGHT:      (c) 2013-2017 by Dimensinfin Industries, all rights reserved.
//	ENVIRONMENT:		Android API16.
//	DESCRIPTION:		Application to get access to CCP api information and help manage industrial activities
//									for characters and corporations at Eve Online. The set is composed of some projects
//									with implementation for Android and for an AngularJS web interface based on REST
//									services on Sprint Boot Cloud.
package org.dimensinfin.eveonline.neocom.datasource;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

import org.dimensinfin.android.datasource.DataSourceLocator;
import org.dimensinfin.android.interfaces.IModelGenerator;
import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.RootPart;
import org.dimensinfin.android.mvc.datasource.SpecialDataSource;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.eveonline.neocom.NeoComApp;
import org.dimensinfin.eveonline.neocom.constant.AppWideConstants;

// - CLASS IMPLEMENTATION ...................................................................................
public class NeoComDataSource extends SpecialDataSource {
	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -33788421750264209L;

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public NeoComDataSource(final DataSourceLocator locator, final IPartFactory partFactory,
			final IModelGenerator generator) {
		super(locator, partFactory);
		if (null == generator) throw new RuntimeException(
				"EX [LoginListDataSource.<initialize>]>UNEX. The Model Generator should not be empty.");
		_modelGenerator = generator;
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	//	/**
	//	 * This method is called to initialize the model structures. Those structures are copies or transformations
	//	 * from the main model stored in the <code>store</code> reference. <br>
	//	 * This is the method to initialize the copy of the model structures on the DataSource. Every time this
	//	 * method is called, the complete model is recreated. There are two ways to recreate it, comparing with the
	//	 * old copy and inserting/deleting different nodes or recreating completely the new model copy. Once this
	//	 * method is called we can create the depending part hierarchy. In the current implementation we always
	//	 * recreate the model from scratch.<br>
	//	 * The resulting model always has a RootNode and the contents are stored as children of that node. The model
	//	 * only deals with the first level so each on the childs will create their own set of the model on call when
	//	 * required by the model transformation.
	//	 */
	//	@Override
	//	public RootNode collaborate2Model() {
	//		SpecialDataSource.logger.info(">< [LoginListDataSource.collaborate2Model]");
	//		_dataModelRoot = modelGenerator.collaborate2Model();
	//		return _dataModelRoot;
	//	}

	/**
	 * Set the RootPart and the sort element for it.
	 */
	@Override
	public void createContentHierarchy() {
		// Do it only if the RootPart is not already set.
		if (null == _partModelRoot) {
			_partModelRoot = new RootPart(_dataModelRoot, _partFactory)
					.setSorting(NeoComApp.createPartComparator(AppWideConstants.comparators.COMPARATOR_NAME));
		}
		super.createContentHierarchy();
	}

	@Override
	public ArrayList<AbstractAndroidPart> getHeaderParts() {
		return new ArrayList<AbstractAndroidPart>();
	}

	/**
	 * This method is called whenever there is an event on any Part related to this DataSource. We just process
	 * structure changes that need the DataSource to reconstruct the Part model from the new Model state. Wrong.
	 * The model does not change but the result from the collaborate2View transformation does.
	 */
	@Override
	public void propertyChange(final PropertyChangeEvent event) {
		// The expand/collapse state has changed.
		if (SystemWideConstants.events
				.valueOf(event.getPropertyName()) == SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE) {
			// REFACTOR Trying to fire a new model generation to solve the problem of renderization.
			//	_dataModelRoot = modelGenerator.collaborate2Model();
			this.createContentHierarchy();

			_bodyParts = new ArrayList<IPart>();
			_bodyParts.addAll(_partModelRoot.collaborate2View());
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), event.getOldValue(),
					event.getNewValue());
			return;
		}
		// TODO Check if we should get this event and fire it again.
		if (SystemWideConstants.events
				.valueOf(event.getPropertyName()) == SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES) {
			this.fireStructureChange(SystemWideConstants.events.EVENTADAPTER_REQUESTNOTIFYCHANGES.name(), event.getOldValue(),
					event.getNewValue());
			return;
		}
		super.propertyChange(event);
	}
}

// - UNUSED CODE ............................................................................................
