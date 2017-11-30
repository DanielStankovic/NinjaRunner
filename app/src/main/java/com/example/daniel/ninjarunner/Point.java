package com.example.daniel.ninjarunner;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Point {



    public static class Factory{

        Context context;
        int gameWidth;
        int gameHeight;
        Bitmap btm;
        private int pointWidth;
        private int pointHeight;

        private static final int POINT_SIZE = 80;

        public Factory(Context context, int gameWidth, int gameHeight){

            this.context = context;
            this.gameHeight = gameHeight;
            this.gameWidth = gameWidth;

            int bitmapResourceId = context.getResources().getIdentifier("coin", "drawable", context.getPackageName());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), bitmapResourceId, options);

            int imageWidth = options.outWidth;
            int imageHeight = options.outHeight;

            float ratio = (float)imageWidth / (float)imageHeight;

            pointWidth = POINT_SIZE;
            pointHeight = (int) (POINT_SIZE/ ratio);

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), bitmapResourceId);
            btm = Bitmap.createScaledBitmap(bitmap, pointWidth,pointHeight, false);
        }

        public Point createPoint(){

            return new Point (context,gameWidth, gameHeight, btm );
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

    private Point(Context context, int gameWidth, int gameHeight, Bitmap bitmap){

        this.context = context;
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;

        this.bitmap = bitmap;

        bitmapPosition = new Rect(gameWidth, (int) (gameHeight * 0.45) - bitmap.getHeight(), gameHeight + bitmap.getWidth(),(int) (gameHeight * 0.45) );
    }

    int x = 0;
    boolean outOfBounds = false;

    public void update(float gameSpeed){

        if(x <= gameWidth + bitmap.getWidth()){
            x = x + (int)(20*gameSpeed);
            bitmapPosition.left = gameWidth - x;
            bitmapPosition.right = bitmap.getWidth() + (gameWidth - x);
        }else {
            outOfBounds = true;
        }
    }
}
