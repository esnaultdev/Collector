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
    }

    public void displayTooltip(@NonNull View view, @NonNull String text) {

        int tooltipWidth = tooltip.getMeasuredWidth();
        int tooltipHeight = tooltip.getMeasuredHeight();

        //Default is top left corner
        boolean left = true;
        boolean top = true;
        int leftPos = view.getLeft();
        int topPos = view.getTop();
        int revealCircleX = 0;
        int revealCircleY = 0;

        if (view.getLeft() + tooltipWidth >= getRight() - getPaddingRight()) {
            left = false;
            leftPos = view.getRight() - tooltipWidth;
            revealCircleX = tooltipWidth;
        }
        if (view.getTop() + tooltipHeight >= getBottom() - getPaddingBottom()) {
            top = false;
            topPos = view.getBottom() - tooltipHeight;
            revealCircleY = tooltipHeight;
        }

        if (left && top) {
            tooltip.setDirection(ElementTooltip.Direction.TOP_LEFT);
        } else if (left){
            tooltip.setDirection(ElementTooltip.Direction.BOTTOM_LEFT);
        } else if (top) {
            tooltip.setDirection(ElementTooltip.Direction.TOP_RIGHT);
        } else {
            tooltip.setDirection(ElementTooltip.Direction.BOTTOM_RIGHT);
        }

        tooltip.setLeft(leftPos);
        tooltip.setTop(topPos);
        tooltip.setRight(leftPos + tooltipWidth);
        tooltip.setBottom(topPos + tooltipHeight);

        float finalRadius = (float) Math.hypot(tooltip.getWidth(), tooltip.getHeight());
        Animator anim = ViewAnimationUtils.createCircularReveal(
                tooltip, revealCircleX, revealCircleY, 0, finalRadius);
        tooltip.setVisibility(VISIBLE);
        anim.setDuration(500).start();

        tooltip.setTranslationZ(-tooltip.getElevation());
        tooltip.animate().translationZ(0).setDuration(500).start();
    }

    public void hideTooltip() {
        tooltip.setVisibility(INVISIBLE);
    }
}
