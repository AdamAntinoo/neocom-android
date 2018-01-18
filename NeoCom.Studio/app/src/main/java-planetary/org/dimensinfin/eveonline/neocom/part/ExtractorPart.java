package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure;
import org.dimensinfin.eveonline.neocom.planetary.ColonyCoreStructure.ColonyExtractor;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;
import org.joda.time.Instant;

public class ExtractorPart extends NeoComAbstractPart {
    private static final long serialVersionUID = 8910031270706432316L;

    public static class ExtractorRender extends NeoComRender {

        protected ImageView nodeIcon = null;
        protected ImageView productImage = null;
        protected TextView contentName = null;
        protected TextView headsCounter = null;
        protected TextView productName = null;
        protected TextView cycleTime = null;
        protected TextView quantityPerCycle = null;
        protected TextView timeToComplete = null;
        protected ProgressBar cycleProgress = null;

        public ExtractorRender(final ExtractorPart target, final Activity context) {
            super(target, context);
        }

        @Override
        public ExtractorPart getPart() {
            return (ExtractorPart) super.getPart();
        }

        @Override
        public void initializeViews() {
            super.initializeViews();
            nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
            productImage = (ImageView) _convertView.findViewById(R.id.productImage);
            contentName = (TextView) _convertView.findViewById(R.id.contentName);
            headsCounter = (TextView) _convertView.findViewById(R.id.headsCounter);
            productName = (TextView) _convertView.findViewById(R.id.productName);
            cycleTime = (TextView) _convertView.findViewById(R.id.cycleTime);
            quantityPerCycle = (TextView) _convertView.findViewById(R.id.quantityPerCycle);
            timeToComplete = (TextView) _convertView.findViewById(R.id.timeToComplete);
            cycleProgress = (ProgressBar) _convertView.findViewById(R.id.cycleProgress);
        }

        @Override
        public void updateContent() {
            super.updateContent();
            final ColonyExtractor model = getPart().getCastedModel().getExtractorDetails();
            cycleProgress.setMax(Long.valueOf(model.getCycleTime()).intValue());
            long cycleElapsed = Instant.now().getMillis() - getPart().getCastedModel().getLastCycleStart().getMillis();
            cycleElapsed=cycleElapsed/1000;
            if ( cycleElapsed > model.getCycleTime() ) cycleElapsed = 0;
            contentName.setText("EXTRACTOR");
            headsCounter.setText(qtyFormatter.format(model.getHeads().size()) + " heads");
            productName.setText(model.getProductTypeName());
            cycleTime.setText("Cycle timing: " + qtyFormatter.format(cycleElapsed) + "/" + qtyFormatter.format(model.getCycleTime()) + " seconds");
            quantityPerCycle.setText(qtyFormatter.format(model.getQtyPerCycle()) + " units");
            //           timeToComplete.setText(name);
            nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.extractor80_white));
 //           loadNodeIcon(productImage, model.getProductTypeId());

            cycleProgress.setProgress(Long.valueOf(cycleElapsed).intValue());
            cycleProgress.setScaleY(2f);
        }

        protected void createView() {
            _convertView = inflateView(R.layout.extractor4row);
            _convertView.setTag(this);
        }
    }

    public ExtractorPart(final ColonyCoreStructure node) {
        super(node);
    }

    public ColonyCoreStructure getCastedModel() {
        return (ColonyCoreStructure) this.getModel();
    }

    public long getModelID() {
        return -1;
    }

    protected AbstractRender selectRenderer() {
        return new ExtractorRender(this, _activity);
    }
}
