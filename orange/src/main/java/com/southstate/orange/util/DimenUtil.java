package com.southstate.orange.util;

import com.southstate.orange.Orange;

/**
 * Created by junkang on 2019/5/17.
 * Function: Orange
 */
public class DimenUtil {

    public static float dot(float px) {
        return px / Orange.getInstance().getDotDensity();
    }

    public static float px(float dot) {
        return dot * Orange.getInstance().getDotDensity();
    }
}
