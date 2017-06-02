package uni.mse.mserss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ChannelsActivity extends AppCompatActivity {

    private ListView lvChannels;
    private DbHelper db;
    private ChannelList channels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);

        Initialize();
    }

    private void Initialize() {
        db = new DbHelper(this);
        channels = db.getChannels();
        db.close();
        if(channels.getChannels().size() > 0) {
            lvChannels = (ListView) findViewById(R.id.lvChannels);
            lvChannels.setAdapter(new ArrayAdapter<>(this, R.layout.channels_items_item, channels.toTitleList()));
            lvChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(ChannelsActivity.this, ItemsActivity.class);
                    intent.putExtra("id", position);
                    startActivity(intent);
                }
            });
        }
        else {
            Log.d("channels.initialize", "channels empty");
        }
    }
}
