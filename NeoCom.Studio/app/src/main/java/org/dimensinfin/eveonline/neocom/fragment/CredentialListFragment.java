//  PROJECT:     NeoCom.Android (NEOC.A)
//  AUTHORS:     Adam Antinoo - adamantinoo.git@gmail.com
//  COPYRIGHT:   (c) 2013-2018 by Dimensinfin Industries, all rights reserved.
//  ENVIRONMENT: Android API22.
//  DESCRIPTION: Android Application related to the Eve Online game. The purpose is to download and organize
//               the game data to help capsuleers organize and prioritize activities. The strong points are
//               help at the Industry level tracking and calculating costs and benefits. Also the market
//               information update service will help to identify best prices and locations.
//               Planetary Interaction and Ship fittings are point under development.
//               ESI authorization is a new addition that will give continuity and allow download game data
//               from the new CCP data services.
//               This is the Android application version but shares libraries and code with other application
//               designed for Spring Boot Angular 4 platform.
//               The model management is shown using a generic Model View Controller that allows make the
//               rendering of the model data similar on all the platforms used.
package org.dimensinfin.eveonline.neocom.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.WorkerThread;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.datasource.MVCDataSource;
import org.dimensinfin.android.mvc.interfaces.IDataSource;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.android.mvc.interfaces.IPartFactory;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.core.model.Separator.ESeparatorType;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.database.entity.Credential;
import org.dimensinfin.eveonline.neocom.datamngmt.AndroidGlobalDataManager;
import org.dimensinfin.eveonline.neocom.model.NewLoginAction;
import org.dimensinfin.eveonline.neocom.part.CredentialPart;
import org.dimensinfin.eveonline.neocom.part.NewLoginActionPart;

//- CLASS IMPLEMENTATION ...................................................................................
public class CredentialListFragment extends AbstractPagerFragment {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - M E T H O D - S E C T I O N ..........................................................................
	@Override
	public IPartFactory createFactory() {
		return new CredentialPartFactory(this.getVariant());
	}

	@Override
	public String getSubtitle() {
		return "";
	}

	@Override
	public String getTitle() {
		return AndroidGlobalDataManager.getResourceString(R.string.activity_title_CredentialList);
	}

	@Override
	protected List<ICollaboration> registerHeaderSource() {
		return new ArrayList<>();
	}

	@Override
	protected IDataSource registerDataSource() {
		logger.info(">< [CredentialListFragment.registerDataSource]");
		return new CredentialListDataSource(new DataSourceLocator().addIdentifier("CREDENTIAL-LIST")
				, getVariant()
				, createFactory()
				, getExtras())
				.setCacheable(false)
				.setVariant(getVariant());
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class CredentialPartFactory extends PartFactory {
		// - F I E L D - S E C T I O N ............................................................................
		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public CredentialPartFactory( final String selectedVariant ) {
			super(selectedVariant);
		}
		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This method returns the matching Part for an input node class. Depending on the class and the variant we can generate a different
		 * set of controllers to build up our model-view-controller structures.
		 * @param node the application node for what we should find a Part and create an instance.
		 * @return new <code>IPart</code> instance that is able to manage this class of node.
		 */
		@Override
		public IPart createPart( final ICollaboration node ) {
			logger.info("-- [AppDashboardFactory.createPart]> Node class: {}" + node.getClass().getSimpleName());
			if ( node instanceof Credential ) {
				IPart part = new CredentialPart((Credential) node).setFactory(this)
				                                                  .setRenderMode(this.getVariant());
				return part;
			}
			if ( node instanceof NewLoginAction ) {
				IPart part = new NewLoginActionPart((NewLoginAction) node).setFactory(this)
				                                                          .setRenderMode(this.getVariant());
				return part;
			}
			return super.createPart(node);
		}
	}
	//...........................................................................................................

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class CredentialListDataSource extends MVCDataSource {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public CredentialListDataSource( final DataSourceLocator locator, final String variant, final IPartFactory factory, final Bundle extras ) {
			super(locator, variant, factory, extras);
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * This is the method that creates the model nodes represent the list of current registered credentials. In addition to this list
		 * there is another item that is the actuator button to initiate the process to register a new credential.
		 */
		@Override
		public void collaborate2Model() {
			logger.info(">> [CredentialListDataSource.collaborate2Model]");
			// Check if the data is already on the cache so we should not have to process again the model generation.
			if ( !isCached() ) {
				try {
					for (Credential credential : AndroidGlobalDataManager.accessAllCredentials()) {
						this.addModelContents(credential);
						logger.info("-- [CredentialListDataSource.collaborate2Model]> Adding '{}' to the _dataModelRoot"
								, credential.getAccountName());
					}

					// Add the node to display the new New Login Button.
					this.addModelContents(new Separator().setType(ESeparatorType.LINE_WHITE));
					this.addModelContents(new NewLoginAction());
				} catch (RuntimeException rtex) {
					AndroidGlobalDataManager.registerRuntimeException("CredentialListDataSource.collaborate2Model"
							, rtex, AndroidGlobalDataManager.EExceptionSeverity.UNEXPECTED);
					rtex.printStackTrace();
				}
			}
			logger.info("<< [CredentialListDataSource.collaborate2Model]");
		}
	}
	//...........................................................................................................
}
// - UNUSED CODE ............................................................................................
