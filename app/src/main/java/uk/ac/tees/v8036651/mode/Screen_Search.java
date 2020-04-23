package uk.ac.tees.v8036651.mode;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

public class Screen_Search extends AppCompatActivity {
    Button openOverflowButton;
    Button openGoogleButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_search);

        //Custom Tab Shinanigans
        openOverflowButton = findViewById(R.id.btnSearchOverflow);
        openGoogleButton = findViewById(R.id.btnSearchGoogle);

        final String overflowUrl = "https://stackoverflow.com/";
        final String googleUrl = "https://www.google.com/";

        openOverflowButton.setOnClickListener((v) -> {openCustomTab(overflowUrl);});
        openGoogleButton.setOnClickListener((v) -> {openCustomTab(googleUrl);});
    }

    //Custom Tab Shinanigans
    void openCustomTab(String url)
    {
        // Use CustomeTabsIntent.Builder to configure CustomTabsIntent
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        // set toolabr color and/or setting custom actions before invoking build()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        builder.addDefaultShareMenuItem();
        // Once ready, call CustomTabsIntent.Builder.build() to create a CustomTabsIntent
        CustomTabsIntent customeTabsIntent = builder.build();
        //and launch the desired url with CustomeTabsIntent.launchUrl()
        customeTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
