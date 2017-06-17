package com.example.user.zippo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebFragment extends Fragment
{
    private WebView mWebView;
    private ProgressBar mProgressBarWeb;
    private static final String APP_CACHE_DIRNAME = "/webcache"; // web缓存目录

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mProgressBarWeb = (ProgressBar)view.findViewById(R.id.progressBarWeb);

        String urlString = getArguments().getString("url");

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        mProgressBarWeb.setVisibility(View.VISIBLE);

        mWebView.setWebViewClient(new WebViewClient()
        {
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url)
            {
                mProgressBarWeb.setVisibility(View.GONE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
            {
                // 載入錯誤網址
            }
        });
        mWebView.loadUrl(urlString);

        return view;
    }

}

