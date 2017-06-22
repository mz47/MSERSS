package uni.mse.mserss;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    private Button btSettingsApply;
    private Spinner spinnerSettings;
    private ArrayList<String> interval;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        btSettingsApply = (Button) findViewById(R.id.btSettingsApply);
        spinnerSettings = (Spinner) findViewById(R.id.spinnerSettings);
        interval = new ArrayList();

        interval.add("15 minutes");
        interval.add("30 minutes");
        interval.add("60 minutes");
        interval.add("120 minutes");

        spinnerSettings.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, interval));
        spinnerSettings.setSelection(0);

        LoadSettings();

        btSettingsApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveSettings();
                finish();
            }
        });
    }

    private void LoadSettings() {
        preferences = getApplicationContext().getSharedPreferences("uni.mse.mserss.INTERVAL", getApplicationContext().MODE_PRIVATE);
        int index = preferences.getInt("interval", 0);
        spinnerSettings.setSelection(index);
    }

    private void SaveSettings() {
        preferences = getApplicationContext().getSharedPreferences("uni.mse.mserss.INTERVAL", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("interval", spinnerSettings.getSelectedItemPosition());
        editor.commit();
    }
}
