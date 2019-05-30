package com.tlabs.android.evanova.app.activity.character.planetary.part;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;

public class ColonyStructurePart extends NeoComAbstractPart {
    private static final long serialVersionUID = 8910031270706432316L;

    public static class StructureRender extends AbstractRender {

        protected ImageView nodeIcon = null;
        protected TextView structureName = null;
        //	protected TextView lastLaunchTime = null;

        public StructureRender(final ColonyStructurePart target, final Activity context) {
            super(target, context);
        }

        @Override
        public ColonyStructurePart getPart() {
            return (ColonyStructurePart) super.getPart();
        }

        @Override
        public void initializeViews() {
            super.initializeViews();
            nodeIcon = (ImageView) _convertView.findViewById(R.id.nodeIcon);
            structureName = (TextView) _convertView.findViewById(R.id.structureName);
        }

        @Override
        public void updateContent() {
            super.updateContent();
            final Pin model = getPart().getCastedModel();
            final long type = model.getTypeId();
            final String name = model.getStructureTypeCode().name();
            structureName.setText(name);
            //        lastLaunchTime.setText("-");
            nodeIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.planets));
            //       loadNodeIcon(nodeIcon, model.getTypeId());
            // Update the right arrow depending on model content and status.
            //        if ( model instanceof IExpandable ) {
            //            if ( ((IExpandable) model).isEmpty() ) _rightArrow.setVisibility(View.INVISIBLE);
            //        } else _rightArrow.setVisibility(View.GONE);
        }

        protected void createView() {
            _convertView = inflateView(R.layout.facility4list);
            _convertView.setTag(this);
        }
    }

    public ColonyStructurePart(final Pin node) {
        super(node);
    }

    public Pin getCastedModel() {
        return (Pin) this.getModel();
    }

    public long getModelID() {
        return -1;
    }

    public Spanned getTFillLevel() {
        return Html.fromHtml(this.generateLevelColor(this.getCastedModel().getVolumeUsedPct()
            , this.getCastedModel().getVolumeUsedPct()) + "%");
    }

    protected String generateLevelColor(double level, final double data) {
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

    protected AbstractRender selectRenderer() {
        return new StructureRender(this, _activity);
    }
}
