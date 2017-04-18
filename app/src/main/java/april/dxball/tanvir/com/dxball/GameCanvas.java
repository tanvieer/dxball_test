package april.dxball.tanvir.com.dxball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;

/**
 * Created by Tanvir on 17-Apr-17.
 */

public class GameCanvas extends View {
    Paint paint;
    float maxWidth, maxHeight, circleSize = 20, maxLeft, maxRight;


    boolean changeDir = true;
    float ballX = 0, ballY = 0, rectLeft, rectRight, rectMoveConst = 2;
    boolean firstTime = true;

    static boolean rn = true;

    public static double value_AccelarometerX;
    public static double value_AccelarometerY;
    public static double value_AccelarometerZ;
    private Sensor _AccelerometerSensor;
    public static Thread accelerometer_sensorThread;
    private static SensorEventListener _AccelerometerListener;
    private static SensorManager sm;


    public GameCanvas(Context context) {
        super(context);

        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);


        try {
            Thread.sleep(50);
            AccelerometerCheckingStart();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        paint = new Paint();

    }


    protected void calculateNextPos() {


        //sm.registerListener(_AccelerometerListener, _AccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        // sm.unregisterListener(_AccelerometerListener);


       /* if(changeDir){
            //y++;
            if(maxHeight<= y){
                changeDir = false;
            }
        }
        else {
           // y--;
            if(y<=circleSize){
                changeDir = true;
            }

        }*/
       /* Log.d("posi", ballY + "  " + maxHeight);*/

       RectMove(value_AccelarometerX);

        /* if (value_AccelarometerY > .09) {
            if (maxHeight > ballY) {
                // y += 4;
                ballY += Math.abs(value_AccelarometerY * 2);
            }
        } else if (value_AccelarometerY < -.09) {
            if (ballY > circleSize) {
                //y -= 4;
                ballY -= Math.abs(value_AccelarometerY * 2);
            }
        }

        if (value_AccelarometerX > .05) {
            Log.d("xside", maxWidth + " l " + ballX);
            if (circleSize < ballX) {
                // x -= 2;

                ballX -= Math.abs(value_AccelarometerX * 2);
            }
        } else if (value_AccelarometerX < -.05) {
            Log.d("xside", circleSize + " r " + ballX);
            if (maxWidth > ballX) {
                //x += 2;
                ballX += Math.abs(value_AccelarometerX * 2);
            }
        }*/


        travel();


    }

    protected void onDraw(Canvas canvas) {
        if (firstTime) {
            firstTime = false;
            ballX = canvas.getWidth() / 2;
            ballY = canvas.getHeight() - canvas.getHeight() / 4;
            maxWidth = canvas.getWidth();
            maxHeight = canvas.getHeight() - circleSize - 1;
            rectLeft = ballX - ballX / 3;
            rectRight = ballX + ballX / 3;



            MovingBall();


            xmin = 0;
            xmax = canvas.getWidth();
            ymin = 0;
            ymax = canvas.getHeight();

        }
        calculateNextPos();
        canvas.drawRGB(255, 255, 255);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(ballX, ballY, circleSize, paint);
        //canvas.drawRect(20, 20, 200 ,200 ,paint);
        //canvas.drawRect(20, 20, 100 ,200 ,paint);

        canvas.drawRect(rectLeft, maxHeight - 10, rectRight, canvas.getHeight(), paint);


        invalidate();
    }


    private void RectMove(double m) {
        m = m * -3;
        if (rectRight < maxWidth + 20 && m > 0) {
            rectLeft += m;
            rectRight += m;
        } else if (rectLeft > 0 && m < 0) {
            {
                rectLeft += m;
                rectRight += m;
            }
        }
    }


    public static void AccelerometerCheckingStop() {
        sm.unregisterListener(_AccelerometerListener);
        rn = false;
    }


    private void AccelerometerCheckingStart() {

        Log.d("sensorval1", " function called");

        _AccelerometerSensor = sm.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);

