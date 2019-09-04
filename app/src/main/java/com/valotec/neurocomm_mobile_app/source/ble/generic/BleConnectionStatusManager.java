package com.valotec.neurocomm_mobile_app.source.ble.generic;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.util.Log;

import com.valotec.neurocomm_mobile_app.source.ble.BleController;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BleCharacteristic;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.execution.BleExecType;

import java.util.ArrayList;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class BleConnectionStatusManager implements BleConnectionStatus, LifecycleObserver {
    private static final String TAG = BleConnectionStatusManager.class.getCanonicalName();

    private BleController mBleController;
    private List<BleCharacteristic> mCharacts = new ArrayList<BleCharacteristic>() {{


    }};
    public boolean isConnected = false;
    private Consumer<Boolean> consumeChangeConnection;

    public BleConnectionStatusManager(BleController bleController) {
        mBleController = bleController;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void connectListener() {
        Log.w("yallah", "BleControllerStatusManager connectListener");
        mBleController.disconnect();
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void disconnectListener() {
        Log.w("yallah", "BleControllerStatusManager disconnectListener");
        mBleController.disconnect();
    }

    @Override
    public void onBleConnecting() {
        Log.e(TAG, " connecting");
    }

    @Override
    public void onBleConnected() {
        Log.e(TAG, "ADD: Repository IMPLANT bleCharacteristic::::  connected");
        isConnected = true;
        watchCharacteristic(mBleController, mCharacts);
    }

    private static void watchCharacteristic(BleController controller, List<BleCharacteristic> mCharacts) {
        Log.e(TAG, "     Repository IMPLANT Add bleCharacteristic:::: "+ mCharacts);
        if (mCharacts.size() == 0) {
            return;
        }
        for (BleCharacteristic characteristic : mCharacts) {
            controller.addCharacteristicToListenning(characteristic);
        }

        controller.addOperation(mCharacts);
        controller.execPendingOperations();
    }

    @Override
    public void onBleDisconnected() {
        Log.e(TAG, " disconnect E D");
        isConnected = false;
        if (consumeChangeConnection !=null) consumeChangeConnection.accept(true);
        else                                Log.e(TAG, " disconnect E D: not consumer to update UI");
    }

    @Override
    public void onDescriptorWriteFinish(String charactUiid) {
        for (BleCharacteristic charact : mCharacts) {
            if (charact.getName().toUpperCase().equals(charactUiid.toUpperCase())) {
                Log.e(TAG, "     Repository IMPLANT FOUND delete ::::"+ charact.getName());

                mCharacts.remove(charact);
                break;
            }
        }
        if (mCharacts.size()>0) {
            mBleController.execPendingOperations();
        }
    }

    public void addRepository(BleCharacteristic bleCharacteristic) {
        mCharacts.add(bleCharacteristic);
    }

    public void setHandleDisconnectUi(Consumer<Boolean> setLogOutView) {
        consumeChangeConnection = setLogOutView;
    }

}
