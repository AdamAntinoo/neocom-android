package org.dimensinfin.eveonline.neocom.mining.render;

import android.content.Context;
import android.widget.ImageView;
import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.app.render.core.NeoComRender;
import org.dimensinfin.eveonline.neocom.domain.IItemFacet;

public abstract class ResourceFacetedRender extends NeoComRender {
	private ImageView nodeIcon;

	public ResourceFacetedRender( @NonNull final IAndroidController controller, @NonNull final Context context ) {
		super(controller, context);
	}

	@Override
	public void initializeViews() {
		super.initializeViews();
		this.nodeIcon = this.getView().findViewById(R.id.nodeIcon);
	}

	@Override
	public void updateContent() {
		super.updateContent();
		if (null != this.nodeIcon) {
			final Object model = this.getController().getModel();
			if (model instanceof IItemFacet) {
				String link = ((IItemFacet)model).getURLForItem();
				Picasso.get().load(link).into(this.nodeIcon);
			}
		}
	}
}
