package fw.supernacho.ru.foxweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFFERENCES = "mysettings";
    public static final String APP_PREFFERENCES_CITY = "city";
    public static final String APP_PREFFERENCES_LASTMSG = "last_msg";
    public static final String APP_PREFFERENCES_ISPRESSURE = "checked_pressure";
    public static final String APP_PREFFERENCES_ISTOMORROW = "checked_tomorrow";
    public static final String APP_PREFFERENCES_ISWEEK = "checked_week";
    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";
    private static final String WEATHER_VIEW = "weather_view";
    public static final String IS_PRESSURE = "pressure_view";
    public static final String IS_TOMORROW = "tomorrow_view";
    public static final String IS_WEEK = "week_view";

    private Spinner spinner_city;
    private TextView viewShowWeather;
    private CheckBox checkBoxPressure;
    private CheckBox checkBoxTomorrow;
    private CheckBox checkBoxWeek;
    private SharedPreferences settings;
    private boolean resulRecieved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        restoreActivity(savedInstanceState);
    }

    private void restoreActivity(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            spinner_city.setSelection(savedInstanceState.getInt(CITY_ID));
            viewShowWeather.setText(savedInstanceState.getString(WEATHER_VIEW));
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_show_weather:
//                    viewShowWeather.setText(Prediction.predict(MainActivity.this,
//                            spinner_city.getSelectedItemPosition()));
                    Intent intent = new Intent(MainActivity.this, MoreInfo.class);
                    intent.putExtra(CITY_ID, spinner_city.getSelectedItemPosition());
                    intent.putExtra(CITY_NAME, spinner_city.getSelectedItem().toString());
                    intent.putExtra(IS_PRESSURE, checkBoxPressure.isChecked());
                    intent.putExtra(IS_TOMORROW, checkBoxTomorrow.isChecked());
                    intent.putExtra(IS_WEEK, checkBoxWeek.isChecked());
                    startActivityForResult(intent, 1);
                    break;
                default:
                    throw new RuntimeException("Clicked view not found in switch");
            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String result = data.getStringExtra(MoreInfo.RESULT);
            viewShowWeather.setText(result);
            resulRecieved = true;
        } else {
            return;
        }

    }

    private void init() {
        settings = getSharedPreferences(APP_PREFFERENCES, MODE_PRIVATE);
        viewShowWeather = findViewById(R.id.textview_show_weather);
        spinner_city = findViewById(R.id.spinner_city_chooser);
        Button buttonShowWeather = findViewById(R.id.button_show_weather);
        buttonShowWeather.setOnClickListener(onClickListener);
        checkBoxPressure = findViewById(R.id.checkbox_predict_pressure);
        checkBoxTomorrow = findViewById(R.id.checkbox_predict_tomorrow);
        checkBoxWeek = findViewById(R.id.checkbox_predict_week);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CITY_ID, spinner_city.getSelectedItemPosition());
        outState.putString(WEATHER_VIEW, viewShowWeather.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        savePrefs();
        super.onPause();
    }

    @Override
    protected void onResume() {
        loadPrefs();
        super.onResume();
    }

    private void savePrefs() {
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(APP_PREFFERENCES_CITY, spinner_city.getSelectedItemPosition());
        editor.putString(APP_PREFFERENCES_LASTMSG, viewShowWeather.getText().toString());
        editor.putBoolean(APP_PREFFERENCES_ISPRESSURE, checkBoxPressure.isChecked());
        editor.putBoolean(APP_PREFFERENCES_ISTOMORROW, checkBoxTomorrow.isChecked());
        editor.putBoolean(APP_PREFFERENCES_ISWEEK, checkBoxWeek.isChecked());
        editor.apply();
    }

    private void loadPrefs() {
        if (settings.contains(APP_PREFFERENCES_ISPRESSURE))
            checkBoxPressure.setChecked(settings.getBoolean(APP_PREFFERENCES_ISPRESSURE, false));
        if (settings.contains(APP_PREFFERENCES_ISTOMORROW))
            checkBoxTomorrow.setChecked(settings.getBoolean(APP_PREFFERENCES_ISTOMORROW, false));
        if (settings.contains(APP_PREFFERENCES_ISWEEK))
            checkBoxWeek.setChecked(settings.getBoolean(APP_PREFFERENCES_ISWEEK, false));
        if (settings.contains(APP_PREFFERENCES_CITY)){
            int pos = settings.getInt(APP_PREFFERENCES_CITY, 0);
            spinner_city.setSelection(pos);
            if (!resulRecieved) {
                viewShowWeather.setText(settings.getString(APP_PREFFERENCES_LASTMSG, APP_PREFFERENCES_LASTMSG));
            }
        }
    }
}
