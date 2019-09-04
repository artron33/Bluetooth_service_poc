package com.valotec.neurocomm_mobile_app.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.valotec.neurocomm_mobile_app.R;


public class VersionNumberTextView extends android.support.v7.widget.AppCompatTextView{

    public VersionNumberTextView(Context context) {
        this(context, null);
    }

    public VersionNumberTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VersionNumberTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //12sp too small to be read (IE:-> it's number version)
        setTextSize(TypedValue.COMPLEX_UNIT_SP,12.0f);

        int padding = getResources().getDimensionPixelOffset(R.dimen.version_number_padding);
        setPadding( padding, 0, padding, 0);
        try {
            String versionName = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName;
            setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
