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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuCollectionEdit:
                ShowEditDialog();
                return true;
            case R.id.menuCollectionRemove:
                ShowRemoveDialog();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }

    private void Initialize() {
        Intent intent = getIntent();
        id = intent.getIntExtra("listId", 0);
        name = intent.getStringExtra("listName");

        getActionBar().setTitle(name);

        db = new DbHelper(this);
        channels = db.getChannels(id);
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

    private void ShowRemoveDialog() {
        AlertDialog dialog = new AlertDialog.Builder(ChannelsActivity.this).create();
        dialog.setTitle("Removing Collection");
        dialog.setMessage("Are you sure?");
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        RemoveCollection();
                        ReturnToOverview();
                    }
                });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        dialog.show();
    }

    private void ShowEditDialog() {
        AlertDialog dialog = new AlertDialog.Builder(ChannelsActivity.this).create();
        final EditText txName = new EditText(this);
        txName.setInputType(InputType.TYPE_CLASS_TEXT);
        txName.setText(name);
        dialog.setView(txName);
        dialog.setTitle("Edit Collection Name");
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditCollection(txName.getText().toString());
                ReturnToOverview();
            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void RemoveCollection() {
        if(db != null && id >= 0) {
            db.removeCollection(id);
        }
    }

    private void EditCollection(String name) {
        if(db != null && id >= 0 && name != null && name.equals("") == false) {
            db.updateCollectionName(id, name);
        }
    }

    private void ReturnToOverview() {
        startActivity(new Intent(ChannelsActivity.this, OverviewActivity.class));
    }
}
