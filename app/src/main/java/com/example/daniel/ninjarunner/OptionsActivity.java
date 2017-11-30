package com.example.daniel.ninjarunner;

import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends AppCompatActivity {

     RadioGroup radioGroup;

  public static String clicked = "btn1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                SharedPreferences settings = getSharedPreferences("MAIN_SETTINGS", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt("checkedId", checkedId);
                if(checkedId == R.id.btn1){
                    clicked = "btn1";
                }else{
                    clicked = "btn2";
                }

                editor.apply();
            }
        });

        SharedPreferences settings = getSharedPreferences("MAIN_SETTINGS", 0);
        int checked = settings.getInt("checkedId", R.id.btn1);
        radioGroup.check(checked);
    }
}
