package uni.mse.mserss;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

    private Button btSettingsApply;
    private Spinner spinnerSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btSettingsApply = (Button) findViewById(R.id.btSettingsApply);
        spinnerSettings = (Spinner) findViewById(R.id.spinnerSettings);

        btSettingsApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.setInterval(spinnerSettings.getSelectedItemPosition());
            }
        });
    }
}
