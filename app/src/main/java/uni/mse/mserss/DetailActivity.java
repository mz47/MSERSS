package uni.mse.mserss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    private TextView tvHeadline;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Initialize();

        Intent intent = getIntent();
        tvHeadline.setText("Headline " + intent.getIntExtra("index", -1));
    }

    private void Initialize() {
        tvHeadline = (TextView) findViewById(R.id.tvHeadline);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }
}
