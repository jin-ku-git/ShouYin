package com.youwu.shouyin.utils_view;

import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class EditTextUtils {
    /**
     * Judge what situation hide the soft keyboard,click EditText view should
     * show soft keyboard
     *
     * @param view
     *            Incident event
     * @param event
     * @return
     */
    public static boolean isShouldHideSoftKeyBoard(View view, MotionEvent event) {
        if (view != null && (view instanceof EditText)) {
            int[] l = { 0, 0 };
            view.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + view.getHeight(), right = left + view.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // If click the EditText event ,ignore it
                return false;
            } else {
                return true;
            }
        }
        // if the focus is EditText,ignore it;
        return false;
    }

    /**
     * hide soft keyboard
     *
     * @param token
     */
    public static void hideSoftKeyBoard(IBinder token, Context context) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}

