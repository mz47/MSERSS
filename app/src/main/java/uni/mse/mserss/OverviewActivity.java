package uni.mse.mserss;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class OverviewActivity extends FragmentActivity {

    private ListView lvItems;
    private List items = new List();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        Initialize();
    }

    private void Initialize() {
        GenerateItems(3);

        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(new ArrayAdapter<>(this, R.layout.overview_item, items.ToUrlList()));
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent details = new Intent(OverviewActivity.this, DetailActivity.class);
                details.putExtra("index", position);
                startActivity(details);
            }
        });
    }

    // Debug Only
    private void GenerateItems(int n) {
        for(int i = 0; i < n; i++) {
            Item item = new Item("www.google.de", "Google_" + i);
            items.Add(item);
        }
    }
}
