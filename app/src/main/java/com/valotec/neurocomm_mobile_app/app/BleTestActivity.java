package com.valotec.neurocomm_mobile_app.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.valotec.neurocomm_mobile_app.R;
import com.valotec.neurocomm_mobile_app.app.home.ble_pairing.BleConnectionInit;
import com.valotec.neurocomm_mobile_app.app.home.broadcast.BroadCastReceiver;
import poc_de_lifika.POJO.broadCastFake;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleConnectionStatus;
import com.valotec.neurocomm_mobile_app.source.ble.BleController;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleSingleton;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BleCharacteristic;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.execution.BleExecType;
import com.valotec.neurocomm_mobile_app.source.ble.WYSGattAttributes;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BLECallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import poc_de_lifika.MyNotificationListenerService;


public class BleTestActivity extends AppCompatActivity implements BleConnectionStatus, BleConnectionInit {
    private static final String TAG = BleTestActivity.class.getCanonicalName();

    ////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////
//                private List<BleCharacteristic> mCharactsOrigin = new ArrayList<BleCharacteristic>() {{
//                    add(new BleCharacteristic(WYSGattAttributes.SERVICE_WYSSSS, WYSGattAttributes.CHARACTERISTIC_IMPLANT, BleExecType.NOTIFICATION, new BLECallBack() {
//                        @Override
//                        public void onCharacteristicRead(byte[] o) {
//                            Log.e(TAG, "    Characteristic:::  IMPLANT:::  READ::: message is "+o);
//                        }
//                    }));
//                    add(new BleCharacteristic(WYSGattAttributes.SERVICE_WYSSSS, WYSGattAttributes.CHARACTERISTIC_CHARGER, BleExecType.NOTIFICATION, new BLECallBack() {
//                        @Override
//                        public void onCharacteristicRead(byte[] o) {
//                            Log.e(TAG, "    Characteristic:::  CHARGER:::  READ::: message is "+o);
//
//                        }
//                    }));
//                    add(new BleCharacteristic(WYSGattAttributes.SERVICE_WYSSSS, WYSGattAttributes.CHARACTERISTIC_WARNING, BleExecType.NOTIFICATION, new BLECallBack() {
//                        @Override
//                        public void onCharacteristicRead(byte[] o) {
//                            Log.e(TAG, "    Characteristic:::  WARNING::: NOTIFY ::: message is "+o);
//                        }
//                    }));
//                }};
    ////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////
    ////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////
    private List<BleCharacteristic> mCharacts = new ArrayList<BleCharacteristic>() {{
//        add(new BleCharacteristic(WYSGattAttributes.SERVICE_UUID_ABCD, WYSGattAttributes.READ_CHARACTERISTIC_UUID, BleExecType.READ, new BLECallBack() {
//            @Override
//            public void onCharacteristicRead(byte[] o) {
//                Log.e(TAG, "    Characteristic:::  IMPLANT:::  READ::: message is "+o);
//            }
//        }));
        add(new BleCharacteristic(WYSGattAttributes.SERVICE_UUID_ABCD, WYSGattAttributes.WRITE_CHARACTERISTIC_UUID, BleExecType.WRITE, new BLECallBack() {
            @Override
            public void onCharacteristicRead(byte[] o) {
                Log.e(TAG, "    Characteristic:::  CHARGER:::  READ::: message is "+o);

            }
        }));
        add(new BleCharacteristic(WYSGattAttributes.SERVICE_UUID_ABCD, WYSGattAttributes.READ_CHARACTERISTIC_UUID, BleExecType.NOTIFICATION, new BLECallBack() {
            @Override
            public void onCharacteristicRead(byte[] o) {
                Log.e(TAG, "    Characteristic:::  WARNING::: NOTIFY ::: message is "+o);
            }
        }));
        add(new BleCharacteristic(WYSGattAttributes.SERVICE_UUID_ABCD, WYSGattAttributes.READ_CHARACTERISTIC_UUID, BleExecType.NOTIFICATION, new BLECallBack() {
            @Override
            public void onCharacteristicRead(byte[] o) {
                Log.e(TAG, "    Characteristic:::  WARNING::: NOTIFY ::: message is "+o);
            }
        }));
    }};
    ////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////
    ////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////
    ////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////
    ////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////////////////////////////// DEBUG //////////////////////////

    // handler for received Intents for the "my-event" event
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            String message0 = intent.getAction();
            Log.d("receiver", "Got message: " + message);
            Log.e("receiver", "Got message: " + message0);
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }
    // This method is assigned to
