package uni.mse.mserss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ChannelsActivity extends Activity {

    private ListView lvChannels;
    private DbHelper db;
    private ChannelList channels;
    private int id;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        Initialize();
    }

    private void Initialize() {
        Intent intent = getIntent();
        id = intent.getIntExtra("listId", 0);
        name = intent.getStringExtra("listName");

        getActionBar().setTitle(name);

        db = new DbHelper(this);
        channels = db.getChannels();
        db.close();

        lvChannels = (ListView) findViewById(R.id.lvChannels);
        lvChannels.setAdapter(new ArrayAdapter<>(this, R.layout.channels_items_item, channels.toTitleList()));
        lvChannels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Channel c = channels.get(position);
                Intent intent = new Intent(ChannelsActivity.this, ItemsActivity.class);
                intent.putExtra("channelId", c.getId());
                startActivity(intent);
            }
        });
    }

    private void ReturnToOverview() {
        startActivity(new Intent(ChannelsActivity.this, OverviewActivity.class));
    }
}
