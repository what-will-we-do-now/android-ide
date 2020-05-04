package uk.ac.tees.cis2003.froyo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class Screen_QR extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(getApplicationInfo().theme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_qr);
    }
}
