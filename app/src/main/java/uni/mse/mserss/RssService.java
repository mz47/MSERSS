package uni.mse.mserss;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by marcel on 20.06.17.
 */

public class RssService extends IntentService {
    public RssService() {
        super("MSERSS.RssService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //TODO fetch all channel data
        boolean isUpToDate = true;
        int signature = intent.getIntExtra("signature", -3);

        Log.d("rssService", "signature: " + signature);

        DbHelper db = new DbHelper(this);
        ChannelList channels = db.getAllChannels();

        for(Channel c : channels.getChannels()) {
            if(c.isRefresh()) {
                c.parse();
            }
        }

        if(!isUpToDate) {
            sendNotification();
        }
    }

    private void sendNotification() {
        Intent notificationIntent = new Intent(this, OverviewActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification =  new Notification.Builder(this);
        notification.setSmallIcon(R.drawable.rss_icon);
        notification.setContentTitle("MSERSS");
        notification.setContentText("All Feeds updated");

        notificationManager.notify(1, notification.build());



    }
}
