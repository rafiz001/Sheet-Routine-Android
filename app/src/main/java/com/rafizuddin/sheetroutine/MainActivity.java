package com.rafizuddin.sheetroutine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
public WebView browser;
private SimpleWebServer server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        try {
            server = new SimpleWebServer(this, 7000); // Pass context and port number
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.browser = (WebView) findViewById(R.id.browser);
        WebSettings browserSettings = browser.getSettings();
        browserSettings.setJavaScriptEnabled(true);
        browserSettings.setDomStorageEnabled(true);
        browser.setWebViewClient(new MyWebViewClient());
        browser.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                // Handle the console message here
                Log.d("WebViewConsole", consoleMessage.message() + " -- From line "
                        + consoleMessage.lineNumber() + " of "
                        + consoleMessage.sourceId());
                return true;
            }
        });
        browser.loadUrl("http://localhost:7000/docs/index.html");

/*
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (browser != null && browser.canGoBack()) {
                    browser.goBack(); // Go back in WebView history
                } else {
                    // If no WebView history, call the default back button action
                    finish();
                }
            }
        });

*/
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = request.getUrl().toString();
            return handleUrl(view, url);
        }



        private boolean handleUrl(WebView view, String url) {
            // Check if the URL is a Telegram URI scheme
            if (url.startsWith("tg:resolve")) {
                // Handle the URI scheme
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true; // Indicate that we handled the URL
            }

            // Otherwise, load the URL within the WebView
            view.loadUrl(url);
            return false; // Let the WebView handle the URL
        }
    }






}