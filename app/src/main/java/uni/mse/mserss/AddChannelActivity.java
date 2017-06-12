package uni.mse.mserss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class AddChannelActivity extends Activity {

    private EditText txChannelUrl;
    private Button btAddChanel;
    private Spinner ddCollections;
    private DbHelper db;
    private CollectionList collections;
    private Collection selectedCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        getActionBar().setTitle("Add Channel");

        txChannelUrl = (EditText) findViewById(R.id.txChannelUrl);
        btAddChanel = (Button) findViewById(R.id.btAddChannel);
        btAddChanel.setEnabled(false);
        ddCollections = (Spinner) findViewById(R.id.ddCollections);

        db = new DbHelper(this);

        fillSpinner();

        txChannelUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals("")) {
                    btAddChanel.setEnabled(false);
                }
                else {
                    btAddChanel.setEnabled(true);
                }
            }
        });

        btAddChanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addChannel();
            }
        });
        ddCollections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectChannel(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void fillSpinner() {
        try {
            if(db != null) {
                collections = db.getLists();
                collections.add(new Collection("(none)", -1));
                ddCollections.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, collections.getNames()));
                ddCollections.setSelection(collections.getSize() - 1);
                //selectedCollection = collections.get(collections.getSize() - 1);
            }
        }
        catch (Exception ex) {
            Log.e("fillSpinner", ex.toString());
        }
    }

    private void selectChannel(int position) {
        try {
            if(collections != null) {
                selectedCollection = collections.get(position);
                Log.d("AddChannel", "selectedCollection: " + selectedCollection.getName());
            }
        }
        catch (Exception ex) {
            Log.e("selectChannel", ex.toString());
        }
    }

    private void addChannel() {
        try {
            String url = txChannelUrl.getText().toString().trim();
            Channel channel = new Channel(url);
            channel.setCollection(selectedCollection);
            channel.parseMeta();
            db.addChannel(channel);

            startActivity(new Intent(AddChannelActivity.this, OverviewActivity.class));
        }
        catch (Exception ex) {
            Log.e("addChannel", ex.toString());
        }
        finally {
            db.close();
        }
    }
}
