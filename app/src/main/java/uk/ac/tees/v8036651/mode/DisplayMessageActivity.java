package uk.ac.tees.v8036651.mode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        String username = "";
        String password = "";
        int pWordStart = 0;

        /**
         * Get the intent that started this activity and extract the string.
         * Then Gets message to be broken down.
         */
        Intent intent = getIntent();
        String message= intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        /**
         * Makes username
         */
        for(int i = 0; message.charAt(i) != '|' && i < message.length();i++)
        {
            username = username + message.charAt(i);

            pWordStart++;
        }

        /**
         * Makes password
         */
        for (int i = pWordStart + 1; i < message.length();i++)
        {
            password = password + message.charAt(i);
        }

        /**
         * Capture the layout's textview and set the string and its text.
         */
        TextView textView = findViewById(R.id.textView);
        textView.setText(username);

        /**
         * Capture the layout's textview and set the string and its text.
         */
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setText(password);
    }

    public void Search (View view)
    {
        Uri webpage = Uri.parse("https://www.howtogeek.com/195430/how-to-create-a-strong-password-and-remember-it/");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(webIntent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;

        System.out.println(isIntentSafe);

        if(isIntentSafe)
        {
            startActivity(webIntent);
        }
    }
}
