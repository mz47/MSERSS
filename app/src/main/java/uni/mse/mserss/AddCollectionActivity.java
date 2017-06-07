package uni.mse.mserss;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddCollectionActivity extends Activity {

    private EditText txCollectionTitle;
    private Button btAddCollection;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_collection);

        getActionBar().setTitle("Add Collection");

        txCollectionTitle = (EditText) findViewById(R.id.txCollectionTitle);
        btAddCollection = (Button) findViewById(R.id.btAddCollection);
        db = new DbHelper(this);

        btAddCollection.setEnabled(false);

        txCollectionTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals("")) {
                    btAddCollection.setEnabled(false);
                }
                else {
                    btAddCollection.setEnabled(true);
                }
            }
        });

        btAddCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String title = txCollectionTitle.getText().toString().trim();   // Get title, trim
                    db.addCollection(new Collection(title));    // Create new Collection, add to Database
                    startActivity(new Intent(AddCollectionActivity.this, OverviewActivity.class));  // Return to Overview Activity
                }
                catch (Exception ex) {
                    Log.e("AddCollection", ex.getMessage());
                }
                finally {
                    db.close(); // Close Database
                }
            }
        });
    }
}
