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
package org.dimensinfin.eveonline.neocom.render;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IMenuActionTarget;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

/**
 * This abstract render will manage all the ui elements on the generic <b>nodebase_decorated</b> layout. This layout has two
 * sections, a left side section with the FrameLayout with the node icon and all the icon modifiers, and a left side section with
 * the expandable right arrow and the optional contextual menu indicator.
 *
 * The render will get access to the part properties and show the corresponding elements that should be active depending on the
 * interfaces covered by the Part.
 * @author Adam Antinoo
 */

// - CLASS IMPLEMENTATION ...................................................................................
public abstract class ANodeBaseDecoratedRender extends AbstractRender {
	// - S T A T I C - S E C T I O N ..........................................................................

	// - F I E L D - S E C T I O N ............................................................................
	// - U I   L E F T   S I D E   F I E L D S
	protected ImageView nodeIcon = null;
	protected ImageView toprightDecorator = null;
	protected ImageView topleftDecorator = null;
	protected ImageView bottomrightDecorator = null;
	protected ImageView bottomleftDecorator = null;
	// - U I   R I G H T   S I D E   F I E L D S
	protected ImageView _rightArrow = null;
	protected ImageView _separator = null;
	protected ImageView _menuIcon = null;


	// - C O N S T R U C T O R - S E C T I O N ................................................................
	public ANodeBaseDecoratedRender( final NeoComAbstractPart newPart, final Activity context ) {
		super(newPart, context);
	}

	// - M E T H O D - S E C T I O N ..........................................................................
	public NeoComAbstractPart getPart() {
		return (NeoComAbstractPart) _part;
	}

	// --- A B S T R A C T R E N D E R   O V E R R I D E S
	@Override
	public void initializeViews() {
		nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
		toprightDecorator = (ImageView) _convertView.findViewById(R.id.toprightDecorator);
		topleftDecorator = (ImageView) _convertView.findViewById(R.id.topleftDecorator);
		bottomrightDecorator = (ImageView) _convertView.findViewById(R.id.bottomrightDecorator);
		bottomleftDecorator = (ImageView) _convertView.findViewById(R.id.bottomleftDecorator);

		_rightArrow = (ImageView) _convertView.findViewById(R.id.rightArrow);
		_separator = (ImageView) _convertView.findViewById(R.id.separator);
		_menuIcon = (ImageView) _convertView.findViewById(R.id.menuIcon);
	}

	@Override
	public void updateContent() {
		// Set the initial setup.
		toprightDecorator.setVisibility(View.GONE);
		topleftDecorator.setVisibility(View.GONE);
		bottomrightDecorator.setVisibility(View.GONE);
		bottomleftDecorator.setVisibility(View.GONE);

		if ( null != _rightArrow )
			_rightArrow.setVisibility(View.GONE);
		if ( null != _separator )
			_separator.setVisibility(View.VISIBLE);
		if ( null != _menuIcon )
			_menuIcon.setVisibility(View.GONE);

		// Check if the model is expandable to show or hide the arrow.
		final ICollaboration targetModel = getPart().getModel();
		if ( null != _rightArrow ) {
			if ( targetModel instanceof IExpandable ) {
				_rightArrow.setVisibility(View.VISIBLE);
				_separator.setVisibility(View.GONE);
				if ( this.getPart().isExpanded() ) {
					_rightArrow.setImageResource(org.dimensinfin.android.mvc.R.drawable.arrowdown);
				} else {
					_rightArrow.setImageResource(org.dimensinfin.android.mvc.R.drawable.arrowright);
				}

				// Check if the click is running and then replace the right arrow by an spinner.
				if ( getPart().clickRunning() ) {
					if ( null != _rightArrow ) {
						logger.info("-- [ANodeBaseDecoratedRender.updateContent]> Activating spinner animation.");
						// The spinner color depends on the Part color theme.
						final Separator.ESeparatorType theme = this.getPart().getThemeColor();
						switch (theme) {
							case LINE_RED:
								_rightArrow.setImageDrawable(getDrawable(R.drawable.progress_spinner_red));
								break;
							case LINE_ORANGE:
								_rightArrow.setImageDrawable(getDrawable(R.drawable.progress_spinner_orange));
								break;
							default:
								_rightArrow.setImageDrawable(getDrawable(R.drawable.progress_spinner_white));
						}
						Animation rotation = AnimationUtils.loadAnimation(getContext(), org.dimensinfin.android.mvc.R.anim.clockwise_rotation);
						rotation.setRepeatCount(Animation.INFINITE);
						_rightArrow.startAnimation(rotation);
					}
				}
			}
		}
		if ( null != _menuIcon ) {
			if ( this.getPart() instanceof IMenuActionTarget )
				_menuIcon.setVisibility(View.VISIBLE);
		}
	}
}
// - UNUSED CODE ............................................................................................
//[01]