//
//    // Send an Intent with an action named "my-event".
//    private void sendMessage() {
//        Intent intent = new Intent("my-event");
//        // add data
//        intent.putExtra("message", "data");
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }
    private static IntentFilter makeDfuActionIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//        intentFilter.addAction("ACTION_DISCOVER_NEAR_WYSS_BLE_IN_DIALOG");
        intentFilter.addAction("ACTION_YO_LO");
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        return intentFilter;
    }

    private EditText mEditText;
    private BleController mBleController;
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble_test);

        mEditText = findViewById(R.id.ble_companion_link_edit);

//        final LocalBroadcastManager manager = LocalBroadcastManager.getInstance(this);
//        final IntentFilter actionFilter = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        final IntentFilter actionFilter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
//        final IntentFilter actionFilter0 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        final IntentFilter actionFilter9 = new IntentFilter("ACTION_YO_LO");
//        final IntentFilter actionFilter2 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//
//        manager.registerReceiver(mMessageReceiver, makeDfuActionIntentFilter());
//        manager.
//

//        MyApp.getApplicationContext().registerReceiver(mMessageReceiver, makeDfuActionIntentFilter());
//        registerReceiver(mMessageReceiver, actionFilter1);
//        registerReceiver(mMessageReceiver, actionFilter0);
//        registerReceiver(mMessageReceiver, actionFilter9);
//        registerReceiver(mMessageReceiver, actionFilter2);

        findViewById(R.id.ble_companion_link).setOnClickListener(v -> {
            testNotifLink(mEditText.getText().toString());
        });
        findViewById(R.id.ble_companion_notif_permission).setOnClickListener(v -> {
            testNotifAuth();
        });
        findViewById(R.id.ble_companion_notif_permission).setOnLongClickListener(v -> {
            testNotifAuthForce();
            return true;
        });

//        scheduleJob(this);

        LinearLayout container = findViewById(R.id.test_ble_container);
        for (int i =0; i<mCharacts.size()+1; ++i) {
            Button b = new Button(this);

//            final Intent intent = new Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
            final Intent intent = new Intent("com.valotec.neurocomm_mobile_app.aaa", Uri.parse("uri"), getApplicationContext(), broadCastFake.class);
            if (i ==0 ) {
                BleCharacteristic charr =  mCharacts.get(i);
                final BleCharacteristic charrCopy = new BleCharacteristic(charr);
                b.setText("characteristic :: :: "+charr.getName());

                intent.putExtra("actionLocal", "actionLocal");
                intent.putExtra("rowClicked", "-2");
            } else if (i == 1) {
                BleCharacteristic charr =  mCharacts.get(i);
                final BleCharacteristic charrCopy = new BleCharacteristic(charr);

                b.setText("characteristic :: :: "+charr.getName());

                intent.putExtra("actionLocal2", "actionLocal2");
                intent.putExtra("rowClicked", i);
            } else if (i == 2) {
                BleCharacteristic charr =  mCharacts.get(i);
                final BleCharacteristic charrCopy = new BleCharacteristic(charr);
                b.setText("characteristic :: :: "+charr.getName());

                intent.putExtra("actionLocal2", "actionLocal2");
                intent.putExtra("rowClicked", i);
            } else if (i == mCharacts.size()) {
                b.setText("network ?");

            }

            b.setPadding(12, 90, 12, 90);
            b.setOnClickListener(v -> {

                if (((Button)v).getText().toString().contains("network")) {
                    testNetworkLoki();
                } else {
                    Log.e("yallah", " intent ? " + intent);
                    Log.e("yallah", " intent ? " + intent.getAction());
                    sendBroadcast(intent);
                    sendBroadcast(new Intent(getApplicationContext(), broadCastFake.class));
                }
            });
            //todo ::: this should not be commented
            //     :::>     b.setOnClickListener(v -> mBleController.addOperation(charrCopy));
            container.addView(b);
        }

//        (new Handler()).postDelayed(() -> {
//            FragmentManager fm = getSupportFragmentManager();
//            if (fm.findFragmentByTag(BleConnectionDialog.TAG) == null) {
//                BleConnectionDialog mBleConnectionDialog = BleConnectionDialog.newInstance();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.add(mBleConnectionDialog, BleConnectionDialog.TAG);
//                ft.commit();
//            }
//
//        }, 2150);

