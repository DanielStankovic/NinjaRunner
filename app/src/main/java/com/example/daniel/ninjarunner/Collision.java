package com.example.daniel.ninjarunner;


import android.graphics.*;

public class Collision {

    public static boolean detectCollisionObstacle(Runner runner, Obstacle obstacle){

        Rect runnerPosition = runner.getSpritePosition();
        Rect originalObstaclePosition = obstacle.getBitmapPosition();

        Rect obstaclePosition = new Rect();
        obstaclePosition.top = (int)(originalObstaclePosition.top * 1.1);
        obstaclePosition.left = (int)(originalObstaclePosition.left * 1.2);
        obstaclePosition.bottom = (int)(originalObstaclePosition.bottom * 0.9);
        obstaclePosition.right = (int)(originalObstaclePosition.right * 0.8);

        return Rect.intersects(runnerPosition, obstaclePosition);
    }

    public static boolean detectCollisionPoint(Runner runner, Point point){

        Rect runnerPosition = runner.getSpritePosition();
        Rect originalObstaclePosition = point.getBitmapPosition();

        Rect pointPosition = new Rect();
        pointPosition.top = (int)(originalObstaclePosition.top * 1.1);
        pointPosition.left = (int)(originalObstaclePosition.left * 1.2);
        pointPosition.bottom = (int)(originalObstaclePosition.bottom * 0.9);
        pointPosition.right = (int)(originalObstaclePosition.right * 0.8);

        return Rect.intersects(runnerPosition, pointPosition);
    }
}
