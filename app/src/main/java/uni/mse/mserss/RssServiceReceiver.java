package uni.mse.mserss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by marcel on 20.06.17.
 */

public class RssServiceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent refreshIntent = new Intent(context, RssService.class);
        refreshIntent.putExtra("signature", intent.getStringExtra("signature"));
        context.startService(refreshIntent);
        Log.d("RssServiceReceiver", "receiver started");
    }
}
