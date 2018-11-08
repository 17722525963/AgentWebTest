package com.yxkj.agentwebtest;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "???";

    private AgentWeb mAgentWeb;

    private ConstraintLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mContainer, new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                .useDefaultIndicator()
                .setAgentWebWebSettings(MySettings())
                .setWebViewClient(mWebViewClient)
                .setWebChromeClient(mWebChromeClient)
                .createAgentWeb()
                .ready()
                .go("https://www.7477.com");


    }


    private IAgentWebSettings MySettings() {

        return new AbsAgentWebSettings() {

            private AgentWeb mAgentWeb;

            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                mAgentWeb = agentWeb;
            }

            @Override
            public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
                return super.setDownloader(webView, DefaultDownloadImpl
                        .create((Activity) webView.getContext(), webView, mDownloadListenerAdapter, mDownloadListenerAdapter, this.mAgentWeb.getPermissionInterceptor()));
            }
        };
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }
    };

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    private DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            extra.setOpenBreakPointDownload(true)
                    .setIcon(R.drawable.ic_file_download_black_24dp)
                    .setConnectTimeOut(6000)
                    .setBlockMaxTime(2000)
                    .setDownloadTimeOut(60L * 5L * 1000L)
                    .setAutoOpen(true)
//                    .setEnableIndicator(false) 设置不显示指示器
                    .setForceDownload(false);
            return false;
        }

        @Override
        public boolean onResult(String path, String url, Throwable e) {
            return super.onResult(path, url, e);
        }

        @Override
        public void onProgress(String url, long downloaded, long length, long usedTime) {
            Log.i(TAG, "onProgress: url" + url + "--downloaded" + downloaded + "--length" + length + "--usedTime" + usedTime);
            super.onProgress(url, downloaded, length, usedTime);
        }

    };
}
