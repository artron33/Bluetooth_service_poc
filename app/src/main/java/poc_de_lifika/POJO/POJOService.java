package poc_de_lifika.POJO;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;




public class POJOService extends Service {
    private Looper serviceLooper;


    @Override
    public void onCreate() {
        Log.e("yallah", "    POJO Service  ->  onCreate");
        Toast.makeText(this, "service creating", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("yallah", "    POJO Service  ->  onStartCommand");
        Toast.makeText(this, "service starting ", Toast.LENGTH_SHORT).show();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("yallah", "    POJO Service  ->  onBind         with Intent = " +intent);
        // We don't provide binding, so return null
        return null;
    }

    @Override
    public void onDestroy() {
        Log.e("yallah", "    POJO Service  ->  onDestroy");
        Toast.makeText(this, "service destroy", Toast.LENGTH_SHORT).show();
    }
}