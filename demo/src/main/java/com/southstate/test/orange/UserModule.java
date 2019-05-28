package com.southstate.test.orange;

import com.southstate.orange.annotation.JSField;
import com.southstate.orange.annotation.JSFunction;
import com.southstate.orange.annotation.JSModule;

/**
 * Created by junkang on 2019/5/22.
 * Function: Orange
 */
@JSModule("user")
public class UserModule {

    @JSField("height")
    public double height = 0f;

    @JSFunction("getName")
    public String getName() {
        return "丁俊康";
    }

}
