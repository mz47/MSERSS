package uni.mse.mserss;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class AddChannelActivity extends Activity {

    private EditText txChannelUrl;
    private Button btAddChanel;
    private CheckBox cbRefresh;
    private DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);
        getActionBar().setTitle("Add Channel");

        txChannelUrl = (EditText) findViewById(R.id.txChannelUrl);
        btAddChanel = (Button) findViewById(R.id.btAddChannel);
        cbRefresh = (CheckBox) findViewById(R.id.cbRefresh);
        btAddChanel.setEnabled(false);
        cbRefresh.setChecked(true);
        db = new DbHelper(this);

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
    }

    private void addChannel() {
        try {
            String url = txChannelUrl.getText().toString().trim();
            Channel channel = new Channel(url);
            if(cbRefresh.isChecked()) {
                channel.setRefresh(1);
            }
            else {
                channel.setRefresh(0);
            }
            channel.parseMeta();
            if(channel.getType().equals(Channel.TYPE_RSS)) {
                db.addChannel(channel);
                startActivity(new Intent(AddChannelActivity.this, OverviewActivity.class));
            }
            else {
                showError();
            }
        }
        catch (Exception ex) {
            Log.e("addChannel", ex.toString());
        }
        finally {
            db.close();
        }
    }

    private void showError() {
        try {
            AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle("Invalid Type");
            dialog.setMessage("The entered URL does not refer to a valid RSS feed.");
            dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
        catch (Exception ex) {
            Log.e("showError", ex.toString());
        }
    }
}
