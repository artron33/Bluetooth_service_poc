package com.valotec.neurocomm_mobile_app.app.home.ble_pairing;

import android.animation.AnimatorInflater;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.valotec.neurocomm_mobile_app.R;
import com.valotec.neurocomm_mobile_app.app.home.ble_pairing.subview.BleStepView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import butterknife.Unbinder;

import static android.view.View.GONE;


//todo:         Add Custom: ViewModel<List<BleConnectionDevice>> extends ViewModel to properly handle
//todo (next)   When the data is needed (to handle when observing, when request new Data with AndroidLifeCycle
public class BleConnectionDialog extends DialogFragment {
    public static final String TAG = BleConnectionDialog.class.getCanonicalName();

    public static final String ACTION_DISCOVER_NEAR_WYSS_BLE_IN_DIALOG = "ACTION_DISCOVER_NEAR_WYSS_BLE_IN_DIALOG";
    
    public static final String ACTION_CONNECT_BLE_DEVICE = "ACTION_CONNECT_BLE_DEVICE";

    public static final String KEY_BLE_DEVICE_ADDRESS       = "BLE_DEVICE_ADDRESS";
    public static final String KEY_BLE_DEVICE_NAME          = "KEY_BLE_DEVICE_NAME";

    private Unbinder unbinder;
    @BindView(R.id.dialog_ble_new_connection_constraint)    ConstraintLayout mConstraintLayout;
    @BindView(R.id.dialog_ble_new_connection_state)         BleStepView mStepView;
    @BindView(R.id.dialog_ble_new_image)                    ImageView mIcon;
    @BindView(R.id.dialog_ble_new_image_reloading)          ImageView mReloadIcon;
    @BindView(R.id.dialog_ble_new_title)                    TextView mTitle;
    @BindView(R.id.listview)                                ListView mListView;

    private BleViewModel mBleViewModel;
    private ArrayAdapter<BleConnectionDevice> mItemsAdapter;

    public static BleConnectionDialog newInstance() {
        return new BleConnectionDialog();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_ble_connection, container);
        // Set transparent background and no title
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        mBleViewModel = ViewModelProviders.of(getActivity()).get(BleViewModel.class);

        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        unbinder = ButterKnife.bind(this, getDialog());
        // Initializes list view adapter.
        mBleViewModel.scanBle(true);
        mBleViewModel.getUsers().observe(this, mObserver);
    }

    @Override
    public void onPause() {
        mBleViewModel.scanBle(false);
        mListView.setVisibility(GONE);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @OnItemClick(R.id.listview)
    protected void onListItemClic(int position) {
        selectDevice(position);
    }

    @OnClick(R.id.dialog_ble_new_image_reloading)
    protected void onReloadClick(){
        Toast.makeText(getContext(), "Launch new BLE scan", Toast.LENGTH_SHORT).show();
        mBleViewModel.scanBle(true);
    }

    void selectDevice(int position) {
        final BleConnectionDevice deviceAdress = mItemsAdapter.getItem(position);
        if (deviceAdress == null) return;

        mBleViewModel.scanBleEnd();
        if (isAdded() && !isRemoving() && mStepView.switchStep(3)) {
            mBleViewModel.getUsers().removeObserver(mObserver);
            mListView.setVisibility(GONE);
            mIcon.setRotation(0);
            mIcon.animate().alpha(1.f).setStartDelay(35).start();
            mIcon.setImageResource(R.drawable.ble_loading_success_icon);
            mIcon.setStateListAnimator(null);
            mTitle.setText("Peering success !");

            (new Handler()).postDelayed(() -> {
                if (BleConnectionDialog.this.isAdded()) {
                    BleConnectionDialog.this.dismiss();
                }
            }, 4500);
        }


        mReloadIcon.setVisibility(GONE);
        //todo end this intent..
        Intent intent = new Intent(ACTION_CONNECT_BLE_DEVICE);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_BLE_DEVICE_ADDRESS, deviceAdress.getDevice().getAddress());
        bundle.putString(KEY_BLE_DEVICE_NAME, deviceAdress.getDevice().getName());
        intent.putExtras(bundle);
        getContext().sendBroadcast(intent);
    }

    private Observer<List<BleConnectionDevice>> mObserver = new Observer<List<BleConnectionDevice>>() {
        @Override
        public void onChanged(@Nullable List<BleConnectionDevice> strings) {
            if (mItemsAdapter == null || mListView.getAdapter() == null) {
                mItemsAdapter = new ArrayAdapter<>(getContext(), R.layout.simple_text, mBleViewModel.getAddress());
                mListView.setAdapter(mItemsAdapter);
            }

            if (strings.size() == 0) {
                mReloadIcon.setStateListAnimator(null);
                return;
            }

            if (mReloadIcon.getStateListAnimator() == null) {
                mReloadIcon.setStateListAnimator(AnimatorInflater.loadStateListAnimator(getContext(), R.animator.animator));
            }

            mItemsAdapter.notifyDataSetChanged();
            if (mStepView.switchStep(2)) {
                mTitle.setText("Proximity users detected :");
                mReloadIcon.setVisibility(View.VISIBLE);
                mListView.setVisibility(View.VISIBLE);
                mReloadIcon.setVisibility(View.VISIBLE);
            }
        }
    };

}
