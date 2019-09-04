package poc_de_lifika.POJO;

import android.os.AsyncTask;
import android.util.Log;

public class AndroidAsyncTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            infinithBucle();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void infinithBucle() throws InterruptedException {
        int i = 0;
        while (true) {
            Log.e("yallah", "    POJO Service  -> start     R U N N I N G     with "+ i++);
            if (i> 10000) i = 0;
            Thread.sleep(15000);

        }
    }
}
