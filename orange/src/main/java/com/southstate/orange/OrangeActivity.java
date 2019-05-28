package com.southstate.orange;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class OrangeActivity extends AppCompatActivity {

    private static final int REQ_START_PAGE = 1001;

    private OrangeView mOrangeView;
    private View progressBar;
    private View errorPanel;

    public static void launch(Context context, String url, String paramsJson) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(context, OrangeActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("params", paramsJson);
        if ("true".equalsIgnoreCase(uri.getQueryParameter("clearTask"))) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        ((Activity) context).startActivityForResult(intent, REQ_START_PAGE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_orange);
        mOrangeView = findViewById(R.id.orange_view);
        mOrangeView.setOnLoadProgressListener(new OrangeView.OnLoadProgressListener() {
            @Override
            public void onLoadStart() {
                progressBar.setVisibility(View.VISIBLE);
                errorPanel.setVisibility(View.GONE);
            }

            @Override
            public void onLoadEnd(Throwable error) {
                progressBar.setVisibility(View.GONE);
                errorPanel.setVisibility(error == null ? View.GONE : View.VISIBLE);
                if (error != null) {
                    error.printStackTrace();
                }
            }
        });
        mOrangeView.setOnFinishActivityListener(resultJson -> {
            setResult(RESULT_OK, new Intent().putExtra("data", resultJson));
            finish();
        });

        progressBar = findViewById(R.id.progress_bar);
        errorPanel = findViewById(R.id.pane_error);
        findViewById(R.id.btn_error_retry).setOnClickListener(v -> reload());

        TextView tvErrorUrl = findViewById(R.id.tv_error_url);
        String url = getIntent().getStringExtra("url");
        tvErrorUrl.setText(url);

        reload();
    }

    private boolean reload() {
        String url = getIntent().getStringExtra("url");
        String params = getIntent().getStringExtra("params");
        mOrangeView.loadUrl(url, params);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQ_START_PAGE) {
            if (resultCode == RESULT_OK && data != null) {
                String resultJson = data.getStringExtra("data");
                mOrangeView.setPageResult(resultJson);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrangeView.onShow();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mOrangeView.onHide();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mOrangeView.onClose();
        mOrangeView = null;
        System.gc();
    }
}
