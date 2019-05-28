package com.southstate.orange.lib;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
import com.eclipsesource.v8.V8Object;
import com.eclipsesource.v8.V8Value;
import com.southstate.orange.context.PageContext;
import com.southstate.orange.util.ErrSafeRunnable;
import com.southstate.orange.util.V8Util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by junkang on 2019/5/14.
 * Function: J2V8Demo
 */
public class LibTimer {

    private final static AtomicInteger TIMER_GENERATOR = new AtomicInteger(0);

    public static void install(PageContext pc) {
        V8 v8 = pc.getV8();
        Context context = pc.getContext();
        Handler jsHandler = pc.getThread().getHandler();

        SparseArray<TimerCallback> timerMap = new SparseArray<>();

        v8.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) != V8Value.V8_FUNCTION) {
                throw new IllegalArgumentException("setTimeout func accepts a function argument");
            }
            V8Function object = (V8Function) v8Array.getObject(0);
            V8Function func = object.twin();
            object.close();

            int delay = 0;
            if (v8Array.length() > 1) {
                delay = v8Array.getInteger(1);
            }

            V8Array arguments = null;
            if (v8Array.length() > 2) {
                arguments = new V8Array(v8);
                for (int i = 2; i < v8Array.length(); i++) {
                    Object value = v8Array.get(i);
                    arguments.push(value);
                    V8Util.close(value);
                }
            }

            V8Object receiver = v8Object.twin();

            int timerId = TIMER_GENERATOR.addAndGet(1);
            TimerCallback timerCallback = new TimerCallback(context, func, receiver, arguments, timerMap, timerId) {
                @Override
                public void call() {
                    try {
                        this.callFunc();
                    } finally {
                        this.remveTimer();
                        this.close();
                    }
                }
            };
            timerMap.put(timerId, timerCallback);
            jsHandler.postDelayed(timerCallback, delay);
            return timerId;
        }, "setTimeout");

        v8.registerJavaMethod((v8Object, v8Array) -> {
            int timerId = v8Array.getInteger(0);
            TimerCallback runnable = timerMap.get(timerId);
            if (runnable != null) {
                runnable.close();
                timerMap.remove(timerId);
                jsHandler.removeCallbacks(runnable);
            }
        }, "clearTimeout");

        v8.registerJavaMethod((v8Object, v8Array) -> {
            if (v8Array.getType(0) != V8Value.V8_FUNCTION) {
                throw new IllegalArgumentException("setTimeout func accepts a function argument");
            }
            V8Function func = ((V8Function) v8Array.getObject(0)).twin();
            int interval = v8Array.getInteger(1);
            V8Object receiver = v8Object.twin();

            V8Array arguments = null;
            if (v8Array.length() > 2) {
                arguments = new V8Array(v8);
                for (int i = 2; i < v8Array.length(); i++) {
                    Object value = v8Array.get(i);
                    arguments.push(value);
                    V8Util.close(value);
                }
            }

            int timerId = TIMER_GENERATOR.addAndGet(1);
            TimerCallback timerCallback = new TimerCallback(context, func, receiver, arguments, timerMap, timerId) {
                @Override
                public void call() {
                    jsHandler.postDelayed(this, interval);
                    this.callFunc();
                }
            };
            timerMap.put(timerId, timerCallback);
            jsHandler.postDelayed(timerCallback, interval);
            return timerId;
        }, "setInterval");

        v8.registerJavaMethod((v8Object, v8Array) -> {
            int timerId = v8Array.getInteger(0);
            TimerCallback runnable = timerMap.get(timerId);
            if (runnable != null) {
                runnable.close();
                timerMap.remove(timerId);
                jsHandler.removeCallbacks(runnable);
            }
        }, "clearInterval");
    }

    private static abstract class TimerCallback extends ErrSafeRunnable {
        private final V8Function function;
        private final V8Object receiver;
        private final V8Array arguments;
        private final SparseArray<TimerCallback> timerMap;
        final int timerId;

        private TimerCallback(Context context, V8Function function, V8Object receiver, V8Array arguments, SparseArray<TimerCallback> timerMap, int timerId) {
            super(context);
            this.function = function;
            this.receiver = receiver;
            this.arguments = arguments;
            this.timerMap = timerMap;
            this.timerId = timerId;
        }

        void callFunc() {
            if (function != null) {
                function.call(receiver, arguments);
            }
        }

        void remveTimer() {
            timerMap.remove(timerId);
        }

        @Override
        protected abstract void call();

        void close() {
            if (function != null) {
                function.close();
            }
            if (receiver != null) {
                receiver.close();
            }
            if (arguments != null) {
                arguments.close();
            }
        }
    }
}
