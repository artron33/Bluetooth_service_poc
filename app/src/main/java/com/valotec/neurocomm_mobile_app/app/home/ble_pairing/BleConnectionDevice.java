package com.valotec.neurocomm_mobile_app.app.home.ble_pairing;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanResult;
import android.support.annotation.NonNull;

public class BleConnectionDevice implements Comparable<BleConnectionDevice> {
    private BluetoothDevice mDevice;
    private long mFoundAtTime;

    public BleConnectionDevice(ScanResult result, long time) {
        mDevice = result.getDevice();
        mFoundAtTime = time;
    }

    @Override
    public String toString() {
        return (mDevice.getName() == null ? "unknown": mDevice.getName()) + "   "+mDevice.getAddress();
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }

    public long getFoundAtTime() {
        return mFoundAtTime;
    }

    @Override
    public int compareTo(@NonNull BleConnectionDevice o) {
        return mDevice.getAddress().compareTo(o.getDevice().getAddress());
    }


    @Override
    public int hashCode() {
        return mDevice.getAddress().hashCode();
    }

}
