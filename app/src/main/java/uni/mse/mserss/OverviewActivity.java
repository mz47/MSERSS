package uni.mse.mserss;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        try {
            GenerateItems(3);

            lvItems = (ListView) findViewById(R.id.lvItems);
            lvItems.setAdapter(new ArrayAdapter<>(this, R.layout.overview_item, items.ToNameList()));
            lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent details = new Intent(OverviewActivity.this, DetailActivity.class);
                    details.putExtra("index", position);
                    details.putExtra("url", items.Get(position).getUrl());
                    details.putExtra("name", items.Get(position).getName());
                    startActivity(details);
                }
            });
        }
        catch (Exception ex) {
            Log.e("Overview.Initialize", ex.getMessage());
        }
    }

    // Debug Only
    private void GenerateItems(int n) {
        for(int i = 0; i < n; i++) {
            Item item = new Item("https://rss.golem.de/rss.php?feed=RSS2.0", "Golem_rss-" + i);
            items.Add(item);
        }
    }
}
