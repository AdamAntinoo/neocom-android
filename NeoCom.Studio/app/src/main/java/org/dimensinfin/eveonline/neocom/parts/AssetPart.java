package org.dimensinfin.eveonline.neocom.parts;

import org.dimensinfin.android.mvc.core.AbstractAndroidPart;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.android.mvc.interfaces.IPart;
import org.dimensinfin.eveonline.neocom.models.NeoComAsset;

import java.util.Vector;

/**
 * Created by Adam on 07/11/2017.
 */

public class AssetPart extends AbstractAndroidPart {
	public AssetPart (final NeoComAsset node) {
		super (node);
	}

	@Override
	public long getModelID () {
		return getCastedModel ().getAssetId();
	}

	@Override
	protected AbstractRender selectRenderer () {
		return null;
	}

	public NeoComAsset getCastedModel () {
		return (NeoComAsset) getModel ();
	}

	@Override
	public Vector<IPart> runPolicies (final Vector<IPart> targets) {
		return targets;
	}
}
