package poc_de_lifika;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.companion.CompanionDeviceManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.JobIntentService;
import android.util.Log;

import com.valotec.neurocomm_mobile_app.BytesHelper.ByteHelper;
import com.valotec.neurocomm_mobile_app.source.ble.BleController;
import com.valotec.neurocomm_mobile_app.source.ble.WYSGattAttributes;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleConnectionStatus;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleConnectionStatusManager;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleGattCallBack;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleHandler;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleSingleton;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BLECallBack;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.BleCharacteristic;
import com.valotec.neurocomm_mobile_app.source.ble.generic.model.execution.BleExecType;

import java.util.ArrayList;
import java.util.HashMap;


public class JobSchedulerService extends JobIntentService {
    private BleController bleController = BleSingleton.getInstance().getBleController();

    private BleConnectionStatusManager mManager = new BleConnectionStatusManager(bleController);
    private HashMap<String, BleCharacteristic> mCharacts;

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("JOB", "START JOB SCHEDULER intent "+intent+" flag "+flags+"    startId="+startId);
        Log.e("JOB", "START JOB SCHEDULER intent "+intent.getAction()+" flag "+flags+"    startId="+startId);
        Log.e("JOB", "START JOB SCHEDULER intent "+intent.getExtras()+" flag "+flags+"    startId="+startId);
        //testNotifLink(null);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        Log.e("JOB", "START JOB SCHEDULER intent .. onCreate");
        super.onCreate();
        Log.e("JOB", "START JOB SCHEDULER intent .. onCreateD");
    }

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context) {
        Intent intent = new Intent(context, JobSchedulerService.class);
        enqueueWork(context, JobSchedulerService.class, 39247, intent);
        Log.d("JOB", "ENQUEUEWORK     JOB SCHEDULER");
        Log.d("JOB", "ENQUEUEWORK     JOB SCHEDULER");
    }


    private void stopTask() {
        Log.d("JOB", "STOP JOB SCHEDULER");
        Log.d("JOB", "STOP JOB SCHEDULER");
    }


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.e("JOB", "HANDLING    HANDLING    HANDLING    HANDLING     HANDLING     HANDLING     JOB SCHEDULER  ======  "+intent);
        Log.e("JOB", "HANDLING    HANDLING    HANDLING    HANDLING     HANDLING     HANDLING     JOB SCHEDULER  ======  "+intent.getAction());
        Log.e("JOB", "HANDLING    HANDLING    HANDLING    HANDLING     HANDLING     HANDLING     JOB SCHEDULER  ======  "+intent.getPackage());
        Log.e("yallah", "    JobService -> start      choosen==  "+intent.getIntExtra("rowClicked", -1));
        updateLogIsConnected(intent.getIntExtra("rowClicked", -1));
        Log.e("JOB", "HANDLING    HANDLING    HANDLING    HANDLING     HANDLING     HANDLING     JOB SCHEDULER  ======  =========== END END");
//        while (true) {
//
//        }

    }
