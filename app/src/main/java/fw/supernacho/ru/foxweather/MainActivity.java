package fw.supernacho.ru.foxweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    public static final String APP_PREFFERENCES = "mysettings";
    public static final String APP_PREFFERENCES_CITY = "city";
    public static final String APP_PREFFERENCES_LASTMSG = "last_msg";
    public static final String CITY_ID = "city_id";
    public static final String CITY_NAME = "city_name";

    private Spinner spinner_city;
    private TextView viewShowWeather;
    private SharedPreferences settings;
    private boolean resulRecieved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(APP_PREFFERENCES_CITY, spinner_city.getSelectedItemPosition());
        editor.putString(APP_PREFFERENCES_LASTMSG, viewShowWeather.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (settings.contains(APP_PREFFERENCES_CITY)){
            int pos = settings.getInt(APP_PREFFERENCES_CITY, 0);
            spinner_city.setSelection(pos);
            if (!resulRecieved) {
                viewShowWeather.setText(settings.getString(APP_PREFFERENCES_LASTMSG, APP_PREFFERENCES_LASTMSG));
            }
        }
    }
}
