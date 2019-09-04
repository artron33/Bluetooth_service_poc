package com.valotec.neurocomm_mobile_app.source.ble.generic;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.os.Handler;

import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BleCharacteristic;

import java.util.HashMap;
import java.util.List;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;
import static android.os.Process.myUid;

/**
 * Link BLe to UI
 */
//todo:::: Split this Class in two. First only about Connection State, other about data from the BLE
public class BleHandler extends Handler {
    private final static String TAG = BleHandler.class.getSimpleName();

    private HashMap<String, BleCharacteristic> mCharacts;
    private BleConnectionStatus mLifeCycleCall;

    public BleHandler(HashMap<String, BleCharacteristic> characts, BleConnectionStatus bleLifeCycleCb) {
        super(getLocalLooper());
//ui
        Log.wtf("yallah", "process UID = "+myUid());
        Log.e("yallah", "process UID = "+myUid());
        Log.e("yallah", "process UID = "+myUid());
        Log.wtf("yallah", "process UID = "+myUid());
//        Log.e("yallah", "process UID = "+Process.BLUETOOTH_UID);
//        Log.e("yallah", "process UID = "+ Process.ROOT_UID);

//        Looper.prepare();
        mCharacts = characts;
        mLifeCycleCall = bleLifeCycleCb;
//        Looper.loop();

    }

    private static Looper getLocalLooper() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                THREAD_PRIORITY_BACKGROUND);
        thread.start();
        return thread.getLooper();
    }

    private void playCallBack(String charactUuid, byte[] data) {
        charactUuid = charactUuid.toUpperCase();

        if (mCharacts.get(charactUuid) != null) {
            mCharacts.get(charactUuid).getmListener().onCharacteristicRead(data);
        } else {
            Log.e(TAG, "Error: can't find proper characteristicCallBack from characteristic.UUID = "+charactUuid + "  mCharacts.size = "+ mCharacts);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        String charactUiid = null;
        byte[] readBuf = null;
        BluetoothGattCharacteristic characteristic;

        if (msg.what != BleConnectionStatus.MESSAGE_STATE_CHANGE) {
            //todo:::: TEST THIS CHANGE IF IT STILL WORK
            if (msg.obj instanceof BluetoothGattCharacteristic) {
                characteristic = ((BluetoothGattCharacteristic) msg.obj);
                readBuf = ((BluetoothGattCharacteristic) msg.obj).getValue();
            } else {
                characteristic = ((BluetoothGattDescriptor) msg.obj).getCharacteristic();
                readBuf = ((BluetoothGattDescriptor) msg.obj).getValue();
            }
            charactUiid = characteristic.getUuid().toString();
        }

        switch (msg.what) {
            case BleConnectionStatus.MESSAGE_STATE_CHANGE:
                Log.e(TAG, "MESSAGE_STATE_CHANGE = "+msg.arg1);
                switch ((int)msg.obj) {
                    case BluetoothProfile.STATE_CONNECTED:
                        Log.e(TAG, "STATE_CONNECTED");
                        mLifeCycleCall.onBleConnected();
                        break;
                    case BluetoothProfile.STATE_CONNECTING:
                        Log.e(TAG, "STATE_CONNECTING");
                        mLifeCycleCall.onBleConnecting();
                        break;
                    case BluetoothProfile.STATE_DISCONNECTED:
                        Log.e(TAG, "STATE_DISCONNECTED");
                        mLifeCycleCall.onBleDisconnected();
                        break;
                }
                break;

            case BleConnectionStatus.MESSAGE_READ:
                Log.e(TAG, "MESSAGE_READ");
                playCallBack(charactUiid, readBuf);
                break;

            case BleConnectionStatus.MESSAGE_WRITE:
                Log.e(TAG, "MESSAGE_WRITE");
                playCallBack(charactUiid, readBuf);
                break;

            case BleConnectionStatus.MESSAGE_NOTIFY:
                Log.e(TAG, "MESSAGE_NOTIFY");
                playCallBack(charactUiid, readBuf);
                break;

            case BleConnectionStatus.MESSAGE_CHARACTERISTIC_CHANGE:
                Log.e(TAG, "MESSAGE_CHARACTERISTIC_CHANGE");
                playCallBack(charactUiid, readBuf);
                break;

            case BleConnectionStatus.MESSAGE_DESCRIPTOR_WRITE:
                Log.e(TAG, "IMPLANT   ;;   MESSAGE_DESCRIPTOR_WRITE");
                playCallBack(charactUiid, readBuf);
                mLifeCycleCall.onDescriptorWriteFinish(charactUiid);
                break;
        }
    }
}
