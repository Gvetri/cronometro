package giuseppevetri.cronometro;

import android.content.Context;
import android.util.Log;

/**
 * Created by giuseppe on 11/05/17.
 */

class Chronometer implements Runnable {

    private static final long MILLIS_TO_MINUTES = 60000;
    private static final long MILLIS_TO_HOURS = 3600000;

    private MainActivity mainActivity;
    private Long startTime;
    private boolean isRunning;

    private int hours, minutes, seconds, millis;
    private long since;
    private long since_aux;
    private boolean first_time = true;


    Chronometer(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    void start() {
        startTime = System.currentTimeMillis();
        isRunning = true;
    }

    void stop() {
        isRunning = false;
        first_time = true;
    }

    void pause() {
        isRunning = false;
        first_time = false;


    }


    @Override
    public void run() {

        if (!first_time && isRunning) {
            startTime = System.currentTimeMillis();
            since_aux = since;
        }

        while (!Thread.currentThread().isInterrupted() && isRunning) {
            if (first_time) {
                since = System.currentTimeMillis() - startTime;
                since_aux = System.currentTimeMillis();
            } else {
                since = since_aux + System.currentTimeMillis() - startTime;
            }

            seconds = (int) ((since / 1000) % 60);
            minutes = (int) ((since / MILLIS_TO_MINUTES) % 60);
            hours = (int) ((since / MILLIS_TO_HOURS) % 60);
            millis = (int) (since % 1000);
            mainActivity.updateTimerText(String.format(
                    "%02d:%02d:%02d:%03d", hours, minutes, seconds, millis
            ));
        }
    }


}
