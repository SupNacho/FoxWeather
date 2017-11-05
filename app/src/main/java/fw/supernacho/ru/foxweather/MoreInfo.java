package fw.supernacho.ru.foxweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MoreInfo extends AppCompatActivity {
    private TextView moreInfo;
    private String cityName;
    private StringBuilder stringBuilder;
    public static final String RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        init();
        getDataFromOtherActivity();
        restoreActivity(savedInstanceState);
    }

    private void getDataFromOtherActivity() {
        Intent intent = getIntent();
        if (intent != null){
            int city = intent.getIntExtra(MainActivity.CITY_ID, 0);
            cityName = intent.getStringExtra(MainActivity.CITY_NAME);
            if (cityName != null) {
                stringBuilder.append(getString(R.string.in)).append(" ")
                        .append(cityName).append("\n");
            }
            stringBuilder.append(Prediction.predict(MoreInfo.this, city));
            moreInfo.setText(stringBuilder.toString());
        }
    }

    private void restoreActivity(Bundle savedInstanceState) {
        if (savedInstanceState != null){
            moreInfo.setText(savedInstanceState.getString(RESULT));
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button_share:
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plane");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, moreInfo.getText().toString());
                    startActivity(shareIntent);
                    returnResultForActivity();
                    break;
                default:
                    throw new RuntimeException("View not found in switch MoreInfo");
            }
        }
    };

    @Override
    public void onBackPressed() {
        returnResultForActivity();
        super.onBackPressed();
    }

    private void returnResultForActivity() {
        Intent intent = new Intent();
        intent.putExtra(RESULT, stringBuilder.toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(RESULT, moreInfo.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private void init(){
        Button buttonShare = findViewById(R.id.button_share);
        moreInfo = findViewById(R.id.textview_more_info);
        stringBuilder = new StringBuilder();
        buttonShare.setOnClickListener(onClickListener);
    }
}