//        mBroadCastReceiver = BroadCastReceiver.initBroadcastReceiver(this,this, null);


    }

    private void testNetworkLoki() {

//        Log.e("yallah", "network is "+isNetworkAvailable());
        Log.e("yallah", "network is "+isNetworkAvailable());
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();



        if (activeNetworkInfo != null ) {
            Log.e("yallah", "network is isAvailable " + activeNetworkInfo.isAvailable());
            Log.e("yallah", "network is isConnected " + activeNetworkInfo.isConnected());
            Log.e("yallah", "network is Connecting? " + activeNetworkInfo.isConnectedOrConnecting());
            Log.e("yallah", "network is isFailover  " + activeNetworkInfo.isFailover());
            Log.e("yallah", "network is isRoaming   " + activeNetworkInfo.isRoaming());
            Log.e("yallah", "network is " + activeNetworkInfo.isConnected());
        } else {
            Log.e("yallah", "network OBJ:: is null");
        }

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void testNotifAuth() {
        if (deviceManager.getAssociations().size() >= 1) {
            if (!deviceManager.hasNotificationAccess(new ComponentName(this, MyNotificationListenerService.class)))
                deviceManager.requestNotificationAccess(new ComponentName(this, MyNotificationListenerService.class));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void testNotifAuthForce() {
        if (deviceManager.getAssociations().size() >= 1) {
                deviceManager.requestNotificationAccess(new ComponentName(this, MyNotificationListenerService.class));
        }
    }


    @Override
    public void onBleConnecting() {
        Log.e(TAG, "    C O N N E C T I N G");
    }

    @Override
    public void onBleConnected() {
        Log.e(TAG, "    C O N N E C T E D");
    }

    @Override
    public void onBleDisconnected() {
        Log.e(TAG, "    D I S C O N N E C T E D");
    }

    @Override
    public void onDescriptorWriteFinish(String charactUiid) {

    }

    @Override
    public void popNewBleDialog() {

    }
    private BroadCastReceiver mBroadCastReceiver;

    @Override
    public void initBleConnection(String addressBle, String name) {
//        ((NoNeedToolBar)findViewById(R.id.noneedtoolbar)).userConnected(name);


        for (BleCharacteristic charact: mCharacts)
            BleSingleton.getInstance().getBleController().getStatusManager().addRepository(charact);


        BleController bleController = BleSingleton.getInstance().getBleController();
        bleController.connect(this, addressBle);



        handy.postDelayed(() -> cochonDinde(), 3000);

    }

    Runnable runner = new Runnable() {
        @Override
        public void run() {
            BleSingleton.getInstance().getBleController().testRSSI();
            cochonDinde();
        }
    };
    Handler handy = new Handler();

    private void cochonDinde() {
        handy.postDelayed(runner, 400);
    }

    public static void scheduleJob(Context context) {
//        ComponentName serviceComponent = new ComponentName(context, JobSchedulerService.class);
//        JobInfo.Builder builder = new JobInfo.Builder(13298, serviceComponent);
//        builder.setMinimumLatency(0); // wait at least
//        builder.inten
//        //builder.setOverrideDeadline(2 * 60 * 1000); // maximum delay
////        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY); // require unmetered network
//        builder.setPersisted(true);
//
//        //builder.setRequiresDeviceIdle(true); // device should be idle
//        //builder.setRequiresCharging(false); // we don't care if the device is charging or not
//        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
//        jobScheduler.schedule(builder.build());
//        Log.e("JOB", "CAST JOB SCHEDULER");
    }



    private CompanionDeviceManager deviceManager;
    private AssociationRequest pairingRequest;
    private BluetoothDeviceFilter deviceFilter;
    boolean isStart = false;
    private static final int SELECT_DEVICE_REQUEST_CODE = 4242;

    @TargetApi(Build.VERSION_CODES.O)
    public void testNotifLink(String filterName) {
        if (filterName == null) {
            filterName = "AU";
        }
        deviceManager = getSystemService(CompanionDeviceManager.class);

        Toast.makeText(this, "filter name is "+filterName, Toast.LENGTH_SHORT).show();
        // To skip filtering based on name and supported feature flags (UUIDs),
        // don't include calls to setNamePattern() and addServiceUuid(),
        // respectively. This example uses Bluetooth.
        BluetoothDeviceFilter.Builder builder = new BluetoothDeviceFilter.Builder();
//        builder.setNamePattern(Pattern.compile("My device"));
        builder.setNamePattern(Pattern.compile(filterName));
        //builder.
//        builder.addServiceUuid(new ParcelUuid(UUID.fromString("00002A00-0000-1000-8000-00805F9B34FB")), null);
//        builder.addServiceUuid(new ParcelUuid(new UUID(0x123abcL, -1L)), null);
        deviceFilter = builder.build();

        // The argument provided in setSingleDevice() determines whether a single
        // device name or a list of device names is presented to the user as
        // pairing options.
        pairingRequest = new AssociationRequest.Builder()
                .addDeviceFilter(deviceFilter)
                .setSingleDevice(true)
                .build();

        Log.i("yallah", "we already have 444 Association in Association "+deviceManager.getAssociations());
        // When the app tries to pair with the Bluetooth device, show the
        // appropriate pairing request dialog to the user.

        deviceManager.associate(pairingRequest,
                new CompanionDeviceManager.Callback() {
                    @Override
                    public void onDeviceFound(IntentSender chooserLauncher) {
                        Log.e("yallah", "intent sender found is "+chooserLauncher);
                        Log.e("yallah", "intent sender found is "+chooserLauncher.getCreatorUid());
                        Log.e("yallah", "intent sender found is "+chooserLauncher.getCreatorPackage());
                        Log.e("yallah", "intent sender found is "+chooserLauncher.describeContents());
                        Log.e("yallah", "intent sender found is "+chooserLauncher.getCreatorUserHandle());

                        if (!isStart) {
                            isStart = true;
                            try {
                                startIntentSenderForResult(chooserLauncher, SELECT_DEVICE_REQUEST_CODE, new Intent("ACTION_YO_LO"), 0, 0, 0);
                            } catch (IntentSender.SendIntentException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(CharSequence error) {
                        Log.e("yallah", "intent sender found is "+error);
                        Log.e("yallah", "intent sender found is "+error.toString());

                    }
                },
                null);

//        (new Handler()).postDelayed(() -> {
////                    Log.i("yallah", "444 Association " +deviceManager.hasNotificationAccess(new ComponentName(getApplication(), "com.valotec.neurocomm_mobile_app.pichantest.poc_de_lifika.MyNotificationListenerService")));
//                    Log.i("yallah", "444 Association " +MyNotificationListenerService.class.getSimpleName());
//                    Log.i("yallah", "444 Association getCanonicalName " +MyNotificationListenerService.class.getCanonicalName());
//                    Log.i("yallah", "444 Association " +MyNotificationListenerService.class.getName());
//                    Log.i("yallah", "444 Association getTypeName      " +MyNotificationListenerService.class.getTypeName());
                    Log.i("yallah", "444 Association getAssociations  " +deviceManager.getAssociations());
//                , 4250);
    }

    private PendingIntent getPendingIntent() {
        Intent alarmIntent = new Intent(this, broadCastFake.class);
        alarmIntent.setAction("aaa_aaa");
        return PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // Device scan callback
    private ScanCallback mLeScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, final ScanResult result) {
            Log.e("yallah", "zzzzz enfin 444");
            Log.e("yallah", "zzzzz enfin 444");
            Log.e("yallah", "zzzzz enfin 444");

        }
    };

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SELECT_DEVICE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // User has chosen to pair with the Bluetooth device.
            Log.e("yallah", "intent devjce result OK ");


            BluetoothDevice deviceToPair = intent.getParcelableExtra(CompanionDeviceManager.EXTRA_DEVICE);
            deviceToPair.createBond();

            Log.e("yallah", "intent devjce result OK "+deviceToPair.getName());
            Log.e("yallah", "intent devjce result OK "+deviceToPair.getAddress());



            Toast.makeText(this, "device bounded", Toast.LENGTH_SHORT).show();


//            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//
//            ScanFilter PsaFilter = new ScanFilter.Builder()
////                    .setDeviceName(deviceToPair.getName())
//                    .build();
//
//            ScanSettings scanSettings = new ScanSettings.Builder()
//                    .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
////                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
//                    .setReportDelay(0)
//                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                    .build();



//            bluetoothAdapter.getBluetoothLeScanner().startScan(Arrays.asList(PsaFilter), scanSettings, getPendingIntent() );


//            deviceToPair.
//                            BluetoothGatt mBluetoothGatt = deviceToPair.connectGatt(this, true,


//                            mBluetoothGatt.readRemoteRssi();

            // ... Continue interacting with the paired device.
        } else {
            Log.e("yallah", "WTF WTF WTF");
            Log.e("yallah", "WTF WTF WTF" +
                    "");
        }

    }
}
