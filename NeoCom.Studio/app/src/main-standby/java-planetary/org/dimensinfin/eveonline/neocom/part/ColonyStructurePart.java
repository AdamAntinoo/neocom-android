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
package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.connector.NeoComAppConnector;
import org.dimensinfin.eveonline.neocom.datamngmt.manager.GlobalAndroidDataManager;
import org.dimensinfin.eveonline.neocom.model.EveItem;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.planetary.ColonyStructure;
import org.dimensinfin.eveonline.neocom.planetary.Schematics;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;
import org.dimensinfin.eveonline.neocom.render.NewBaseExpandableRender;
import org.joda.time.DateTime;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ColonyStructurePart extends NeoComAbstractPart {
	// - CLASS IMPLEMENTATION ...................................................................................
	public static class StructureRender extends NeoComRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		protected ImageView nodeIcon = null;
		protected TextView structureName = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public StructureRender (final ColonyStructurePart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public ColonyStructurePart getPart () {
			return (ColonyStructurePart) super.getPart();
		}

		@Override
		public void initializeViews () {
			super.initializeViews();
			nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
			structureName = (TextView) _convertView.findViewById(R.id.structureName);
		}

		@Override
		public void updateContent () {
			super.updateContent();
			final ColonyStructure model = getPart().getCastedModel();
			final long type = model.getTypeId();
			final String name = model.getStructureTypeCode().name();
			structureName.setText(name);
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.planets));
		}

		protected void createView () {
			_convertView = inflateView(R.layout.facility4grouplist);
			_convertView.setTag(this);
		}
	}

	public static class BasicIndustryRender extends NewBaseExpandableRender {
		// - S T A T I C - S E C T I O N ..........................................................................

		// - F I E L D - S E C T I O N ............................................................................
		private ViewGroup nodeContainer;
		protected ImageView nodeIcon = null;
		protected TextView structureType = null;
		private ViewGroup input1Block = null;
		protected TextView input1Name = null;
		protected ImageView input1Icon = null;
		protected TextView input1PctFill = null;
		private ViewGroup input2Block = null;
		protected TextView input2Name = null;
		protected ImageView input2Icon = null;
		protected TextView input2PctFill = null;
		private ViewGroup input3Block = null;
		protected TextView input3Name = null;
		protected ImageView input3Icon = null;
		protected TextView input3PctFill = null;

		protected ImageView productImage = null;
		protected TextView productName = null;
		protected TextView outputData = null;

		protected TextView factoryStatus = null;
		protected ProgressBar cycleTimeProgress = null;
		protected TextView cycleTimeData = null;

		// - C O N S T R U C T O R - S E C T I O N ................................................................
		public BasicIndustryRender (final ColonyStructurePart target, final Activity context) {
			super(target, context);
		}

		// - M E T H O D - S E C T I O N ..........................................................................
		public ColonyStructurePart getPart () {
			return (ColonyStructurePart) super.getPart();
		}

		@Override
		public void initializeViews () {
			super.initializeViews();
			nodeContainer = (ViewGroup) _convertView.findViewById(R.id.nodeContainer);

			nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
			structureType = (TextView) _convertView.findViewById(R.id.structureType);
			input1Block = (ViewGroup) _convertView.findViewById(R.id.input1Block);
			input1Name = (TextView) _convertView.findViewById(R.id.input1Name);
			input1Icon = (ImageView) _convertView.findViewById(R.id.input1Icon);
			input1PctFill = (TextView) _convertView.findViewById(R.id.input1PctFill);
			input2Block = (ViewGroup) _convertView.findViewById(R.id.input2Block);
			input2Name = (TextView) _convertView.findViewById(R.id.input2Name);
			input2Icon = (ImageView) _convertView.findViewById(R.id.input2Icon);
			input2PctFill = (TextView) _convertView.findViewById(R.id.input2PctFill);
			input3Block = (ViewGroup) _convertView.findViewById(R.id.input3Block);
			input3Name = (TextView) _convertView.findViewById(R.id.input3Name);
			input3Icon = (ImageView) _convertView.findViewById(R.id.input3Icon);
			input3PctFill = (TextView) _convertView.findViewById(R.id.input3PctFill);

			productImage = (ImageView) _convertView.findViewById(R.id.productImage);
			productName = (TextView) _convertView.findViewById(R.id.productName);
			outputData = (TextView) _convertView.findViewById(R.id.outputData);

			factoryStatus = (TextView) _convertView.findViewById(R.id.factoryStatus);
			cycleTimeProgress = (ProgressBar) _convertView.findViewById(R.id.cycleTimeProgress);
			cycleTimeData = (TextView) _convertView.findViewById(R.id.cycleTimeData);

			getPart().prepareData();
		}

		@Override
		public void updateContent () {
			super.updateContent();
			int start = -1;
			if ( null != nodeContainer ) {
				// Change the container layout attributes to indent it depending on the level.
				final ViewGroup.MarginLayoutParams marginParams = new ViewGroup.MarginLayoutParams(nodeContainer.getLayoutParams());
				if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ) {
					start = marginParams.getMarginStart();
				} else {
					start = 10;
				}
			}
			final ColonyStructure basicFactory = getPart().getCastedModel();
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.facility80_white));
			structureType.setText("Basic Industry Facility");
			// Hide the 2 and 3 inputs because basic only have one input.
			input2Block.setVisibility(View.GONE);
			input3Block.setVisibility(View.GONE);
			// Set the information for the output.
			loadEveIcon(productImage, getPart().getProductTypeId());
			productName.setText(getPart().getProductName());
			outputData.setText(getPart().getTOutputCount());
			// Update the right arrow depending on model content and status.
			_rightArrow.setVisibility(View.GONE);

			loadEveIcon(input1Icon, getPart().getInputTypeId(1));
			input1Name.setText(getPart().getInputName(1));
			input1PctFill.setText(getPart().getInputPct(1));

			if ( getPart().getCastedModel().getStructureTypeCode() == ColonyStructure.EPlanetaryStructureType
					.ADVANCED_INDUSTRY ) {
				input2Block.setVisibility(View.VISIBLE);
				structureType.setText("Advanced Industry Facility");
				loadEveIcon(input2Icon, getPart().getInputTypeId(2));
				input2PctFill.setText(getPart().getInputPct(2));
			}
			if ( getPart().getCastedModel().getStructureTypeCode() == ColonyStructure.EPlanetaryStructureType
					.HIGH_TECH_PRODUCTION ) {
				input2Block.setVisibility(View.VISIBLE);
				input3Block.setVisibility(View.VISIBLE);
				structureType.setText("Hight-Tech Production Facility");
				loadEveIcon(input2Icon, getPart().getInputTypeId(2));
				input2PctFill.setText(getPart().getInputPct(2));
				loadEveIcon(input3Icon, getPart().getInputTypeId(3));
				input3PctFill.setText(getPart().getInputPct(3));
			}

			int cycleDuration = 0;
			if ( getPart().getCastedModel().getStructureTypeCode() == ColonyStructure.EPlanetaryStructureType.BASIC_INDUSTRY ) {
				cycleDuration = Long.valueOf(TimeUnit.MINUTES.toSeconds(30)).intValue();
				cycleTimeProgress.setMax(cycleDuration);
			}
			if ( getPart().getCastedModel().getStructureTypeCode() == ColonyStructure.EPlanetaryStructureType.ADVANCED_INDUSTRY ) {
				cycleDuration = Long.valueOf(TimeUnit.MINUTES.toSeconds(60)).intValue();
				cycleTimeProgress.setMax(cycleDuration);
			}
			if ( getPart().getCastedModel().getStructureTypeCode() == ColonyStructure.EPlanetaryStructureType.HIGH_TECH_PRODUCTION ) {
				cycleDuration = Long.valueOf(TimeUnit.MINUTES.toSeconds(120)).intValue();
				cycleTimeProgress.setMax(cycleDuration);
			}
			cycleTimeProgress.setProgress(getPart().getCycleProgressSeconds(cycleDuration));
			//			cycleTimeProgress.setScaleY(2f);
		}

		protected void createView () {
			_convertView = inflateView(R.layout.factory4colony);
			_convertView.setTag(this);
		}
	}

	private static class InputDataBlock {
		private String name;
		private int typeId;
		private double percentage;

		private String getName () {
			return name;
		}

		private int getTypeId () {
			return typeId;
		}

		private double getPercentage () {
			return percentage;
		}

		private InputDataBlock setName (final String name) {
			this.name = name;
			return this;
		}

		private InputDataBlock setTypeId (final int typeId) {
			this.typeId = typeId;
			return this;
		}

		private InputDataBlock setPercentage (final double percentage) {
			this.percentage = percentage;
			return this;
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -4985635652531888436L;
	//	private static Logger logger = LoggerFactory.getLogger(ColonyStructurePart.class);

	// - F I E L D - S E C T I O N ............................................................................
	private Integer schematicIdentifier = 0;
	private Vector<Schematics> schematicList = null;
	private Schematics output = null;
	private EveItem outputItem = null;
	private List<InputDataBlock> inputs = new ArrayList<>();

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ColonyStructurePart (final ColonyStructure node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ColonyStructure getCastedModel () {
		return (ColonyStructure) this.getModel();
	}

	public long getModelID () {
		return -1;
	}

	/**
	 * Reads and generates the data structures that later will be shown on the render.
	 */
	public void prepareData () {
		schematicIdentifier = getCastedModel().getSchematicId();
		schematicList = NeoComAppConnector.getSingleton().getCCPDBConnector().selectSchematics4Id(schematicIdentifier);
		for (Schematics target : schematicList) {
			if ( target.getDirection() == Schematics.ESchematicDirection.OUTPUT ) {
				output = target;
			}
		}
		// Generate the input data blocks.
		inputs.clear();
		for (Schematics target : schematicList) {
			if ( target.getDirection() == Schematics.ESchematicDirection.INPUT ) {
				final EveItem item = GlobalAndroidDataManager.searchItemById(target.getTypeId());
				final InputDataBlock inputData = new InputDataBlock()
						.setName(item.getName())
						.setTypeId(target.getTypeId())
						.setPercentage(getInputPctUsage(target.getTypeId()));
				inputs.add(inputData);
			}
		}
	}

	public String getInputName (final int index) {
		return inputs.get(index - 1).getName();
	}

	/**
	 * Get the data for the input number indicated by the index.
	 *
	 * @param index
	 * @return
	 */
	public int getInputTypeId (final int index) {
		return inputs.get(index - 1).getTypeId();
	}

	public String getInputPct (final int index) {
		return percentageZeroFormat.format(inputs.get(index - 1).getPercentage()) + "%";
	}

	public String getTOutputCount () {
		return output.getQty() + " units";
	}

	public Spanned getTFillLevel () {
		return Html.fromHtml(this.generateLevelColor(this.getCastedModel().getVolumeUsedPct()
				, this.getCastedModel().getVolumeUsedPct()) + "%");
	}

	/** Gets the type id for the output produced by the factory. This is obtained form the schematics. */
	public int getProductTypeId () {
		for (Schematics target : schematicList) {
			if ( target.getDirection() == Schematics.ESchematicDirection.OUTPUT ) {
				output = target;
				return target.getTypeId();
			}
		}
		// We never should reach this place.
		return 121;
	}

	public int getCycleProgressSeconds (final int duration) {
		final DateTime install = getCastedModel().getLastCycleStart();
		final double elapsed = (Instant.now().getMillis() - install.getMillis()) / 1000.0;
		if ( elapsed > duration ) return 0;
		else return Double.valueOf(duration - elapsed).intValue();
	}

	/** Gets the name of the output resources for this factory. */
	public String getProductName () {
		if ( null != output ) {
			outputItem = GlobalAndroidDataManager.searchItemById(output.getTypeId());
			return outputItem.getName();
		}
		// We never should reach this place.
		return "-";
	}

	public double getInputPctUsage (final int resourceType) {
		// Match the resource type of input with the strcuture contents.
		final ColonyStructure.ColonyContent content = locateContent4Id(resourceType);
		if ( null != content ) {
			final Schematics input = locateInput4Id(resourceType);
			if ( null != input ) {
				return content.getAmount() / input.getQty() * 100.0;
				//			return percentageFormat.format(percentage) + "%";
			}
		}
		return 0.0;
	}

	private ColonyStructure.ColonyContent locateContent4Id (final int typeid) {
		final List<ColonyStructure.ColonyContent> contentList = getCastedModel().getContentList();
		for (ColonyStructure.ColonyContent content : contentList) {
			if ( content.getTypeId() == typeid ) return content;
		}
		return null;
	}

	private Schematics locateInput4Id (final int typeid) {
		final List<ColonyStructure.ColonyContent> contentList = getCastedModel().getContentList();
		for (Schematics schematic : schematicList) {
			if ( schematic.getTypeId() == typeid ) return schematic;
		}
		return null;
	}

	protected String generateLevelColor (double level, final double data) {
		StringBuffer htmlFragmentWithColor = new StringBuffer();
		String levelColor = "#00A000";
		// Get the color from the table.
		if ( level > 75.0 ) {
			levelColor = "#A0A000";
		}
		if ( level > 90.0 ) {
			levelColor = "#A00000";
		}
		htmlFragmentWithColor.append("<font color='").append(levelColor).append("'>").append(qtyFormatter.format(data)).append("</font>");
		//       htmlFragmentWithColor.append("%");
		return htmlFragmentWithColor.toString();
	}

	/**
	 * The render selected will depend on the PlanetaryFacility type stored on the Model.
	 */
	protected AbstractRender selectRenderer () {
		final ColonyStructure.EPlanetaryStructureType structureType = getCastedModel().getStructureTypeCode();
		switch (structureType) {
			case BASIC_INDUSTRY:
			case ADVANCED_INDUSTRY:
			case HIGH_TECH_PRODUCTION:
				return new BasicIndustryRender(this, _activity);
		}
		return new StructureRender(this, _activity);
	}

	@Override
	public String toString () {
		StringBuffer buffer = new StringBuffer("ColonyStructurePart [");
		buffer.append("Model: ").append(getCastedModel().toString());
		buffer.append("]");
		//		buffer.append("->").append(super.toString());
		return buffer.toString();
	}
}
