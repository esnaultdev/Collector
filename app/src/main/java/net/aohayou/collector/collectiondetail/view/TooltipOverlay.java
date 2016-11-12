package net.aohayou.collector.collectiondetail.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import net.aohayou.collector.R;

public class TooltipOverlay extends RelativeLayout {

    private ElementTooltip tooltip;

    public TooltipOverlay(Context context) {
        super(context);
        init();
    }

    public TooltipOverlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TooltipOverlay(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        tooltip = (ElementTooltip) inflater.inflate(R.layout.element_tooltip, this, true);
        addView(tooltip);
        //tooltip.setVisibility(INVISIBLE);
    }
}
