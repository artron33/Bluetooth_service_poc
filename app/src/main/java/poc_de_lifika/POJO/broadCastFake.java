package poc_de_lifika.POJO;

import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.companion.CompanionDeviceManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class broadCastFake extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        // assumes WordService is a registered service
//        Toast.makeText(context, "Don't panik but your time is up!!!!.", Toast.LENGTH_LONG).show();
        Log.e("yallah", "zzzzz enfin "+intent);
        Log.e("yallah", "zzzzz enfin extras extras extras extras extras extras"+intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE));

        Log.e("yallah", "zzzzz enfin Extras  "+intent.getExtras());

        CompanionDeviceManager deviceManager = context.getSystemService(CompanionDeviceManager.class);
//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        ScanFilter PsaFilter = new ScanFilter.Builder()
//                    .setDeviceAddress(deviceManager.getAssociations().get(0))
//                .build();
////        Log.e("yallah", "zzzzz enfin==========================PsaFilter========" +PsaFilter);
//        ScanSettings scanSettings = new ScanSettings.Builder()
//                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
////                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
//                .setReportDelay(0)
//                .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
//                .build();
//

        Intent intent44 = new Intent(context, POJOService.class);
        Log.e("yallah", "POSO Receiver  ->    Start Intent");
        context.startService(intent44);

        if(intent.hasExtra("actionLocal")){
            Log.i("test","444 Association "+"********** onReceive ********** extras        actionLocal0000");

//                MyNotificationListenerService.this.cancelAllNotifications();
        } else if(intent.hasExtra("actionLocal2")){
            Log.i("test","444 Association "+"********** onReceive ********** extras        actionLocal2222");
//            JobSchedulerService.enqueueWork(context);

        }

//        else
//        if (STATE_TURNING_OFF == intent.getExtras().getInt("android.bluetooth.adapter.extra.STATE")) {
//            Log.e("yallah", "zzzzz enfin==========================bluetoothAdapter======== STATE_TURNING_OFF" );
////
////            BluetoothAdapter
////                    .getDefaultAdapter()
////                    .getBluetoothLeScanner()
////                    .stopScan(getPendingIntent(context));
//
//        } else if (bluetoothAdapter.getBluetoothLeScanner() != null && STATE_ON == intent.getExtras().getInt("android.bluetooth.adapter.extra.STATE")) {
//            Log.e("yallah", "zzzzz enfin==========================bluetoothAdapter======== State ON" );
////            JobSchedulerService service = new JobSchedulerService();
////            JobSchedulerService.enqueueWork(context);
//
//
////            BluetoothAdapter
////                    .getDefaultAdapter()
////                    .getBluetoothLeScanner()
////                    .startScan(Arrays.asList(PsaFilter), scanSettings, getPendingIntent(context));
////
//
//        } else if (intent.getAction().equals("aaa_aaa")) {
//            Log.e("yallah", "zzzzz enfin==========================bluetoothAdapter======== aenqueu work" );
////            JobSchedulerService service = new JobSchedulerService();
////            service.startService(new Intent("ACTION_YO_LO"));
////            JobSchedulerService.enqueueWork(context);
//
////            BluetoothAdapter
////                    .getDefaultAdapter()
////                    .getBluetoothLeScanner()
////                    .stopScan(getPendingIntent(context));
//        }

//        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner().startScan();
        // Vibrate the mobile phone
//        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        vibrator.vibrate(2000);

//        context.(111);
    }

////    public static PendingIntent getPendingIntent(Context context) {
////        Intent alarmIntent = new Intent(context, JobSchedulerService.class);
//////        alarmIntent.setAction("com.myapp.ACTION_FOUND");
////        return PendingIntent.getService(context, 123456, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
////    }
//

//    public static PendingIntent getPendingIntent(Context context) {
//        Intent alarmIntent = new Intent(context, broadCastFake.class);
//        alarmIntent.setAction("ACTION_YO_LO");
//        return PendingIntent.getBroadcast(context, 1013, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }


    public static PendingIntent getPendingIntent(Context context) {
        Intent alarmIntent = new Intent(context, broadCastFake.class);
        alarmIntent.setAction("aaa_aaa");
        return PendingIntent.getBroadcast(context, 1013, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
