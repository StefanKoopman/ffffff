package com.singleuseapps.ffffff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int colorIndex = 0;
    private ArrayList<Integer> color = new ArrayList<>();
    private FrameLayout frameLayout;
    private TextView textColor, mTextClicks;
    private int clicks = 0;
    private SharedPreferences mSharedPref;
    private GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Games.API).addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            }
        }).build();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen);

        buildGoogleApiClient();

        mSharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        clicks = mSharedPref.getInt("clicks",0);

        fillColors();

        textColor = (TextView)findViewById(R.id.text_color);
        mTextClicks = (TextView)findViewById(R.id.text_clicks);

        frameLayout = (FrameLayout)findViewById(R.id.layout);
        Button mButtonLeaderBords = (Button)findViewById(R.id.button_leaderbord);
        mButtonLeaderBords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mGoogleApiClient.isConnected()){
                    /*startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                            "CgkI3LHE-JoJEAIQAQ"), 1337);*/

                    startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),1000);

                }
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clicks += 1;
                mTextClicks.setText(clicks+"");

                if(mGoogleApiClient.isConnected()) {
                    if (clicks == 50) {
                        Games.Achievements.unlock(mGoogleApiClient, "CgkI3LHE-JoJEAIQAg");
                    } else if (clicks == 16581375) {
                        Games.Achievements.unlock(mGoogleApiClient, "CgkI3LHE-JoJEAIQBw");
                    }
                }


                setNextColor();
            }
        });

    }


    @Override
    protected void onPause() {
        SharedPreferences.Editor editor = mSharedPref.edit();
        editor.putInt("clicks",clicks);
        editor.apply();
        if(!mGoogleApiClient.isConnected()){
            mGoogleApiClient.connect();
        }
        if(mGoogleApiClient.isConnected()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, "CgkI3LHE-JoJEAIQAQ", clicks);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if(!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
        clicks = PreferenceManager.getDefaultSharedPreferences(this).getInt("clicks",0);
        mTextClicks.setText(clicks+"");


        super.onResume();
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
