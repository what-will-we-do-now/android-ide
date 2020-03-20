package uk.ac.tees.v8036651.mode;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Update_Theme {

    public Update_Theme(Context c){
        SharedPreferences pref = c.getSharedPreferences("light_mode", MODE_PRIVATE);
        final String lmSummary = pref.getString("light_mode", "");
        System.out.println("String value of Light Mode is: " + lmSummary);
        System.out.println();
        System.out.println();
        System.out.println();
        if (lmSummary.equals(c.getString(R.string.light_mode_disabled))){
            c.setTheme(R.style.darkTheme);
        }
        else if (lmSummary.equals(c.getString(R.string.light_mode_enabled))){
            c.setTheme(R.style.lightTheme);
        }
    }
}
