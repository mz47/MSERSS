package uni.mse.mserss;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends Activity {

    private DbHelper db;
    private TextView tvHeadline;
    private WebView wvContent;
    private Button btOpen;
    private Button btShare;
    private Button btPrevious;
    private Button btNext;
    private Item item;
    private ItemList items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Initialize();
        ShowItem();

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Open();
            }
        });
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Share();
            }
        });
        btPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Previous();
            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Next();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Next();
                return true;
            case MotionEvent.ACTION_UP:
                Previous();
                return true;
            default: return super.onTouchEvent(event);
        }
    }

    private void Initialize() {
        tvHeadline = (TextView) findViewById(R.id.tvHeadline);
        wvContent = (WebView) findViewById(R.id.wvContent);
        btOpen = (Button) findViewById(R.id.btOpen);
        btShare = (Button) findViewById(R.id.btShare);
        btPrevious = (Button) findViewById(R.id.btPrevious);
        btNext = (Button) findViewById(R.id.btNext);

        Intent intent = getIntent();
        final int channelId = intent.getIntExtra("channelId", -1);
        final int itemId = intent.getIntExtra("itemId", -1);

        db = new DbHelper(this);
        Channel channel = db.getChannel(channelId);
        items = channel.getItems();
        item = items.getItem(itemId);
        item.setId(itemId);
    }

    private void ShowItem() {
        tvHeadline.setText(item.getTitle());
        wvContent.loadData(item.getContent(), "text/html; charset=UTF-8", null);
    }

    private void Open() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getUrl()));
            startActivity(intent);
        }
        catch(Exception ex) {
            Log.e("DetailAct.Open", ex.toString());
        }
    }

    private void Share() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, item.getUrl());
            startActivity(Intent.createChooser(intent, "Share"));
        }
        catch(Exception ex) {
            Log.e("DetailAct.Share", ex.toString());
        }
    }

    private void Previous() {
        try {
            int id = item.getId();
            if(id > -1) {    // id in Intent gefunden, vorherige id nicht kleiner 0
                Item previous = items.getItem(id - 1);
                if(previous != null) {    // Vorheriges Element gefunden
                    this.item = previous;
                    item.setId(id - 1);
                    ShowItem();
                }
                else {
                    btPrevious.setEnabled(false);
                    btNext.setEnabled(true);
                    Log.d("DetailActivity.Previous", "previous element null");
                }
            }
            else {
                btPrevious.setEnabled(false);
                btNext.setEnabled(true);
                Log.d("DetailActivity.Previous", "id < -1 OR id-1 < -1 (id=" + id + ")");
            }
        }
        catch (Exception ex) {
            Log.e("DetailAct.Prev", ex.toString());
        }
    }

    private void Next() {
        try {
            int id = item.getId();
            if(id > -1) {    // id in Intent gefunden, vorherige id nicht kleiner 0
                Item next = items.getItem(id + 1);
                if(next != null) {    // Vorheriges Element gefunden
                    this.item = next;
                    item.setId(id + 1);
                    ShowItem();
                }
                else {
                    Log.d("DetailActivity.Previous", "next element null");
                }
            }
            else {
                Log.d("DetailActivity.Previous", "id < -1 OR id-1 < -1 (id=" + id + ")");
            }
        }
        catch (Exception ex) {
            Log.e("DetailAct.Prev", ex.toString());
        }
    }
}
