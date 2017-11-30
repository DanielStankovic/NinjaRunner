package com.example.daniel.ninjarunner;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }

public void playGame(View v){
    Intent intent = new Intent(this, PlayGame.class);
    startActivity(intent);
}
public void options(View v){

    Intent intent = new Intent(this, OptionsActivity.class);
    startActivity(intent);
}

}
