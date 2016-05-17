package com.udacity.capstone.utils;

import android.content.Context;
import android.content.res.TypedArray;

import com.udacity.capstone.R;

/**
 * Created by chyupa on 10-May-16.
 */
public class Utils {

    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }
}
