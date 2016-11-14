package net.aohayou.collector.collectiondetail.view;

import android.animation.Animator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
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
        tooltip = (ElementTooltip) inflater.inflate(R.layout.formula_element_tooltip, this, false);
        addView(tooltip);
        tooltip.setVisibility(INVISIBLE);
        tooltip.setDirection(ElementTooltip.Direction.TOP_LEFT);
    }

    public void displayTooltip(@NonNull View view, @NonNull String text) {
        tooltip.setRight(view.getLeft() + tooltip.getWidth());
        tooltip.setBottom(view.getTop() + tooltip.getHeight());
        tooltip.setLeft(view.getLeft());
        tooltip.setTop(view.getTop());

        float finalRadius = (float) Math.hypot(tooltip.getWidth(), tooltip.getHeight());
        Animator anim =
                ViewAnimationUtils.createCircularReveal(tooltip, 0, 0, 0, finalRadius);
        tooltip.setVisibility(VISIBLE);
        anim.setDuration(500).start();

        tooltip.setTranslationZ(-tooltip.getElevation());
        tooltip.animate().translationZ(0).setDuration(500).start();
    }

    public void hideTooltip() {
        tooltip.setVisibility(INVISIBLE);
    }
}
