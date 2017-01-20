package com.singleuseapps.ffffff;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int colorIndex = 0;
    private ArrayList<Integer> color = new ArrayList<>();
    private FrameLayout frameLayout;
    private TextView textColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen);

        fillColors();

        textColor = (TextView)findViewById(R.id.text_color);


        frameLayout = (FrameLayout)findViewById(R.id.layout);
        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setNextColor();
            }
        });
    }

    private void fillColors(){
        color = new ArrayList<>();
            color.add(Color.parseColor("#ffffff"));
            color.add(Color.parseColor("#33aaff"));
            color.add(Color.parseColor("#ffaa33"));
            color.add(Color.parseColor("#aaff33"));
            color.add(Color.parseColor("#33ffaa"));
            color.add(Color.parseColor("#ff33aa"));
            color.add(Color.parseColor("#aa33ff"));
    }

    private void setNextColor(){
        colorIndex += 1;
        if(colorIndex >= color.size()){
            colorIndex = 0;
        }else{
            Random rnd = new Random();
            int colorint = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            color.add(colorint);
        }
        frameLayout.setBackgroundColor(color.get(colorIndex));

        int bgColor = color.get(colorIndex);
        int text = getComplimentColor(bgColor);

        textColor.setTextColor(text);
        textColor.setText(String.format("#%06X", (0xFFFFFF & color.get(colorIndex))));

    }

    public static int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }

}
