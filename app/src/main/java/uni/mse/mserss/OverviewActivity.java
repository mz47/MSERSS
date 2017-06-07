package uni.mse.mserss;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OverviewActivity extends FragmentActivity {

    private ExpandableListView lvExpandable;
    private ChannelList channels;
    private CollectionList lists;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        db = new DbHelper(this);
        channels = db.getChannels();
        lists = db.getLists();
        Log.d("overview.oncreate", "channels.length: " + channels.getChannels().size());
        Log.d("overview.oncreate", "lists.length: " + lists.getCollections().size());
        db.close();

        Initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuOverviewAddCollection:
                startActivity(new Intent(OverviewActivity.this, AddCollectionActivity.class));
                return true;
            case R.id.menuOverviewEditCollections:
                startActivity(new Intent(OverviewActivity.this, EditCollectionsActivity.class));
                return true;
            case R.id.menuOverviewEditFeeds:
                startActivity(new Intent(OverviewActivity.this, EditChannelsActivity.class));
                return true;
            case R.id.menuOverviewSettings:
                startActivity(new Intent(OverviewActivity.this, SettingsActivity.class));
                return true;
            case R.id.menuOverviewAbout:
                startActivity(new Intent(OverviewActivity.this, AboutActivity.class));
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void Initialize() {
        try {
            lvExpandable = (ExpandableListView) findViewById(R.id.lvExpanded);

            List<String> headlines = new ArrayList<>();
            headlines.add("Collections");
            headlines.add("Feeds");

            HashMap<String, List<String>> list = new HashMap<>();
            list.put("Collections", lists.getNames());
            list.put("Feeds", channels.toTitleList());

            ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter(this, headlines, list);
            lvExpandable.setAdapter(expandableListAdapter);
            lvExpandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if(groupPosition == 0) {    // Open List
                        Intent intent = new Intent(OverviewActivity.this, ChannelsActivity.class);
                        intent.putExtra("listId", 1);
                        startActivity(intent);
                    }
                    if(groupPosition == 1) {    // Open Channel
                        Channel c = channels.get(childPosition);
                        Intent intent = new Intent(OverviewActivity.this, ItemsActivity.class);
                        intent.putExtra("channelId", c.getId());
                        startActivity(intent);
                    }
                    return false;
                }
            });
        }
        catch (Exception ex) {
            Log.e("Overview.Initialize", ex.getMessage());
        }
    }
}
