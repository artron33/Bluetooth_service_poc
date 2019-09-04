package com.valotec.neurocomm_mobile_app.app;


import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.valotec.neurocomm_mobile_app.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 */
public class DeviceScanFragment extends Fragment {
    private static final String TAG = DeviceScanFragment.class.getSimpleName();

    private BleTestActivity mActivity;

    private BluetoothLeScanner mScanner;
    private LeDeviceListAdapter mLeDeviceListAdapter;

    private static final int SCAN_PERIOD = 2500;
    private Handler mHandler = new Handler();

    private ListView mListView;
    private String mDeviceAddress;

    TextView mTitleScanResult;

    public DeviceScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static DeviceScanFragment newInstance() {
        return new DeviceScanFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_scan, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity = (BleTestActivity)getActivity();

        // Get the local Bluetooth adapter
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get BLE scanner
        mScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mListView = view.findViewById(R.id.device_scan_result);
        mListView.setOnItemClickListener((parent, view1, position, id) -> selectDevice(position));

        mTitleScanResult = view.findViewById(R.id.scan_state);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        mListView.setAdapter(mLeDeviceListAdapter);

        mDeviceAddress = "";
        scanLeDevice(true);
    }

    @Override
    public void onPause() {
        super.onPause();

        scanLeDevice(false);
        mLeDeviceListAdapter.clear();
    }

    private void scanLeDevice(final boolean enable) {
        Log.d(TAG, "Scanning " + enable);

        if (enable) {
            mHandler.postDelayed(() -> {
                // If only 1 device detected
                if (mLeDeviceListAdapter.getCount() == 1) {
                    // autoselect it
                    selectDevice(0);
                }
            }, SCAN_PERIOD);

            ScanFilter wyssFilter = new ScanFilter.Builder()
                .build();

            ScanSettings settings = new ScanSettings.Builder()
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setReportDelay(0)
                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                .build();

            mScanner.startScan(Arrays.asList(wyssFilter), settings, mLeScanCallback);
        } else {
            mScanner.stopScan(mLeScanCallback);
        }
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        LeDeviceListAdapter() {
            super();

            mLeDevices = new ArrayList<>();
            mInflator = getActivity().getLayoutInflater();
        }

        void addDevice(BluetoothDevice device) {
            if (device.getName() != null)// && device.getName().startsWith("ID"))
                if (!mLeDevices.contains(device)) {
                    Log.e("yallah", "log is "+device);
                    Log.e("yallah", "log is "+device.getAddress());
                    Log.e("yallah", "log is "+device.getName());
                    Log.e("yallah", "log==========="+mLeDevices.contains(device)+"================");
                    Log.e("yallah", "log===========================");
                    mLeDevices.add(device);
                }
        }

        BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = view.findViewById(R.id.device_address);
                viewHolder.deviceName = view.findViewById(R.id.device_name_edit);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0) {
                viewHolder.deviceName.setText(deviceName);
            }
            else {
                viewHolder.deviceName.setText(R.string.unknown_device);
            }
            viewHolder.deviceAddress.setText(device.getAddress());

            return view;
        }
    }

    // Device scan callback
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            getActivity().runOnUiThread(() -> {
                mLeDeviceListAdapter.addDevice(result.getDevice());
                mLeDeviceListAdapter.notifyDataSetChanged();
            });
        }
    };

    private void selectDevice(int position) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) {
            return;
        }

        scanLeDevice(false);
        mDeviceAddress = device.getAddress();
//        mActivity.onBleSelected(mDeviceAddress);
    }

    static private class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }
}
