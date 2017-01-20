package com.singleuseapps.ffffff;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by stefankoopman on 13/11/16.
 */

public class AlteTextView extends TextView {

    public AlteTextView(Context context) {
        super(context);
        setAlte();
    }

    public AlteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAlte();
    }

    public AlteTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAlte();
    }

    void setAlte(){
        Typeface typeface = Typeface.createFromAsset(getResources().getAssets(),"fonts/din1451alt.ttf");
        setTypeface(typeface);
    }
}
