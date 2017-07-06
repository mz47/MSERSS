package uni.mse.mserss;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

/**
 * Created by marcel on 20.06.17.
 */

public class RssService extends IntentService {
    public RssService() {
        super(RssService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d("RssService", "service started");

        DbHelper db = new DbHelper(this);
        ChannelList channels = db.getChannels();
        db.close();

        String lastSignature = intent.getStringExtra("signature");
        String currentSignature = "";

        for(Channel c : channels.getChannels()) {
            //if(c.getRefresh() == 1) {
                c.parse();
                currentSignature += c.getSignature();
            //}
        }

        //sendNotification();
        Log.d("rssservice", "last signature: " + lastSignature);
        Log.d("rssservice", "current signature: " + currentSignature);

        if(currentSignature.equals(lastSignature) == false) {
            sendNotification();
            lastSignature = currentSignature;
        }
    }

    private void sendNotification() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification =  new Notification.Builder(this);
        notification.setSmallIcon(R.drawable.rss_icon);
        notification.setContentTitle("MSERSS");
        notification.setContentText("New Items available!");
        notificationManager.notify(1, notification.build());
    }
}
