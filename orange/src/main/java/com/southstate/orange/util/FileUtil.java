package com.southstate.orange.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import com.southstate.orange.Orange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by junkang on 2019/5/8.
 * Function: J2V8Demo
 */
public class FileUtil {

    public static String getStrFromAssets(Context context, String fileName) {
        try (InputStream open = context.getAssets().open(fileName)) {
            return convertToString(open);
        } catch (IOException e) {
            return "";
        }
    }

    public static String convertToString(InputStream open) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream(open.available());
        byte[] bytes = new byte[1024];
        int read = 0;
        while ((read = open.read(bytes)) > 0) {
            out.write(bytes, 0, read);
        }
        return new String(out.toByteArray());
    }

    public static String httpGetString(String urlStr) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            // 设置请求方法，默认是GET
            connection.setRequestMethod("GET");
            // 设置字符集
            connection.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型
            connection.setRequestProperty("Content-Type", "text/javascript; charset=UTF-8");

            if (connection.getResponseCode() == 200) {
                InputStream is = connection.getInputStream();
                return convertToString(is);
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return "";
    }

    public static String getString(String url) throws IOException {
        if (TextUtils.isEmpty(url)) {
            throw new UnknownHostException("url is empty");
        }
        if (url.startsWith("http://") || url.startsWith("https://")) {
            return httpGetString(url);
        }
        if (url.startsWith("assets://")) {
            return getAssetsString(url);
        }
        throw new UnknownHostException("url[" + url + "] is unreachable");
    }

    private static String getAssetsString(String url) throws IOException {
        Uri uri = Uri.parse(url);
        String path = uri.getPath();
        if (path == null) {
            return "";
        }
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        try (InputStream is = Orange.getInstance().getContext().getAssets().open(path)) {
            ByteArrayOutputStream out = new ByteArrayOutputStream(is.available());
            byte[] bytes = new byte[1024];
            int r;
            while ((r = is.read(bytes)) > 0) {
                out.write(bytes, 0, r);
            }
            return new String(out.toByteArray());
        }
    }

    public static Bitmap base64Bitmap(String base64) {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }
}
