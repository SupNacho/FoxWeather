package fw.supernacho.ru.foxweather;

import android.content.Context;

class Prediction {
    static String predict(Context context, int position){
        String[] temps = context.getResources().getStringArray(R.array.cities_temps);
        return temps[position];
    }

}
