package com.valotec.neurocomm_mobile_app.app.home.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.valotec.neurocomm_mobile_app.app.home.ble_pairing.BleConnectionInit;


import static com.valotec.neurocomm_mobile_app.app.home.ble_pairing.BleConnectionDialog.ACTION_CONNECT_BLE_DEVICE;
import static com.valotec.neurocomm_mobile_app.app.home.ble_pairing.BleConnectionDialog.ACTION_DISCOVER_NEAR_WYSS_BLE_IN_DIALOG;
import static com.valotec.neurocomm_mobile_app.app.home.ble_pairing.BleConnectionDialog.KEY_BLE_DEVICE_ADDRESS;
import static com.valotec.neurocomm_mobile_app.app.home.ble_pairing.BleConnectionDialog.KEY_BLE_DEVICE_NAME;


public class BroadCastReceiver extends BroadcastReceiver {

    private BleConnectionInit mBleConnectionInit;


    public static BroadCastReceiver initBroadcastReceiver(Context context, BleConnectionInit bleInit) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DISCOVER_NEAR_WYSS_BLE_IN_DIALOG);
        filter.addAction(ACTION_CONNECT_BLE_DEVICE);
        BroadCastReceiver broadCast = new BroadCastReceiver(bleInit);
        context.registerReceiver(broadCast, filter);

        return broadCast;
    }

    public static void unRegisterBroadCastReceiver(Context context, BroadCastReceiver broadCastReceiver) {
        context.unregisterReceiver(broadCastReceiver);
    }

    public BroadCastReceiver(BleConnectionInit bleConnectionInit) {
        mBleConnectionInit = bleConnectionInit;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("IntentReceived", " onIntent Receiving is = "+intent.getAction());

        switch (intent.getAction()) {
            case ACTION_DISCOVER_NEAR_WYSS_BLE_IN_DIALOG:
                mBleConnectionInit.popNewBleDialog();
                break;

            case ACTION_CONNECT_BLE_DEVICE:
                mBleConnectionInit.initBleConnection(
                        intent.getExtras().getString(KEY_BLE_DEVICE_ADDRESS),
                        intent.getExtras().getString(KEY_BLE_DEVICE_NAME));
                break;
        }
    }
}
