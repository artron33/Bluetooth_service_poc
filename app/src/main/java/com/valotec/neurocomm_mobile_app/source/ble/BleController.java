package com.valotec.neurocomm_mobile_app.source.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;


import com.valotec.neurocomm_mobile_app.source.ble.generic.BleConnectionStatusManager;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleGattCallBack;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleHandler;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleOperationExec;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BleCharacteristic;


import java.util.HashMap;
import java.util.List;

import static android.bluetooth.BluetoothDevice.TRANSPORT_LE;

public class BleController {
    private final static String TAG = BleController.class.getSimpleName();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt mBluetoothGatt;
    private BleGattCallBack mGattCallback;
    private BleOperationExec mOperation;
    private HashMap<String, BleCharacteristic> mCharacts;
    private BleConnectionStatusManager mManager = new BleConnectionStatusManager(this);

    private String mBluetoothDeviceAddress;

    public BleController() {
        mOperation = new BleOperationExec();
        mCharacts = new HashMap<>();
//        if (Looper.myLooper() == null)
//        {
//            Looper.prepare();
//        }
        mGattCallback = new BleGattCallBack(new BleHandler(mCharacts, mManager));
        initialize();
    }

    /**
     * Initializes a reference to the local Bluetooth adapter.
     *
     * @return Return true if the initialization is successful.
     */
    private boolean initialize() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain the BluetoothAdapter.");
            return false;
        }

        return true;
    }

    /**
     * Connects to the GATT server hosted on the Bluetooth LE device.
     *
     * @param address The device address of the destination device.
     *
     * @return Return true if the connection is initiated successfully. The connection result
     *         is reported asynchronously through the
     *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     *         callback.
     */
    public boolean connect(Context context, final String address) {
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        if (mBluetoothAdapter == null || address == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null && manager.getConnectedDevices(BluetoothProfile.GATT).size()>0) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
//            Log.d(TAG, "mBluetoothGatt.getConnectionState()" +mBluetoothGatt.getConnectedDevices());
            return mBluetoothGatt.connect();
        }

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }

        //means we have a previous connection in mind, that is not closed yet
        if (mBluetoothGatt != null) {
            close();
        }

        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(context, false, mGattCallback, TRANSPORT_LE);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        Log.w("yallah", "BleController disconnect disconnect disconnect disconnect");
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }

        mBluetoothGatt.disconnect();
        if (mOperation != null) {
            mOperation.clearOperations();
        }
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    public void addOperation(List<BleCharacteristic> mOperations) {
        Log.e("AddOperation", "state... addOperation  operation.size() =::  "+mOperations.size());
        mOperation.addOperation(mOperations);
    }

    public void execPendingOperations() {
        mOperation.exec(mBluetoothGatt);
    }

    public void addCharacteristicToListenning(BleCharacteristic characteristic){
        mCharacts.put(characteristic.getCharacteristic(), characteristic);
    }

    public BleConnectionStatusManager getStatusManager(){
        return mManager;
    }


    public void testRSSI() {
        mBluetoothGatt.readRemoteRssi();
    }

}
