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

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;

import org.dimensinfin.android.mvc.activity.AbstractPagerFragment;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.core.PartFactory;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.core.datasource.AbstractGenerator;
import org.dimensinfin.core.datasource.DataSourceLocator;
import org.dimensinfin.core.datasource.ModelGeneratorStore;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IModelGenerator;
import org.dimensinfin.core.model.RootNode;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.enums.ENeoComVariants;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;
import org.dimensinfin.eveonline.neocom.service.TimeTickReceiver;
import org.dimensinfin.eveonline.neocom.util.JobData;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.concurrent.Future;

//- CLASS IMPLEMENTATION ...................................................................................
public class JobsListFragment extends AbstractPagerFragment {
	public static final class JobDataRender extends BaseExpandableRender {
		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public JobDataRender (final JobDataPart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		@Override
		public JobDataPart getPart () {
			return (JobDataPart) super.getPart();
		}
		@Override
		public void updateContent () {
			logger.info(">> [JobDataRender.updateContent]");
			super.updateContent();
			nodeIcon.setImageDrawable(getDrawable(R.drawable.reprocess));
			name.setText(this.getPart().getCastedModel().getReference());
			childCount.setText(this.getPart().getTStatus());

			// Set this variant labels
			classLabel.setText("UPDATEJOB");
			categoryLabel.setText("STATUS");
			if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ) {
				_convertView.setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderblue, getContext()
						.getTheme()));
			} else _convertView.setBackground(getContext().getResources().getDrawable(R.drawable.uipanelborderblue));
			_convertView.invalidate();
			logger.info("<< [JobDataRender.updateContent]");
		}

		protected void createView () {
			_convertView = inflateView(R.layout.baseexpandablenode);
			_convertView.setTag(this);
		}
	}

	public static final class JobDataPart extends NeoComAbstractPart {
		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public JobDataPart (final JobData node) {
			super(node);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		private JobData getCastedModel () {
			return (JobData) this.getModel();
		}

		@Override
		public long getModelID () {
			return 0;
		}

		public String getTStatus(){
			if(getCastedModel().isRunning())return "PENDING RUN";
			else return "DONE";
		}
		protected AbstractRender selectRenderer () {
			return new JobDataRender(this, _activity);
		}
	}

	public static class CommonPartFactory extends PartFactory {

		public CommonPartFactory (final String selectedVariant) {
			super(selectedVariant);
		}

		public IPart createPart (final ICollaboration node) {
			logger.info("-- [CommonPartFactory.createPart]> Node class: " + node.getClass().getName());
			if ( this.getVariant() == ENeoComVariants.JOB_LIST.name() ) {
				if ( node instanceof JobData ) {
					IPart part = new JobDataPart((JobData) node).setFactory(this)
					                                            .setRenderMode(ENeoComVariants.valueOf(this.getVariant()).name());
					return part;
				}
			}
			return super.createPart(node);
		}
	}

	//- CLASS IMPLEMENTATION ...................................................................................
	public static class JobListGenerator extends AbstractGenerator implements IModelGenerator {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public JobListGenerator (final DataSourceLocator locator, final String variant, final Bundle extras) {
			super(locator, variant);
			setCacheable(false);
		}

		// - M E T H O D - S E C T I O N ..........................................................................

		/**
		 * If the debug flag is true (usually has that value or POCs we should get the model from the mockup model
		 * generator.
		 */
		public RootNode collaborate2Model () {
			AbstractGenerator.logger.info(">> [JobListGenerator.collaborate2Model]");
			// Check if the model is already generated.
			if ( cacheIsInvalid() ) {
				// Get the list of Futures from the UpdaterJobManager and collect their info.
				final Hashtable<String, Future<?>> jobs = TimeTickReceiver.UpdateJobManager.runningJobs;
				final Enumeration<String> jobElements = jobs.keys();
				while (jobElements.hasMoreElements()) {
					final String key = jobElements.nextElement();
					final JobData jobData = new JobData(key)
							.setStatus(jobs.get(key).isDone());
					_dataModelRoot.addChild(jobData);
				}
			}
			AbstractGenerator.logger.info("<< [JobListGenerator.collaborate2Model]");
			return _dataModelRoot;
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................

	//- M E T H O D - S E C T I O N ..........................................................................
	@Override
	public void createFactory () {
		JobsListFragment.logger.info(">> [JobsListFragment.createFactory]");
		this.setFactory(new CommonPartFactory(this.getVariant()));
		JobsListFragment.logger.info("<< [JobsListFragment.createFactory]");
	}

	@Override
	public String getSubtitle () {
		return null;
	}

	/**
	 * Warning. This value has dependencies. I have to simplify this to reduce the number of global methods.
	 *
	 * @return
	 */
	@Override
	public String getTitle () {
		return "Jobs List";
	}

	@Override
	public void registerDataSource () {
		JobsListFragment.logger.info(">> [JobsListFragment.registerDataSource]");
		// Get access to the parameters and reconstruct the data chain if required.
		DataSourceLocator locator = new DataSourceLocator().addIdentifier(this.getVariant());
		// This part of the code may depend on the variant so surround it with the detector.
		IModelGenerator generator = null;
		if ( this.getVariant() == ENeoComVariants.JOB_LIST.name() ) {
			generator = ModelGeneratorStore.registerGenerator(
					new JobListGenerator(locator, this.getVariant(), getExtras()));
		}
		if ( null != generator )
			setGenerator(generator);
		JobsListFragment.logger.info("<< [JobsListFragment.registerDataSource]");
	}

	@Override
	public void setHeaderContents () {
	}
}
//- UNUSED CODE ............................................................................................
