package com.valotec.neurocomm_mobile_app.source.ble.generic;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import java.util.UUID;

import static com.valotec.neurocomm_mobile_app.BytesHelper.ByteHelper.bytesToHex;

// Implements callback methods for GATT events that the app cares about.  For example,
// connection change and services discovered.
public class BleGattCallBack extends BluetoothGattCallback {
    private final static String TAG = BleGattCallBack.class.getSimpleName();

    private BleHandler mBleHandler;

    public BleGattCallBack(BleHandler bleHandler) {
        mBleHandler = bleHandler;
    }

    private void handleResponse(int status, int messageType, Object characteristic, byte[] value, UUID uuid) {
        Log.e(TAG, "handleResponse::::  "+messageType+"::::  to uuid = "+uuid);
        Log.i("TEMPS_TAG", "::::  to uuid = "+uuid+"  ::   == "+ bytesToHex(value));

        if (status == BluetoothGatt.GATT_SUCCESS) {
            sendMsg(messageType, characteristic);

        } else {
            Log.e(TAG, "handleBleResponse from type:"+messageType+" . F A I L E D");
            mBleHandler.sendMessage(mBleHandler.obtainMessage(BleConnectionStatus.MESSAGE_FAILED));
        }
    }

    private void sendMsg(int messageStateChange, Object stateConnecting) {
        mBleHandler.sendMessage(mBleHandler.obtainMessage(messageStateChange, stateConnecting));
    }

    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
        Log.e("yallah", "yes gatt is = "+rssi);
        Log.e("yallah", "yes gatt is = "+rssi);
        Log.e("yallah", "rssi end=============");

    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.e("wazaa", "on Conn State Change.");
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            Log.i(TAG, "Connected to GATT server.");

            // Attempts to discover services after successful connection.
            boolean isDiscovering = gatt.discoverServices();
            Log.e(TAG, "Attempting to start service discovery:" + isDiscovering);
            sendMsg(BleConnectionStatus.MESSAGE_STATE_CHANGE, BluetoothProfile.STATE_CONNECTING);

        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // Clear pending operations
            Log.e(TAG, "Disconnected from GATT server.");
            sendMsg(BleConnectionStatus.MESSAGE_STATE_CHANGE, BluetoothProfile.STATE_DISCONNECTED);
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            Log.e(TAG, "IMPLANTXXXX Services discovered. status ="+status);
            Log.e(TAG, "SERVICE       :           STATE_CONNECTED");
            sendMsg(BleConnectionStatus.MESSAGE_STATE_CHANGE, BluetoothProfile.STATE_CONNECTED);

        } else {
            Log.w(TAG, "IMPLANTXXXX onServicesDiscovered received::::  NO_SUCCESS:::: code = " + status);
        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt,
                                     BluetoothGattCharacteristic characteristic,
                                     int status) {
            handleResponse(status, BleConnectionStatus.MESSAGE_READ, characteristic, characteristic.getValue(), characteristic.getUuid());
    }

    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt,
                                      BluetoothGattCharacteristic characteristic,
                                      int status) {
            handleResponse(status, BleConnectionStatus.MESSAGE_WRITE, characteristic, characteristic.getValue(), characteristic.getUuid());
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,
                                        BluetoothGattCharacteristic characteristic) {
        handleResponse(BluetoothGatt.GATT_SUCCESS, BleConnectionStatus.MESSAGE_CHARACTERISTIC_CHANGE, characteristic, characteristic.getValue(), characteristic.getUuid());
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt,
                                  BluetoothGattDescriptor descriptor,
                                  int status) {
        handleResponse(status, BleConnectionStatus.MESSAGE_DESCRIPTOR_WRITE, descriptor, descriptor.getValue(), descriptor.getCharacteristic().getUuid());
    }




}
