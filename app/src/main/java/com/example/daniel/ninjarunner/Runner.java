package com.example.daniel.ninjarunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.widget.RadioButton;


public class Runner {

    private Bitmap runSpritesheet;
    private Bitmap jumpSpritesheet;

    String clicked = OptionsActivity.clicked;


    public Bitmap getBitmap(){

        if(running){
            return runSpritesheet;
        } else{
            return jumpSpritesheet;
        }
    }

    private int currentSpriteIndex;
    private Rect currentSprite;
    private Rect spritePosition;

    public Rect getCurrentSprite(){return currentSprite;}
    public Rect getSpritePosition(){return spritePosition;}


    private int gameWidth;
    private int gameHeight;

    private boolean running;


    private static final int BMP_COLUMNS_RUN = 5;
    private static final int BMP_COLUMNS_JUMP = 2;

    private int spriteWidth = 0;
    private int spriteHeight = 0;

    private int stepJumpIn;
    private int stepJumpOut;

    public Runner(Context context, int gameWidth, int gameHeight){

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;


// velicina jednog sprite, tj runnera
        spriteHeight = (int) (gameHeight / 3.6);
        spriteWidth = (int) (spriteHeight / 1.33);

        String clicked = OptionsActivity.clicked;

if(clicked.equals("btn1")) {
    runSpritesheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.running);
    runSpritesheet = Bitmap.createScaledBitmap(runSpritesheet, spriteWidth * BMP_COLUMNS_RUN, spriteHeight, false);

    jumpSpritesheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.jumping);
    jumpSpritesheet = Bitmap.createScaledBitmap(jumpSpritesheet, spriteWidth * BMP_COLUMNS_JUMP, spriteHeight, false);
}else if(clicked.equals("btn2")) {
    runSpritesheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl_running);
    runSpritesheet = Bitmap.createScaledBitmap(runSpritesheet, spriteWidth * BMP_COLUMNS_RUN, spriteHeight, false);

    jumpSpritesheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.girl_jump);
    jumpSpritesheet = Bitmap.createScaledBitmap(jumpSpritesheet, spriteWidth * BMP_COLUMNS_JUMP, spriteHeight, false);
}


        currentSpriteIndex = 0;


        currentSprite = new Rect(0,0, spriteWidth, spriteHeight);
        spritePosition = new Rect((int)(gameWidth * 0.1), gameHeight - spriteHeight - (int)(gameHeight * 0.1), spriteWidth + (int)(gameWidth * 0.1), gameHeight - (int)(gameHeight * 0.1));

        running = true;

        stepJumpIn = (gameHeight /3)/ 10;     //ovo definise kolko visoko ce da skoci
        stepJumpOut = (gameHeight /3)/ 8;    // ovo definise kolko ce da se spusta

    }
    //broj ukupnih frejmova
    private int frame_counter = 0;

    //broj frejmova pri skoku
    private int jump_frame_counter = 0;

    //updateuje poziciju runnera
    public void update(){

        if(running){

            if(frame_counter % (5) == 0){
                if(currentSpriteIndex < 4){
                    currentSpriteIndex++;
                }else {
                    currentSpriteIndex = 0;
                }
            }
            currentSprite.left = currentSpriteIndex * spriteWidth;
            currentSprite.right = currentSprite.left + spriteWidth;

            spritePosition = new Rect ((int) (gameWidth * 0.1), gameHeight - spriteHeight - (int)(gameHeight * 0.1), spriteWidth + (int)(gameWidth * 0.1), gameHeight - (int)(gameHeight * 0.1));

            //ako skace
        }else{
            if(jump_frame_counter < 10){

                //ovo je prvi jump sprite kad se uzdize
                currentSprite.left = 0;
                currentSprite.top = 0;
                currentSprite.right = spriteWidth;
                currentSprite.bottom = spriteHeight;

                // ovo je kad skace

                spritePosition.left = (int)(gameWidth * 0.1);
                spritePosition.top = gameHeight - spriteHeight - (stepJumpIn * jump_frame_counter) - (int)(gameHeight * 0.1);
                spritePosition.right = spriteWidth + (int)(gameWidth * 0.1);
                spritePosition.bottom = gameHeight - (stepJumpIn * jump_frame_counter) - (int)(gameHeight * 0.1);
            } else if(jump_frame_counter >= 10 && jump_frame_counter < 15){
                //ovde cekamo 5 frejmova

            }else if(jump_frame_counter >= 15 && jump_frame_counter < 23){

                //ovo je drugi jump sprite kad se spusta
                currentSprite.left = spriteWidth;
                currentSprite.right = currentSprite.left + spriteWidth;

                //ovo je spustanje runnera na dole
                spritePosition = new Rect ((int)(gameWidth * 0.1), gameHeight - spriteHeight - (stepJumpIn *10) -(int)(gameHeight * 0.1) + ((jump_frame_counter - 15)* (stepJumpOut)), spriteWidth + (int) (gameWidth * 0.1), gameHeight - (stepJumpIn * 10) - (int) (gameHeight * 0.1) + ((jump_frame_counter - 15) * stepJumpOut));

                //zavrsio se skok i vraca se na trcanje
            }else{
                running = true;
                jump_frame_counter = -1;
                frame_counter = -1;
                currentSpriteIndex = 0;

            }
            jump_frame_counter ++;  //ovde unutar jump bloka se updateuje ovaj counter


        }
            frame_counter++; // ovde unutar running bloka se updateuje globalni counter

    }

    public void jump(){
        running = false;
    }


}
