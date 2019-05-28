package com.southstate.orange.util;

import android.net.Uri;

/**
 * Created by junkang on 2019/5/10.
 * Function: J2V8Demo
 */
public class PageUri {

    public final String url;
    public final String host;
    public final String schema;
    public final int port;

    public PageUri(String url) {
        this.url = url;
        Uri uri = Uri.parse(url);
        this.host = uri.getHost();
        this.schema = uri.getScheme();
        this.port = uri.getPort();
    }
}
