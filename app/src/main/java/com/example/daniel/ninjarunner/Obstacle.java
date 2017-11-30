package com.example.daniel.ninjarunner;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class Obstacle {


    public static class Factory{

        private static Bitmap[] bitmaps;
        Context context;
        int gameWidth;
        int gameHeight;


        private int obstacleHeight;
        private int obstacleWidth;


        private static final int OBSTACLE_1_SIZE = 110;
        private static final int OBSTACLE_2_SIZE = 70;
        private static final int OBSTACLE_3_SIZE = 70;

        private int OBSTACLE_INDEX;

        public Factory(Context context, int gameWidth, int gameHeight){

            this.context = context;
            this.gameWidth = gameWidth;
            this.gameHeight = gameHeight;

            bitmaps = new Bitmap[3];


            for (int i = 0; i < 3; i++) {

                int bitmapResourceId = context.getResources().getIdentifier("weapon" + (i +1),"drawable", context.getPackageName());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeResource(context.getResources(), bitmapResourceId, options);

                int imageHeight = options.outHeight;
                int imageWidth = options.outWidth;
                float ratio = (float)imageWidth / (float)imageHeight;

                if(i == 0){
                    obstacleWidth = OBSTACLE_1_SIZE - 10; //kunai treba da bude kraci
                    obstacleHeight = (int)(OBSTACLE_1_SIZE/ratio);
                } else if(i == 1){
                    obstacleWidth = OBSTACLE_2_SIZE;
                    obstacleHeight = (int)(OBSTACLE_2_SIZE/ratio);
                }else if(i == 2){
                    obstacleWidth = OBSTACLE_3_SIZE;
                    obstacleHeight = (int)(OBSTACLE_3_SIZE/ratio);
                }

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResourceId);
                bitmaps[i] = Bitmap.createScaledBitmap(bitmap, obstacleWidth, obstacleHeight, false);
            }
        }
        public Obstacle createRandomObstacle(){
            Random random = new Random();
            OBSTACLE_INDEX = random.nextInt(3);
            Bitmap bitmap = bitmaps[OBSTACLE_INDEX];
            return new Obstacle(context, gameWidth, gameHeight, bitmap);
        }
    }

    private Context context;
    private int gameWidth;
    private int gameHeight;

    private Bitmap bitmap;
    private Rect bitmapPosition;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public Rect getBitmapPosition() {
        return bitmapPosition;
    }

    private Obstacle(Context context, int gameWidth, int gameHeight, Bitmap bitmap){

        this.context = context;
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;

        this.bitmap = bitmap;

        bitmapPosition = new Rect(gameWidth, (int) (gameHeight *0.9) - bitmap.getHeight(), gameWidth + bitmap.getWidth(), (int)(gameHeight * 0.9));

    }

    int x = 0;
    boolean outOfBounds = false;

    public void update(float gameSpeed){
        if(x <= gameWidth + bitmap.getWidth()){
            x = x +(int)(20* gameSpeed);

            bitmapPosition.left = gameWidth -x;
            bitmapPosition.right = bitmap.getWidth() + (gameWidth - x);

        }else {
            outOfBounds = true;
        }


    }
}
