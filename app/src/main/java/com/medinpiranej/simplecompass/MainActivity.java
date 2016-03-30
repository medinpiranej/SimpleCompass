package com.medinpiranej.simplecompass;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements SensorEventListener {

    public static Float rotation;
    private ImageView compass_background;
    private ImageView compass_star;
    private boolean isPopupOpen;
    private RelativeLayout popup;
    private ImageView infoBtn;
    private SensorManager sensorManager;
    private Sensor sensor;

    protected void onCreate(Bundle savedInstanceState) {
        isPopupOpen = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // if LOLLIPOP or above change status bar color
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.parseColor("#303F9F"));
        }

        compass_background = (ImageView) findViewById(R.id.compass_background);
        compass_star = (ImageView) findViewById(R.id.compass_star);
        infoBtn = (ImageView) findViewById(R.id.infoBtn);
        popup = (RelativeLayout) findViewById(R.id.infoContainer);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        // action to open info popup
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPopupOpen) {
                    popup.setAlpha(0);
                    popup.setVisibility(View.VISIBLE);
                    popup.animate().alpha(1f);
                    isPopupOpen = true;
                }
            }
        });
        // action to close info popup
        popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPopupOpen) {
                    popup.animate().alpha(0f);
                    isPopupOpen = false;
                }
            }
        });
    }

    public void onSensorChanged(SensorEvent event) {
        rotation = event.values[0];
        if (compass_background != null && rotation != null) {
            compass_background.setRotation(-rotation);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onStop() {
        sensorManager.unregisterListener(this);
        super.onStop();
    }
}