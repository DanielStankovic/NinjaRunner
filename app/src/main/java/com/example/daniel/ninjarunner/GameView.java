package com.example.daniel.ninjarunner;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    //initial game speed
    private float gameSpeed = 0.5f;

    Context context;

    SurfaceHolder surfaceHolder;

    Thread gameThread;

    Paint paint;
    Canvas canvas;

    private int gameWidth;
    private int gameHeight;

    Runner runner;
    Ground ground;
    Obstacle.Factory obstacleFactory;
    List<Obstacle> obstacles;
    Point.Factory pointFactory;
    List<Point> points;


    boolean running;
    boolean gameOver = false;

    int score = 0;
    int newScore = 0;


    public GameView(Context context, int gameWidth, int gameHeight) {
        super(context);

        this.context = context;
        this.gameHeight = gameHeight;
        this.gameWidth = gameWidth;

        surfaceHolder = getHolder();
        surfaceHolder.setFixedSize(gameWidth, gameHeight);

        paint = new Paint();

        runner = new Runner(context, gameWidth, gameHeight);
        ground = new Ground(context, gameWidth, gameHeight);

        obstacles = new ArrayList<>();
        obstacleFactory = new Obstacle.Factory(context, gameWidth, gameHeight);
        points = new ArrayList<>();
        pointFactory = new Point.Factory(context, gameWidth,gameHeight);

    }

    long startTime;
    long endTime;
    public static final int TARGET_FPS = 60;

    @Override
    public void run() {

        while(running){

            startTime = System.currentTimeMillis();
            update();

            draw();
            endTime = System.currentTimeMillis();

            long delta = endTime - startTime;

            long sleepTime = (int)((1000/TARGET_FPS) - delta);

            if(sleepTime < 0){
                sleepTime = 0;
            }

            try{
                Thread.sleep(sleepTime);
            }catch (Exception e){
                Log.e("exception", e.toString());
            }
        }

    }

    private int frameCounter = 0;
    private int pointFrameCounter = 0;

    int control = 0;
    int pointControl = 0 ;

    Random random = new Random();

    public void update(){

        gameSpeed = gameSpeed + 0.0002f;

        if(gameSpeed > 1.5f){
            gameSpeed = 1.5f;
        }

        score = score + 1 ;
        runner.update();
        ground.update(gameSpeed);

        if(control == 0){
            control = random.nextInt((int)(1.2 * gameWidth / (20 * gameSpeed))) + gameWidth / 2 / (int)(20 * gameSpeed);
        }
        if(frameCounter == control){

            Obstacle obstacle = obstacleFactory.createRandomObstacle();
            obstacles.add(obstacle);

            control = 0;
            frameCounter = 0;
        }
        if(pointControl == 0){
            pointControl = random.nextInt((int)(1.2 * gameWidth / (30 * gameSpeed))) + gameWidth / 2 / (int)(30 * gameSpeed);
        }
        if(pointFrameCounter == pointControl){


            Point point = pointFactory.createPoint();
            points.add(point);

            pointControl = 0;
            pointFrameCounter = 0;
        }

        for (int i = 0; i <obstacles.size() ; i++) {
            if(obstacles.get(i) != null && !obstacles.get(i).outOfBounds){
                obstacles.get(i).update(gameSpeed);

            if(Collision.detectCollisionObstacle(runner, obstacles.get(i))){
                gameOver = true;
            }
            }else {
                obstacles.remove(i);
            }
        }

        for (int i = 0; i <points.size() ; i++) {
            if(points.get(i) != null && !points.get(i).outOfBounds){
                points.get(i).update(gameSpeed);

                if(Collision.detectCollisionPoint(runner, points.get(i))){

              score+=200;




                    points.remove(i);

                }
            }
        }


            frameCounter++;
        pointFrameCounter++;
    }

    public void draw(){

        if(surfaceHolder.getSurface().isValid()){

            //ovom linijom pravimo od canvas objekta podlogu za crtanje
            canvas = surfaceHolder.lockCanvas();
            // brisemo sve sa ekrana
            canvas.drawColor(Color.parseColor("#FF8093FF"));

            //prvo crtamo pozadinske layere tj zemlju posto je ninja preko zemlje
            canvas.drawBitmap(ground.getFinalBitmap(),null, ground.getGroundPosition(), null);

            for (int i = 0; i <obstacles.size() ; i++) {
                if(obstacles.get(i)!= null && !obstacles.get(i).outOfBounds){
                    canvas.drawBitmap(obstacles.get(i).getBitmap(),null, obstacles.get(i).getBitmapPosition(),null);
                }
            }

            for (int i = 0; i <points.size() ; i++) {
                if(points.get(i)!= null && !points.get(i).outOfBounds){
                    canvas.drawBitmap(points.get(i).getBitmap(),null, points.get(i).getBitmapPosition(),null);
                }
            }

            canvas.drawBitmap(runner.getBitmap(), runner.getCurrentSprite(), runner.getSpritePosition(), null);

            paint.setTextSize(30);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);




            canvas.drawText("Score: " + score, 20,40,paint);


            if(gameOver){
                canvas.drawColor(Color.argb(130, 16, 16, 16));
                paint.setColor(Color.WHITE);
                paint.setTextSize(50);
                drawVerticallyCenteredText(canvas, paint, "GAME OVER", -20);
                paint.setTextSize(30);
                drawVerticallyCenteredText(canvas, paint, "tap to restart", 20);
                running = false;
            }

            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }
    public static void drawVerticallyCenteredText(Canvas canvas, Paint paint, String text, int verticalOffset){

        Rect bound = new Rect();
        paint.getTextBounds(text, 0, text.length(), bound);

        int x = (canvas.getWidth()/ 2) - (bound.width()/2);
        int y = (canvas.getHeight()/ 2) - (bound.height()/2 - verticalOffset);
        paint.setAntiAlias(true);
        canvas.drawText(text, x, y, paint);
    }
    public void pause(){

        running = false;
        try{
            gameThread.join();

        }catch (InterruptedException e){
            Log.e("ext", e.getMessage() );
        }
    }

    public void resume(){
        running = true;
        gameThread = new Thread(this);
        gameThread.start();

    }

    public boolean onTouchEvent(MotionEvent motionEvent){
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                break;

            case MotionEvent.ACTION_DOWN:
                if(!gameOver){
                    runner.jump();
                }else {
                    restartGame();
                }

        }
        return true;
    }

    private void restartGame() {

        //erase all obstacles
        obstacles = new ArrayList<>();
        points = new ArrayList<>();
        gameSpeed = 0.5f;
        frameCounter = 0;
        control = 0;
        running = true;
        gameOver = false;
        score = 0;
        newScore = 0;
        resume();

    }
}