        accelerometer_sensorThread = new Thread() {
            public void run() {

                _AccelerometerListener = new SensorEventListener() {

                    @Override
                    public void onSensorChanged(SensorEvent event) {
                        // TODO Auto-generated method stub

                        value_AccelarometerX = event.values[0];
                        value_AccelarometerY = event.values[1];
                        value_AccelarometerZ = event.values[2];

                        Log.d("sensorval", "X = " + value_AccelarometerX + " Y = " + value_AccelarometerY + " Z = " + value_AccelarometerZ);

                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {
                        // TODO Auto-generated method stub

                    }
                };

            }
        };
        accelerometer_sensorThread.start();

        try {
            accelerometer_sensorThread.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        sm.registerListener(_AccelerometerListener, _AccelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

























    private double xmin, xmax;  // The horizontal limits on the ball's position.
    //    The x-coordinate of the ball statisfies
    //    xmin <= x <= xmax.

    private double ymin, ymax;  // The vertical limits on the ball's position.
    //    The y-coordinate of the ball statisfies
    //    ymin <= y <= ymax.

    private double x, y;      // Current position of the ball.

    private double dx, dy;    // The velocity (speed + direction) of the ball.
    //   When the travel() method is called, the
    //   ball moves dx pixesl horizontally and dy
    //   pixels vertically.


    public void MovingBall() {

        x = (xmin + xmax) / 2;
        y = (ymin + ymax) / 2;
        double angle = 2 * Math.PI * Math.random();  // Random direction.
        double speed = 4 + 8 * Math.random();          // Random speed.
        dx = Math.cos(angle) * speed;
        dy = Math.sin(angle) * speed;
    }



    public void travel() {
        // Move the ball by one time unit.  The ball moves in its current
        // direction for a number of pixels equal to its current speed.
        // That is, speed is given in terms of pixels per time unit.
        // Note:  The ball won't move at all if the width or height
        // of the rectangle is smaller than the ball's diameter.
        travel(1.0);
    }

    public void travel(double time) {
        // Move the ball for the specified number of time units.
        // The ball is restricted to the specified rectangle.
        // Note:  The ball won't move at all if the width or height
        // of the rectangle is smaller than the ball's diameter.

      /* Don't do anything if the rectangle is too small. */

        if (xmax - xmin < 2 * circleSize || ymax - ymin < 2 * circleSize)
            return;

      /* First, if the ball has gotten outside its rectangle, move it
         back.  (This will only happen if the rectagnle was changed
         by calling the setLimits() method or if the position of
         the ball was changed by calling the setLocation() method.)
      */

        if (x - circleSize < xmin)
            x = xmin + circleSize;
        else if (x + circleSize > xmax)
            x = xmax - circleSize;
        if (y - circleSize < ymin)
            y = ymin + circleSize;
        else if (y + circleSize > ymax)
            y = ymax - circleSize;

      /* Compute the new position, possibly outside the rectangle. */

        double newx = x + dx * time;
        double newy = y + dy * time;

      /* If the new position lies beyond one of the sides of the rectangle,
         "reflect" the new point through the side of the rectangle, so it
         lies within the rectangle. */

        if (newy < ymin + circleSize) {
            newy = 2 * (ymin + circleSize) - newy;
            dy = Math.abs(dy);
        } else if (newy > ymax - circleSize) {
            newy = 2 * (ymax - circleSize) - newy;
            dy = -Math.abs(dy);
        }
        if (newx < xmin + circleSize) {
            newx = 2 * (xmin + circleSize) - newx;
            dx = Math.abs(dx);
        } else if (newx > xmax - circleSize) {
            newx = 2 * (xmax - circleSize) - newx;
            dx = -Math.abs(dx);
        }

      /* We have the new values for x and y. */

        x = newx;
        y = newy;

        ballX = (float)x;
        ballY = (float)y;

    }




}

