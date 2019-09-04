package poc_de_lifika;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.companion.CompanionDeviceManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.valotec.neurocomm_mobile_app.source.ble.BleController;
import com.valotec.neurocomm_mobile_app.source.ble.generic.BleSingleton;


public class MyNotificationListenerService extends NotificationListenerService {

    private String TAG = "yallah";
    private NLServiceReceiver nlservicereciver;

    private BleController bleController = BleSingleton.getInstance().getBleController();
    private JobSchedulerService appService = null;

    private boolean fakeNotWork = false;

    /**
     * The service connection, that aims to connect to the service
     */
    private ServiceConnection onService;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);

        Log.i(TAG,"444 Association "+"**********  onCreate");

        if (onService == null && fakeNotWork) {
            onService = new ServiceConnection() {
                /* (non-Javadoc)
                 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName,* android.os.Ibinder)
                 */
                public void onServiceConnected(ComponentName className, IBinder rawBinder) {
                    Log.i(TAG,"444 Association "+"**********  onServiceConnected");

                    //appService = ((JobSchedulerService.LocalBinder) rawBinder).getService();
                }
                /* (non-Javadoc)
                 * @see
                 * android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
                 */
                public void onServiceDisconnected(ComponentName className) {
                    appService = null;
                    Log.i(TAG,"444 Association "+"**********  onServiceDisconnected");
                }
            };
            bindService(new Intent(this, JobSchedulerService.class), onService, Context.BIND_AUTO_CREATE);
        }
    }


    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap, int reason) {
        super.onNotificationRemoved(sbn, rankingMap, reason);
        Log.i(TAG,"444 Association "+"**********  onListenerRemoved");
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
        Log.i(TAG,"444 Association "+"**********  onListenerConnected");
    }


    @Override
    public void onDestroy() {
        Log.i(TAG,"444 Association "+"**********  Notification Service  ==> onDestroy     onDestroy    onDestroy");
        unregisterReceiver(nlservicereciver);
        if (onService != null) unbindService(onService);
        super.onDestroy();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"444 Association "+"**********  onNotificationPosted");
        Log.i(TAG,"444 Association "+"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
        Intent i = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationPosted :" + sbn.getPackageName() + "n");
        sendBroadcast(i);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"444 Association "+"********** onNOtificationRemoved");
        Log.i(TAG,"444 Association "+"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText +"t" + sbn.getPackageName());
        Intent i = new  Intent("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "n");

        sendBroadcast(i);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG,"444 Association "+"********** onReceive ********** intent@       "+intent);
            Log.i(TAG,"444 Association "+"********** onReceive ********** Action        "+intent.getAction());

            if (!fakeNotWork) return;

            if(intent.hasExtra("actionLocal")){
                Log.i(TAG,"444 Association "+"********** onReceive ********** extras        actionLocal0000");
//                MyNotificationListenerService.this.cancelAllNotifications();
            } else if(intent.hasExtra("actionLocal2")){
                Log.i(TAG,"444 Association "+"********** onReceive ********** extras        actionLocal2222");

            }

        }
    }
}
