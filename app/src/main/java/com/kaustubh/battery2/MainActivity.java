package com.kaustubh.battery2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView batteryPercentageText;

    private SeekBar batterySeekBar;
    private Button setButton;
    private TextView displayText;
    private int desiredBatteryLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        batteryPercentageText = findViewById(R.id.batteryPercentageText);

        int batteryPercentage = getBatteryPercentage();
        batteryPercentageText.setText("Battery: " + batteryPercentage + "%");


        batterySeekBar = findViewById(R.id.batterySeekBar);
        setButton = findViewById(R.id.setButton);
        displayText = findViewById(R.id.displayText);

        batterySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredBatteryLevel = progress;
                displayText.setText("Set to: " + progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setButton.setOnClickListener(v -> {
            startMonitoringBatteryLevel();
        });
    }

    private int getBatteryPercentage() {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = 0;
        int scale = 0;

        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        }

        return (int) ((level / (float) scale) * 100);
    }



    private void startMonitoringBatteryLevel() {
        BatteryLevelReceiver batteryLevelReceiver = new BatteryLevelReceiver(desiredBatteryLevel);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, filter);
    }

//        if (batteryPct >= desiredBatteryLevel) {
//        triggerAlarm(context);
//    }

}
