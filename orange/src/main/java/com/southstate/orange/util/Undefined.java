package com.southstate.orange.util;

/**
 * Created by junkang on 2019/5/28.
 * Function: Orange
 */
public final class Undefined {

    public static final Undefined INSTANCE = new Undefined();

    private Undefined() {
    }

    @Override
    public String toString() {
        return "undefined";
    }
}
