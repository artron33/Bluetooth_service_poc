package com.valotec.neurocomm_mobile_app.source.ble.generic.model;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;

import com.valotec.neurocomm_mobile_app.source.ble.generic.model.execution.BleExecType;

import java.util.UUID;

public class BleCharacteristic {
    private BleExecType mOperationType;

    /**
     * used to write someData (could be in separete child class)
     */
    private byte[] mData;

    private String mName;
    private String mCharacteristic;
    private String mService;

    private BLECallBack mListener;

    public BleCharacteristic(String service, String mName, String mCharacteristic, BleExecType executionType, BLECallBack mListener) {
        this.mName = mName;
        this.mCharacteristic = mCharacteristic;
        this.mListener = mListener;
        this.mOperationType = executionType;
        this.mService=service;
    }

    public BleCharacteristic(String service, String characteristic, BleExecType executionType, BLECallBack mListener) {
        this(service, characteristic, characteristic, executionType, mListener);
    }

    public BleCharacteristic(BleCharacteristic charr) {
        this.mName = charr.mName;
        this.mCharacteristic = charr.mCharacteristic;
        this.mListener = charr.mListener;
        this.mOperationType = charr.mOperationType;
        this.mService= charr.mService;
    }


    public boolean read(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt){

        return (characteristic != null) && bluetoothGatt.readCharacteristic(characteristic);
    }

    public boolean notification(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt){
        if (characteristic == null) {
            return false;
        }

        // Enable notification Android side
        bluetoothGatt.setCharacteristicNotification(characteristic, true);

        for (BluetoothGattDescriptor descriptor: characteristic.getDescriptors()){
            // Enable notification peripheral side
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            bluetoothGatt.writeDescriptor(descriptor);
        }

        //=========================================

        return true;
    }

    public boolean write(BluetoothGattCharacteristic characteristic, BluetoothGatt bluetoothGatt){
        return (characteristic != null) &&
                characteristic.setValue(mData) &&
                bluetoothGatt.writeCharacteristic(characteristic);
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getCharacteristic() {
        return mCharacteristic;
    }

    public BLECallBack getmListener() {
        return mListener;
    }


    public BleExecType getOperationType() {
        return mOperationType;
    }

    public void setOperationType(BleExecType mOperationType) {
        this.mOperationType = mOperationType;
    }

    public String getService() {
        return mService;
    }

    public void setService(String mService) {
        this.mService = mService;
    }

    public byte[] getData() {
        return mData;
    }

    public void setData(byte[] mData) {
        this.mData = mData;
    }


}
