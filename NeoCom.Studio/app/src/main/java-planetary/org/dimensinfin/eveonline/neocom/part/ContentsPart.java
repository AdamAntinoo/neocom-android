package org.dimensinfin.eveonline.neocom.part;

import android.app.Activity;
import android.widget.TextView;

import org.dimensinfin.android.mvc.core.AbstractRender;
import org.dimensinfin.eveonline.neocom.R;
import org.dimensinfin.eveonline.neocom.part.core.NeoComAbstractPart;
import org.dimensinfin.eveonline.neocom.planetary.ColonyStructure.ColonyContent;
import org.dimensinfin.eveonline.neocom.render.NeoComRender;

public class ContentsPart extends NeoComAbstractPart {
    private static final long serialVersionUID = 8910031270706432316L;

    public static class ContentsRender extends NeoComRender {
        protected TextView contentName = null;
        protected TextView contentAmount = null;
        protected TextView contentVolume = null;
        protected TextView contentValue = null;

        public ContentsRender(final ContentsPart target, final Activity context) {
            super(target, context);
        }

        @Override
        public ContentsPart getPart() {
            return (ContentsPart) super.getPart();
        }

        @Override
        public void initializeViews() {
            super.initializeViews();
            contentName = (TextView) _convertView.findViewById(R.id.contentName);
            contentAmount = (TextView) _convertView.findViewById(R.id.contentAmount);
            contentVolume = (TextView) _convertView.findViewById(R.id.quantityPerCycle);
            contentValue = (TextView) _convertView.findViewById(R.id.cycleTime);
        }

        @Override
        public void updateContent() {
            super.updateContent();
            final ColonyContent model = getPart().getCastedModel();
            contentName.setText(model.getName());
            contentAmount.setText(getPart().getTAmount());
            contentVolume.setText(getPart().getTVolume());
            contentValue.setText(getPart().getTValue());
  //          loadNodeIcon(nodeIcon, model.getTypeId());
        }

        protected void createView() {
            _convertView = inflateView(R.layout.content4list);
            _convertView.setTag(this);
        }
    }

    public ContentsPart(final ColonyContent node) {
        super(node);
    }

    public ColonyContent getCastedModel() {
        return (ColonyContent) this.getModel();
    }

    public long getModelID() {
        return -1;
    }

    @Override
    protected AbstractRender selectRenderer() {
        return new ContentsRender(this, _activity);
    }

    private String getTAmount() {
        return qtyFormatter.format(getCastedModel().getAmount()) + " units";
    }
    private String getTVolume() {
        return qtyFormatter.format(getCastedModel().getVolume()) + "m3";
    }
    private String getTValue() {
        return generatePriceString(getCastedModel().getPrice(),true,true);
    }

}
