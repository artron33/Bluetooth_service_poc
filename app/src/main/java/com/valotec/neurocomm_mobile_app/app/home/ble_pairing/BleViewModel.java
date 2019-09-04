package com.valotec.neurocomm_mobile_app.app.home.ble_pairing;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class BleViewModel extends ViewModel {
    private final long DELAY_SCANNER_BLE = 4000;

    private MutableLiveData<List<BleConnectionDevice>> mUsers;
    private boolean mIsScanning = false;
    private List<BleConnectionDevice> mDeviceName = new ArrayList<>();
    private Set<BleConnectionDevice> mDeviceAddress = new TreeSet<>();
    private BluetoothLeScanner mScanner;

    private long mTimeStart = 0L;
    private long mLastResetBleAddress = 0L;

    public BleViewModel() {
        mScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    }

    private final ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            long now  = System.currentTimeMillis();

            if (result.getDevice().getName() == null
                /*|| !result.getDevice().getName().startsWith("ID")*/
            ) {
//                Log.e("DEBUG", "DEVICE   BLE    IGNORED    NAME    IS    = " + result.getDevice().getName());
                return;
            }
                Log.e("DEBUG", "DEVICE   BLE    FOUND    NAME    IS    = " + result.getDevice().getName());

            if (mIsScanning && (now - mLastResetBleAddress) > (DELAY_SCANNER_BLE)) {
                mLastResetBleAddress = now;
                mDeviceAddress.clear();
                mDeviceName.clear();
                mScanner.flushPendingScanResults(mLeScanCallback);
            }

            BleConnectionDevice deviceFound = new BleConnectionDevice(
                    result,
                    System.currentTimeMillis());

            if (mDeviceAddress.contains(deviceFound)) {
                return;
            }
            mDeviceName.add(deviceFound);
            mDeviceAddress.add(deviceFound);
            mUsers.postValue(mDeviceName);
        }
    };

    public List<BleConnectionDevice> getAddress() {
        return mDeviceName;
    }

    public LiveData<List<BleConnectionDevice>> getUsers() {
        if (mUsers == null) {
            mUsers = new MutableLiveData<>();
            loadUsers();
        }
        return mUsers;
    }

    private void loadUsers() {
        // Do an asynchronous operation to fetch mUsers.
        mUsers.setValue(mDeviceName);
    }

    //WORKAROUND : https://stackoverflow.com/questions/29731176/ble-scanning-callback-only-get-called-several-times-then-stopped#29731725
    //because scanner drain too much Battery, and all different Android Device that behave differently, we should have a delay
    //between 2 scans. Meaning :
    //                              StopScan(); delay(1100MS); StartScan();
    void scanBle(final boolean enable) {
        synchronized (mLeScanCallback) {
            Log.d("BLE_State", "Scanning " + enable);
            long now  = System.currentTimeMillis();

            if (enable && now - mTimeStart > DELAY_SCANNER_BLE) {
                mIsScanning = true;
                scanLeDevice(true, mLeScanCallback, mScanner
                );
                mTimeStart = now;
            } else if (!enable && (now - mTimeStart) > DELAY_SCANNER_BLE * 0.75f && mIsScanning) {
                mIsScanning = false;
                scanLeDevice(false, mLeScanCallback, mScanner);
            } else {
                Log.d("BLE_State", "Scanning     but nothing start or stop");
            }
        }
    }

    void scanBleEnd() {
        if (mIsScanning) {
            synchronized (mLeScanCallback) {
                mIsScanning = false;
                scanLeDevice(false, mLeScanCallback, mScanner);
            }
        }
    }

    private static void scanLeDevice(final boolean enable, final ScanCallback leScanCallback,
                                     BluetoothLeScanner mScanner) {
        ScanFilter pma_service_filter = new ScanFilter.Builder()
                .build();

        ScanSettings settings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
                .setReportDelay(0)
                .build();

        if (enable) {
//            mScanner.startScan(Arrays.asList(pma_service_filter), settings, leScanCallback);
            Log.d("BLE_SCAN", " ble   S T A R T  scan now");
        } else {

            Log.d("BLE_SCAN", " ble   S T O P   scan now");
            mScanner.stopScan(leScanCallback);
        }

    }

}
