package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.dimensinfin.android.mvc.constants.SystemWideConstants;
import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.core.interfaces.IExpandable;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure.EPlanetaryStructureType;
import org.dimensinfin.eveonline.neocom.render.BaseExpandableRender;

public class StructureWithContentsPart extends ColonyStructurePart implements View.OnClickListener {
    private static final long serialVersionUID = 8910031270706432316L;

    public static class CommandCenterRender extends BaseExpandableRender {
        protected TextView structureType = null;
        protected TextView fillLevelNumber = null;
        protected ProgressBar storageUsedProgress = null;

        public CommandCenterRender(final ColonyStructurePart target, final Activity context) {
            super(target, context);
        }

        @Override
        public ColonyStructurePart getPart() {
            return (ColonyStructurePart) super.getPart();
        }

        @Override
        public void initializeViews() {
            super.initializeViews();
            structureType = (TextView) _convertView.findViewById(R.id.structureType);
            fillLevelNumber = (TextView) _convertView.findViewById(R.id.fillLevelNumber);
            storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
        }

        @Override
        public void updateContent() {
            super.updateContent();
            final ColonyCoreStructure model = getPart().getCastedModel();
            structureType.setText("COMMAND CENTER");
            fillLevelNumber.setText(getPart().getTFillLevel());
            storageUsedProgress.setMax(100);
            storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
            storageUsedProgress.setScaleY(2f);
            //	setFillLevel();
            //		installationsNumber.setText(model.get);
            //		final long time = model.getLastLaunchTime();
            //		lastLaunchTime.setText("-");
            nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.commandcenter80_white));

            //		loadNodeIcon(nodeIcon, model.getTypeId());
            // Update the right arrow depending on model content and status.
            if ( model instanceof IExpandable ) {
                if ( ((IExpandable) model).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
            } else _rightArrow.setVisibility(View.GONE);
        }

//        private void setFillLevel() {
//            final int capacity = 12000;
//            double fillLevel = 0.0;
//            final List<PinContent> contents = getPart().getCastedModel().getContent();
//            if ( null != contents ) {
//                fillLevel = 0.0;
//                for (PinContent content : contents) {
//                    fillLevel += content.getAmount() * 0.1;
//                }
//            }
//            final double progress = fillLevel / capacity * 100.0;
//        }

        protected void createView() {
            _convertView = inflateView(R.layout.commandcenter4row);
            _convertView.setTag(this);
        }
    }

    public static class LaunchpadRender extends CommandCenterRender {
        //        protected TextView structureType = null;
        //        protected TextView fillLevelNumber = null;
        //        protected ProgressBar storageUsedProgress = null;

        public LaunchpadRender(final ColonyStructurePart target, final Activity context) {
            super(target, context);
        }

        //        @Override
        //        public ColonyStructurePart getPart() {
        //            return (ColonyStructurePart) super.getPart();
        //        }
        //
        //        @Override
        //        public void initializeViews() {
        //            super.initializeViews();
        //            structureType = (TextView) _convertView.findViewById(R.id.structureType);
        //            fillLevelNumber = (TextView) _convertView.findViewById(R.id.fillLevelNumber);
        //            storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
        //        }

        @Override
        public void updateContent() {
            super.updateContent();
            //      final Pin model = getPart().getCastedModel();
            structureType.setText("LAUNCHPAD");
            //            fillLevelNumber.setText(getPart().getTFillLevel());
            //            storageUsedProgress.setMax(100);
            //            storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
            //            storageUsedProgress.setScaleY(2f);
            nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.launchpad80_white));
            // Update the right arrow depending on model content and status.
            //            if ( getPart().getCastedModel() instanceof IExpandable ) {
            //                if ( ((IExpandable) getPart().getCastedModel()).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
            //            } else _rightArrow.setVisibility(View.GONE);
        }

        //         protected void createView() {
        //            _convertView = inflateView(R.layout.commandcenter4row);
        //            _convertView.setTag(this);
        //        }
    }

    public static class StorageRender extends CommandCenterRender {
        //        protected TextView structureType = null;
        //        protected TextView fillLevelNumber = null;
        //        protected ProgressBar storageUsedProgress = null;

        public StorageRender(final ColonyStructurePart target, final Activity context) {
            super(target, context);
        }

        //        @Override
        //        public ColonyStructurePart getPart() {
        //            return (ColonyStructurePart) super.getPart();
        //        }
        //
        //        @Override
        //        public void initializeViews() {
        //            super.initializeViews();
        //            structureType = (TextView) _convertView.findViewById(R.id.structureType);
        //            fillLevelNumber = (TextView) _convertView.findViewById(R.id.fillLevelNumber);
        //            storageUsedProgress = (ProgressBar) _convertView.findViewById(R.id.storageUsedProgress);
        //        }

        @Override
        public void updateContent() {
            super.updateContent();
            //      final Pin model = getPart().getCastedModel();
            structureType.setText("STORAGE");
            //            fillLevelNumber.setText(getPart().getTFillLevel());
            //            storageUsedProgress.setMax(100);
            //            storageUsedProgress.setProgress(Double.valueOf(getPart().getCastedModel().getVolumeUsedPct()).intValue());
            //            storageUsedProgress.setScaleY(2f);
            nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.storage80_white));
            // Update the right arrow depending on model content and status.
            //            if ( getPart().getCastedModel() instanceof IExpandable ) {
            //                if ( ((IExpandable) getPart().getCastedModel()).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
            //            } else _rightArrow.setVisibility(View.GONE);
        }

        //         protected void createView() {
        //            _convertView = inflateView(R.layout.commandcenter4row);
        //            _convertView.setTag(this);
        //        }
    }

    public StructureWithContentsPart(final ColonyCoreStructure node) {
        super(node);
    }

    public ColonyCoreStructure getCastedModel() {
        return (ColonyCoreStructure) this.getModel();
    }

    public long getModelID() {
        return -1;
    }

    /**
     * The render selected will depend on the Structure type stored on the Model.
     */
    protected AbstractRender selectRenderer() {
        final EPlanetaryStructureType structureType = getCastedModel().getStructureTypeCode();
        switch (structureType) {
            case COMMAND_CENTER:
                return new CommandCenterRender(this, _activity);
            case LAUNCHPAD:
                return new LaunchpadRender(this, _activity);
            case STORAGE:
                return new StorageRender(this, _activity);
        }
        return new StructureRender(this, _activity);
    }

    @Override
    public void onClick(final View v) {
        logger.info(">> [StructureWithContentsPart.onClick]");
        // Do the action and invalidate the display.
        toggleExpanded();
        invalidate();
        fireStructureChange(SystemWideConstants.events.EVENTSTRUCTURE_ACTIONEXPANDCOLLAPSE.name(), this, this);
        logger.info("<< [StructureWithContentsPart.onClick]");
    }
}
