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
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;

/**
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public class ColonyStructurePart extends NeoComAbstractPart {
	// - CLASS IMPLEMENTATION ...................................................................................
	public static class StructureRender extends BaseExpandableRender {
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
			final ColonyCoreStructure model = getPart().getCastedModel();
			final long type = model.getTypeId();
			final String name = model.getStructureTypeCode().name();
			structureName.setText(name);
			nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.planets));
		}

		protected void createView () {
			_convertView = inflateView(R.layout.structure4list);
			_convertView.setTag(this);
		}
	}

	// - S T A T I C - S E C T I O N ..........................................................................
	private static final long serialVersionUID = -4985635652531888436L;
//	private static Logger logger = LoggerFactory.getLogger(ColonyStructurePart.class);

	// - F I E L D - S E C T I O N ............................................................................

	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ColonyStructurePart (final ColonyCoreStructure node) {
		super(node);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public ColonyCoreStructure getCastedModel () {
		return (ColonyCoreStructure) this.getModel();
	}

	public long getModelID () {
		return -1;
	}

	public Spanned getTFillLevel () {
		return Html.fromHtml(this.generateLevelColor(this.getCastedModel().getVolumeUsedPct()
				, this.getCastedModel().getVolumeUsedPct()) + "%");
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

	protected AbstractRender selectRenderer () {
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
