package com.valotec.neurocomm_mobile_app.generic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class ButterKnifeConstraintView extends ConstraintLayout {
    private Unbinder mUnbinder;

    public ButterKnifeConstraintView(Context context) {
        this(context, null);
    }

    public ButterKnifeConstraintView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ButterKnifeConstraintView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        inflate(context, getView(), this);
        this.setBackgroundResource(android.R.color.transparent);
        mUnbinder = ButterKnife.bind(this);
    }

    public abstract int getView();

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // View is now detached, and about to be destroyed
         mUnbinder.unbind();
    }

}
