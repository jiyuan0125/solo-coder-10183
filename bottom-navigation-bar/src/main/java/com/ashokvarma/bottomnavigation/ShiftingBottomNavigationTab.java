package com.ashokvarma.bottomnavigation;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see BottomNavigationTab
 * @since 19 Mar 2016
 */
class ShiftingBottomNavigationTab extends BottomNavigationTab {

    private Animation mResizeAnimation;

    public ShiftingBottomNavigationTab(Context context) {
        super(context);
    }

    public ShiftingBottomNavigationTab(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShiftingBottomNavigationTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ShiftingBottomNavigationTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    void init() {
        paddingTopActive = (int) getResources().getDimension(R.dimen.shifting_height_top_padding_active);
        paddingTopInActive = (int) getResources().getDimension(R.dimen.shifting_height_top_padding_inactive);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.shifting_bottom_navigation_item, this, true);
        containerView = view.findViewById(R.id.shifting_bottom_navigation_container);
        labelView = view.findViewById(R.id.shifting_bottom_navigation_title);
        iconView = view.findViewById(R.id.shifting_bottom_navigation_icon);
        iconContainerView = view.findViewById(R.id.shifting_bottom_navigation_icon_container);
        badgeView = view.findViewById(R.id.shifting_bottom_navigation_badge);

        super.init();
    }

    @Override
    public void select(boolean setActiveColor, int animationDuration) {
        super.select(setActiveColor, animationDuration);

        if (mResizeAnimation != null) {
            mResizeAnimation.cancel();
        }
        mResizeAnimation = new ResizeWidthAnimation(this, mActiveWidth);
        mResizeAnimation.setDuration(animationDuration);
        this.startAnimation(mResizeAnimation);

        labelView.animate().cancel();
        labelView.animate().scaleY(1).scaleX(1).setDuration(animationDuration).start();
    }

    @Override
    public void unSelect(boolean setActiveColor, int animationDuration) {
        super.unSelect(setActiveColor, animationDuration);

        if (mResizeAnimation != null) {
            mResizeAnimation.cancel();
        }
        mResizeAnimation = new ResizeWidthAnimation(this, mInActiveWidth);
        mResizeAnimation.setDuration(animationDuration);
        this.startAnimation(mResizeAnimation);

        labelView.animate().cancel();
        labelView.animate().scaleY(0).scaleX(0).setDuration(0).start();
    }

    @Override
    public void cancelAnimations() {
        super.cancelAnimations();
        if (mResizeAnimation != null) {
            mResizeAnimation.cancel();
            mResizeAnimation = null;
        }
        if (labelView != null) {
            labelView.animate().cancel();
        }
    }

    @Override
    protected void setNoTitleIconContainerParams(FrameLayout.LayoutParams layoutParams) {
        layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.shifting_no_title_icon_container_height);
        layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.shifting_no_title_icon_container_width);
    }

    @Override
    protected void setNoTitleIconParams(LayoutParams layoutParams) {
        layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.shifting_no_title_icon_height);
        layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.shifting_no_title_icon_width);
    }

    private class ResizeWidthAnimation extends Animation {
        private int mWidth;
        private int mStartWidth;
        private View mView;

        ResizeWidthAnimation(View view, int width) {
            mView = view;
            mWidth = width;
            mStartWidth = view.getWidth();
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            mView.getLayoutParams().width = mStartWidth + (int) ((mWidth - mStartWidth) * interpolatedTime);
            mView.requestLayout();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

}
