package xu.ferris.cubeeffecttest;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private MySurfaceView mGLSurfaceView;
    private  GLWebView glWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mGLSurfaceView=(MySurfaceView)findViewById(R.id.gl_layout);
        glWebView=(GLWebView)findViewById(R.id.gl_layout_webview);
        glWebView.setViewToGLRenderer(mGLSurfaceView.getRenderer());
        glWebView.setWebViewClient(new WebViewClient());
        glWebView.getSettings().setJavaScriptEnabled(true);
        glWebView.setWebChromeClient(new WebChromeClient());
        glWebView.loadUrl("http://www.qq.com");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }
}
