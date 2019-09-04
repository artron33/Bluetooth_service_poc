package com.valotec.neurocomm_mobile_app.app.home.ble_pairing.subview;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.valotec.neurocomm_mobile_app.R;
import com.valotec.neurocomm_mobile_app.generic.ButterKnifeConstraintView;

import butterknife.BindView;

public class BleStepView extends ButterKnifeConstraintView {

    private static final String TAG = BleStepView.class.getCanonicalName();
    private static final int START_INDEX = 1;
    private static final int END_INDEX = 3;

    private static final float CURRENT_STEP = 1.0f;
    private static final float NO_CURRENT_STEP = 0.4f;

    private int mCurrentStep = -START_INDEX;

    @BindView(R.id.ble_step_one)            TextView mStepOne;
    @BindView(R.id.ble_step_left)           ImageView mLineLeft;
    @BindView(R.id.ble_step_two)            TextView mStepTwo;
    @BindView(R.id.ble_step_right)          ImageView mLineRight;
    @BindView(R.id.ble_step_three)          TextView mStepThree;

    public BleStepView(Context context) {
        super(context);
    }

    public BleStepView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BleStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getView() {
        return R.layout.view_ble_step;
    }

    public boolean switchStep(@IntRange(from = START_INDEX, to = END_INDEX) int step)  {
        if (mStepOne == null) {
            Log.e(TAG, "step is ALREADY at this step");
            return false;
        }

        if (mCurrentStep==step) {
            Log.e(TAG, "step are ALREADY at this step");
            return false;
        }else {
            Log.e(TAG, "step at now is step ========="+step);
        }
        mCurrentStep = step;

        if (step < START_INDEX)     throw new IllegalArgumentException("Step start a " + START_INDEX);
        else if (step > END_INDEX)  throw new IllegalArgumentException("Step end  at " + END_INDEX);

        switch (step) {
            case 1:
                mStepOne  .animate().alpha(CURRENT_STEP)   .start();
                mLineLeft .animate().alpha(NO_CURRENT_STEP).start();
                mStepTwo  .animate().alpha(NO_CURRENT_STEP).start();
                mLineRight.animate().alpha(NO_CURRENT_STEP).start();
                mStepThree.animate().alpha(NO_CURRENT_STEP).start();
                break;

            case 2:
                mStepOne  .animate().alpha(NO_CURRENT_STEP).start();
                mLineLeft .animate().alpha(NO_CURRENT_STEP).start();
                mStepTwo  .animate().alpha(CURRENT_STEP)   .start();
                mLineRight.animate().alpha(NO_CURRENT_STEP).start();
                mStepThree.animate().alpha(NO_CURRENT_STEP).start();
                break;

            case 3:
                mStepOne  .animate().alpha(NO_CURRENT_STEP).start();
                mLineLeft .animate().alpha(NO_CURRENT_STEP).start();
                mStepTwo  .animate().alpha(NO_CURRENT_STEP).start();
                mLineRight.animate().alpha(NO_CURRENT_STEP).start();
                mStepThree.animate().alpha(CURRENT_STEP)   .start();
                break;
        }

        return true;
    }

}
