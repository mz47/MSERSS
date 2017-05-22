package uni.mse.mserss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");

        webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl(url);

    }
}
