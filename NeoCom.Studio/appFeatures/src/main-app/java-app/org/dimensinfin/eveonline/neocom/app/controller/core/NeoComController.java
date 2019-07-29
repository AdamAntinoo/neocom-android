package org.dimensinfin.eveonline.neocom.app.controller.core;

import androidx.annotation.NonNull;

import org.dimensinfin.android.mvc.controller.AndroidController;
import org.dimensinfin.android.mvc.controller.IAndroidController;
import org.dimensinfin.android.mvc.domain.IContainer;
import org.dimensinfin.android.mvc.interfaces.IControllerFactory;
import org.dimensinfin.android.mvc.support.SeparatorController;
import org.dimensinfin.core.interfaces.ICollaboration;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.core.model.Separator;
import org.dimensinfin.eveonline.neocom.R;

import java.util.List;

public abstract class NeoComController<M extends ICollaboration> extends AndroidController<M> {
	private Separator.ESeparatorType theme = Separator.ESeparatorType.DEFAULT;

	public NeoComController( @NonNull final M model, @NonNull final IControllerFactory factory ) {
		super(model, factory);
	}

	/**
	 * On the NeoCom implementation the expnadable item will add the colored separation at the beginning and at the end of the list.
	 *
	 * @param contentCollector the list where we are collecting the Controllers visible for rendering.
	 */
	@Override
	public void collaborate2View( final List<IAndroidController> contentCollector ) {
		logger.info(">< [AndroidController.collaborate2View]> Collaborator: {}", this.getClass().getSimpleName());
		// If the node is expanded then give the children the opportunity to also be added.
		if (this.getModel() instanceof IExpandable) {
			// --- This is the section that is different for any AndroidController.
			List<IAndroidController> ch = this.orderingFeature(this.getChildren());
			logger.info("-- [AndroidController.collaborate2View]> Collaborator children: {}", ch.size());
			// --- End of policies
			if (((IExpandable) this.getModel()).isExpanded())
				contentCollector.add(new SeparatorController(new Separator().setType(this.getEncaseTheme()), this.getControllerFactory()));
			// Add this node to the list of controllers only if it should be visible.
			if (this.isVisible()) contentCollector.add(this);
			if (((IExpandable) this.getModel()).isExpanded())
				for (IAndroidController controller : ch) {
					controller.collaborate2View(contentCollector);
				}
			if (((IExpandable) this.getModel()).isExpanded())
				contentCollector.add(new SeparatorController(new Separator().setType(this.getEncaseTheme()), this.getControllerFactory()));
		} else if (this instanceof IContainer) {
			if (this.isVisible()) contentCollector.add(this);
			if (((IContainer) this).wants2Collaborate()) {
				List<IAndroidController> ch = this.orderingFeature(this.getChildren());
				for (IAndroidController controller : ch) {
					controller.collaborate2View(contentCollector);
				}
			}
		} else if (this.isVisible()) contentCollector.add(this);
	}

	public Separator.ESeparatorType getEncaseTheme() {
		return this.theme;
	}

	public NeoComController<M> setTheme( final Separator.ESeparatorType theme ) {
		this.theme = theme;
		return this;
	}

	public int drawableBorder() {
		switch (this.theme) {
			case LINE_RED:
				return R.drawable.uipanelborderred;
			case LINE_YELLOW:
				return R.drawable.uipanelborderyellow;
			case LINE_GREEN:
				return R.drawable.uipanelbordergreen;
			case LINE_LIGHTBLUE:
				return R.drawable.uipanelborderlightblue;
			case LINE_DARKBLUE:
				return R.drawable.uipanelborderdarkblue;
			case LINE_PURPLE:
				return R.drawable.uipanelborderpurple;
		}
		return R.drawable.uipanelborderwhite;
	}
}
