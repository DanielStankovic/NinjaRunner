package com.example.daniel.ninjarunner;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.Random;

public class Ground {

    private Context context;

    private int gameWidth;
    private int gameHeight;

    private int spriteWidth;
    private int spriteHeight;

    //initialground ce biti loadovana iz png fajla
    //ova druga je verovatno ono sto se prikazuje????
    private Bitmap initialGround;
    private Bitmap finalBitmap;


    //sluzi za cuvanje info o poziciji grounda
    private Rect groundPosition;

    public Bitmap getFinalBitmap() {
        return finalBitmap;
    }

    public Rect getGroundPosition() {
        return groundPosition;
    }

    public Ground(Context context, int gameWidth, int gameHeight){

        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;
        this.context = context;


        spriteWidth = gameWidth / 10;
        spriteHeight = (int)(gameHeight * 0.4);

        //ovde se kreira i finalni bitmap grounda koji je zapravo dve duzine ekrana
        finalBitmap = createNewDoubleBitmap();
    }

    //ova varijabla sluzi za updejtovanje gorund position
    private int offset = 0;

    public void update(float gameSpeed){

        if(Math.abs(offset) >= gameWidth){
            finalBitmap = createNewDoubleBitmap();
            offset = gameWidth - Math.abs(offset) - (int)(20* gameSpeed);
        }else{
            offset = offset - (int)(20* gameSpeed);
        }
        groundPosition = new Rect(offset, (int) (gameHeight * 0.55), 2* gameWidth + offset, gameHeight);
    }

    private Bitmap previousPart = null;

    private Bitmap createNewDoubleBitmap(){

        Bitmap bmOverlay = Bitmap.createBitmap(2 * gameWidth, spriteHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);

        if(previousPart == null){

            initialGround = BitmapFactory.decodeResource(context.getResources(), R.drawable.ground);
            initialGround = Bitmap.createScaledBitmap(initialGround, gameWidth, (int)(gameHeight * 0.4), false);
            canvas.drawBitmap(initialGround, null, new Rect(0,0,gameWidth, spriteHeight), null);
        }else{
            canvas.drawBitmap(previousPart, null, new Rect(0,0,gameWidth, spriteHeight), null);
        }

        // gde se crta drugo deo
        Rect whereToDraw = new Rect(gameWidth, 0,gameWidth * 2, spriteHeight);

        //generate random second part
        Bitmap newPart = createRandomGrassBitmap();

        //sada sacuvavamo drugi deo  za sledeci frejm posto ce da postane prvi  WTF???

        previousPart = newPart.copy(newPart.getConfig(), true);

        //na kraju spajamo sada 2 bitmapa u jedan finalni dupli bitmap
        canvas.drawBitmap(previousPart, null, whereToDraw, null);
        return bmOverlay;
    }

    private Bitmap createRandomGrassBitmap(){

        Bitmap bmOverlay = Bitmap.createBitmap(gameWidth, spriteHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmOverlay);
        for (int i = 0; i < 10; i++) {

            Random random =  new Random();
            int rndNum = random.nextInt(10);

            //rect objekat da isece samo jedan deo od 10 iz spritesheeta
            Rect singleRndSprite = new Rect(rndNum * spriteWidth, 0, (rndNum + 1)* spriteWidth, spriteHeight);

            Rect whereToDraw = new Rect(spriteWidth * i, 0, (spriteWidth * i) + spriteWidth, spriteHeight);

            //draw sprite on the bitmap
            canvas.drawBitmap(initialGround, singleRndSprite, whereToDraw, null);

        }
        return  bmOverlay;
    }
}
