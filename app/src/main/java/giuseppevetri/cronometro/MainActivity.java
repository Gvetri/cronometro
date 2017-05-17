package giuseppevetri.cronometro;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Cronometro";
    private FloatingActionButton fab_add_lap;
    private FloatingActionButton fab_start;
    private FloatingActionButton fab_stop;
    private Animation animation;
    private TextView tv_time;
    private giuseppevetri.cronometro.Chronometer chronometer;
    private Thread thread;
    private Context context;
    private RecyclerView recyclerview;
    private LapsAdapter adapter;
    private ArrayList<String> laps = new ArrayList<>();


    private boolean chronometer_active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animation = AnimationUtils.loadAnimation(this,R.anim.blink);

        tv_time = (TextView) findViewById(R.id.tv_time);

        context = this;

        fab_add_lap = (FloatingActionButton) findViewById(R.id.fab_add_lap);
        fab_start = (FloatingActionButton) findViewById(R.id.fab_play);
        fab_stop = (FloatingActionButton) findViewById(R.id.fab_stop);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        adapter = new LapsAdapter(laps);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);



        fab_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!chronometer_active) {
                    if (chronometer == null) {
                        chronometer = new Chronometer((MainActivity) context);
                        thread= new Thread(chronometer);
                        thread.start();
                        chronometer.start();
                        startChronometer();

                        Log.d(TAG, "onClick: cronometro 1 nulo");
                    } else {
                        Log.d(TAG, "onClick: cronometro 1 distinto de nulo");
                        thread= new Thread(chronometer);
                        thread.start();
                        chronometer.start();
                        startChronometer();
                    }
                } else {
                    if (chronometer != null) {
                        thread= new Thread(chronometer);
                        thread.interrupt();
                        chronometer.pause();
                        pauseChronometer();
                        Log.d(TAG, "onClick: cronometro 2 distinto de nulo");
                    }
                }

            }
        });

        fab_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_time.startAnimation(animation);
                if (chronometer != null) {
                    chronometer.stop();
                    thread.interrupt();
                    chronometer = null;
                    thread = null;
                    pauseChronometer();
                }

            }
        });

        fab_add_lap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                laps.add(tv_time.getText().toString());
                Log.d(TAG, "onClick: Agregada tag "+tv_time.getText().toString());
                adapter.notifyItemInserted(adapter.getItemCount());
            }
        });
    }

    private void startChronometer() {
        chronometer_active = true;
        tv_time.clearAnimation();
        tv_time.setTextColor(Color.BLUE);
        changeDrawable();
    }


    private void changeDrawable() {
        if (chronometer_active){
            Drawable drawable = ContextCompat.getDrawable(this,R.drawable.ic_pause_24dp);
            fab_start.setImageDrawable(drawable);
        } else {
            Drawable drawable = ContextCompat.getDrawable(this,R.drawable.ic_play_arrow_24dp);
            fab_start.setImageDrawable(drawable);
        }
    }

    private void pauseChronometer(){
        chronometer_active = false;
        tv_time.startAnimation(animation);
        changeDrawable();
        tv_time.setTextColor(Color.RED);

    }


    public void updateTimerText(final String time){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_time.setText(time);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("time",tv_time.getText().toString());
        outState.putBoolean("chronometer",chronometer_active);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String string = savedInstanceState.getString("time");
        Log.d(TAG, "onRestoreInstanceState: "+string);
        if (tv_time != null) {
            tv_time.setText(string);
        }

        chronometer_active = savedInstanceState.getBoolean("chronometer");
    }
}
