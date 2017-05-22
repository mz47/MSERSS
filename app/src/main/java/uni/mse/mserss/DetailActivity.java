package uni.mse.mserss;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView tvHeadline;
    private TextView tvContent;
    private WebView wvContent;
    private Button btOpen;
    private Button btShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Initialize();

        Intent intent = getIntent();
        final String headline = intent.getStringExtra("headline");
        final String content = intent.getStringExtra("content");
        final String url = intent.getStringExtra("url");

        tvHeadline.setText(headline);
        //tvContent.setText(content);
        wvContent.loadData(content, "text/html; charset=UTF-8", null);

        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                //intent.putExtra("url", url);
                startActivity(intent);
            }
        });
    }

    private void Initialize() {
        tvHeadline = (TextView) findViewById(R.id.tvHeadline);
        //tvContent = (TextView) findViewById(R.id.tvContent);
        wvContent = (WebView) findViewById(R.id.wvContent);
        btOpen = (Button) findViewById(R.id.btOpen);
        btShare = (Button) findViewById(R.id.btShare);
    }
}
