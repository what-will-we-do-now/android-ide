package uk.ac.tees.v8036651.mode;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class Screen_Search extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_search);
    }

    public void loadStackOverflow (View view)
    {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://stackoverflow.com/");
    }

    public void loadGoogle (View view)
    {
        WebView myWebView = (WebView) findViewById(R.id.webview);
        myWebView.loadUrl("https://www.google.com/");
    }
}
