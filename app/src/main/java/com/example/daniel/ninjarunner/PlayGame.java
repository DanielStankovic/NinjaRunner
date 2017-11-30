package com.example.daniel.ninjarunner;

import android.content.pm.ActivityInfo;
import android.graphics.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class PlayGame extends AppCompatActivity {

    GameView gameView;

    FrameLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        Display display = getWindowManager().getDefaultDisplay();

        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);

        double screenRatio = (size.x * 1.0) / (size.y * 1.0);

        gameView = new GameView(this, 800, (int)(800/screenRatio));

        setContentView(R.layout.activity_play_game);

        android.widget.FrameLayout.LayoutParams params = new android.widget.FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        gameView.setLayoutParams(params);

        container = (FrameLayout)findViewById(R.id.main_container);
        container.addView(gameView);

    }


    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
