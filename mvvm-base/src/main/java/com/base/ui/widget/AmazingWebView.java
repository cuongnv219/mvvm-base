package com.base.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.base.R;
import com.base.utils.Logger;

/**
 * ka
 * 08/11/2017
 */

public class AmazingWebView extends RelativeLayout {

    private static final Logger LOGGER = Logger.getLogger(AmazingWebView.class);

    private WebView webView;
    private View progressBar;
    private View mCustomView;
    private FrameLayout mCustomViewContainer;

    private WebChromeClient.CustomViewCallback mCustomViewCallback;

    public AmazingWebView(Context context) {
        super(context);
        setLayout();
        initCompoundView();
        initListener();
    }

    public AmazingWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayout();
        initCompoundView();
        initListener();
    }

    public WebView getWebView() {
        return webView;
    }

    private void setLayout() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.layout_amazing_web_view, this, true);
    }

    private void initCompoundView() {
        webView = findViewById(R.id.web_view_real);
        progressBar = findViewById(R.id.progress_bar_waiting);
        mCustomViewContainer = findViewById(R.id.fullscreen_custom_content);

        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());

        // Configure the webview
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setJavaScriptEnabled(true);
    }

    private void initListener() {
        mCustomViewContainer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if ((mCustomView == null) && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return onKey(v, keyCode, event);
            }
        });
    }

    /**
     * Loads the given URL.
     *
     * @param url the URL of the resource to load
     */
    public void loadUrl(String url) {
        webView.loadUrl(url);
        LOGGER.info("Load url: " + url);
    }

    private class MyWebChromeClient extends WebChromeClient {
        private Bitmap mDefaultVideoPoster;
        private View mVideoProgressView;

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // Logger.i(LOGTAG, "here in on ShowCustomView");
            webView.setVisibility(View.GONE);

            // if a view already exists then immediately terminate the new one
            if (mCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }

            mCustomViewContainer.addView(view);
            mCustomView = view;
            mCustomViewCallback = callback;
            mCustomViewContainer.setVisibility(View.VISIBLE);
        }

        @Override
        public void onHideCustomView() {

            if (mCustomView == null) {
                return;
            }

            // Hide the custom view.
            mCustomView.setVisibility(View.GONE);

            // Remove the custom view from its container.
            mCustomViewContainer.removeView(mCustomView);
            mCustomView = null;
            mCustomViewContainer.setVisibility(View.GONE);
            mCustomViewCallback.onCustomViewHidden();

            webView.setVisibility(View.VISIBLE);
            // Logger.i(LOGTAG, "set it to webVew");
        }

        @Override
        public Bitmap getDefaultVideoPoster() {
            // Logger.i(LOGTAG, "here in on getDefaultVideoPoster");
            if (mDefaultVideoPoster == null) {
                mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), R.drawable.ic_back);
//                mDefaultVideoPoster = BitmapFactory.decodeResource(getResources(), R.drawable.default_video_poster);
            }
            return mDefaultVideoPoster;
        }

        @Override
        public View getVideoLoadingProgressView() {
            LOGGER.info("here in on getVideoLoadingPregressView");

            if (mVideoProgressView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                mVideoProgressView = inflater.inflate(R.layout.video_loading_progress, null);
            }
            return mVideoProgressView;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
//            getContext().getWindow().setFeatureInt(Window.FEATURE_PROGRESS, newProgress * 100);
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LOGGER.debug("shouldOverrideUrlLoading: " + url);
            // don't override URL so that stuff within iframe can work properly
            // view.loadUrl(url);

//            if (url.contains(URL_TEMP)) {
//                String placeId = url.substring(URL_TEMP.length(), url.length() - 1);
//                LogUtil.d("place id: " + placeId);
//                Intent intent = new Intent(getBaseContext(), PlaceDetailActivity.class);
//                intent.putExtra(BaseDetailActivity.DETAIL_ID, Integer.parseInt(placeId));
//                startActivity(intent);
//                return true;
//            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
        }
    }

}