//
//    /****************************************************************************************/
//    /** Managing the binder to this service *************************************************/
//    /****************************************************************************************/
//    /**
//     * The binder that glue to my service
//     */
//    private final Binder binder=new LocalBinder();
//    /**
//     * @author mSeguy
//     * @goals This class aims to define the binder to use for my service
//     */
//    public class LocalBinder extends Binder {
//
//        public JobSchedulerService getService() {
//            return JobSchedulerService.this;
//        }
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return binder;
//    }

    @Override
    public void onDestroy() {
        Log.e("JOB", "onDestroy    onDestroy    onDestroy    onDestroy     onDestroy     onDestroy     JOB SCHEDULER  ======  ");
        stopTask();
        super.onDestroy();
    }

    @Override
    public void setInterruptIfStopped(boolean interruptIfStopped) {
        Log.e("JOB", "setInterruptIfStopped    setInterruptIfStopped    setInterruptIfStopped    setInterruptIfStopped     setInterruptIfStopped     setInterruptIfStopped     JOB SCHEDULER  ======  "+interruptIfStopped);
        super.setInterruptIfStopped(false);
    }

    @Override
    public boolean isStopped() {
        Log.e("JOB", "isStopped   isStopped   isStopped   isStopped   isStopped   JOB SCHEDULER  ======  ");
        return super.isStopped();
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.e("JOB", "onStopCurrentWork   onStopCurrentWork   onStopCurrentWork   onStopCurrentWork    onStopCurrentWork    JOB SCHEDULER  ======  ");
        return false;
    }

    public void updateLogIsConnected(int buttonNumber) {
        if (bleController==null) bleController = BleSingleton.getInstance().getBleController();


//        if (!bleController.getStatusManager().isConnected) {
            Log.e("yallah", "    JobBleService :: Ble Status =  Not Connected ..!!.. "+bleController.getStatusManager().isConnected);
        Log.e("yallah", "    JobService -> start      choosen==  "+buttonNumber);
            testNotifLink(this, buttonNumber);
//        } else {
//            Log.e("yallah", "    JobBleService :: Ble Status =  IS Connected  ..!!.. "+bleController.getStatusManager().isConnected);
//
//        }
    }

    public static String SERVICE_WYSSSS                      = "000018FF-0000-1000-8000-00805F9B34FB";

    public static String CHARACTERISTIC_                     = "00001822-0000-1000-8000-00805F9B34FB";
    public static String CHARACTERISTIC_2                    = "00001824-0000-1000-8000-00805F9B34FB";

    final private BleCharacteristic mCharacteristic =
            new BleCharacteristic(
                    SERVICE_WYSSSS,
                    CHARACTERISTIC_,
                    BleExecType.READ,
                    new BLECallBack() {
                        @Override
                        public void onCharacteristicRead(byte[] o) {
                            Log.e("yallah", "    JobService -> work work  "+ ByteHelper.bytesToHex(o));
                            Log.e("yallah", "    JobService -> work work  "+o);
                        }
                    });
    final private BleCharacteristic mCharacteristic2 =
            new BleCharacteristic(
                    SERVICE_WYSSSS,
                    CHARACTERISTIC_2,
                    BleExecType.READ,
                    new BLECallBack() {
                        @Override
                        public void onCharacteristicRead(byte[] o) {
                            Log.e("yallah", "    JobService -> work work2  "+ ByteHelper.bytesToHex(o));
                            Log.e("yallah", "    JobService -> work work2  "+o);
                        }
                    });

    private static final int SELECT_DEVICE_REQUEST_CODE = 4242;
    @TargetApi(Build.VERSION_CODES.O)
    public void testNotifLink(Context context, int buttonNumber) {
        Log.e("yallah", "    JobService -> testNotifLink ");
        Log.e("yallah", "    JobService -> start      C O N N E C T I N G     with ");
        CompanionDeviceManager deviceManager = getSystemService(CompanionDeviceManager.class);
//        String deviceMacAdress = "04:52:C7:C4:87:C3";//deviceManager.getAssociations().get(0);
        String deviceMacAdress = deviceManager.getAssociations().get(0);
        //00:1E:C0:2B:53:CC ok
        //04:52:C7:C4:87:C3 failed (screw beatch)
        Log.e("yallah", "    JobService -> start      C O N N E C T I N G     with "+deviceMacAdress);
        Log.e("yallah", "    JobService -> start      C O N N E C T I N G     with "+deviceMacAdress);
        Log.e("yallah", "    JobService -> start      C O N N E C T I N G     with "+deviceMacAdress);
        Log.e("yallah", "    JobService -> start      C O N N E C T I N G     exec     button = "+buttonNumber);

        bleController.getStatusManager().addRepository(mCharacteristic);
        bleController.getStatusManager().addRepository(mCharacteristic2);
        bleController.connect(this, deviceMacAdress);

        ArrayList<BleCharacteristic> listOperation;
        if (buttonNumber == 1) {
            Log.e("yallah", "    JobService -> start      choosen == 1");
            listOperation = new ArrayList<BleCharacteristic>() {{add(mCharacteristic);}};

        } else if (buttonNumber == 2) {
            Log.e("yallah", "    JobService -> start      choosen == 2");
            listOperation = new ArrayList<BleCharacteristic>() {{add(mCharacteristic2);}};
        } else {
            Log.e("yallah", "    JobService -> start      Error Error not operation define.. neither choosen==  ");
            listOperation = new ArrayList<BleCharacteristic>() {{add(mCharacteristic);}};
        }

        bleController.addOperation(listOperation);

        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        Log.e("yallah", "    JobService -> start      getConnectedDevices ==  "+manager.getConnectedDevices(BluetoothProfile.GATT));
        bleController.execPendingOperations();

    }


}









