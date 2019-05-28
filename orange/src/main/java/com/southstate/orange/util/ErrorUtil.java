package com.southstate.orange.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by junkang on 2019/5/18.
 * Function: J2V8Demo
 */
public class ErrorUtil {

    public static void handleJsError(Context context, Throwable e) {
        e.printStackTrace();

        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        String msg = out.toString();
        new Handler(Looper.getMainLooper()).post(() -> {
            TextView textView = new TextView(context);
            ScrollView scrollView = new ScrollView(context);
            scrollView.addView(textView);
            int p = (int) (context.getResources().getDisplayMetrics().density * 15);
            scrollView.setPadding(p, p, p, p);
            textView.setText(msg);
            new AlertDialog.Builder(context)
                    .setView(scrollView)
                    .setTitle("错误信息")
                    .setPositiveButton("知道了", null)
                    .create()
                    .show();
        });
    }

}
