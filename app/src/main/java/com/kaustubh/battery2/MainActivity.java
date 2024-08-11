package com.kaustubh.battery2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

public class MainActivity extends AppCompatActivity {
    private TextView batteryPercentageText;

    private SeekBar batterySeekBar;
    private Button setButton;
    private TextView displayText;
    private int desiredBatteryLevel;
    private ImageView powerImage;
    private ImageView exitImage;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView marqueeText = findViewById(R.id.marqueeText);
        marqueeText.setSelected(true);

        batteryPercentageText = findViewById(R.id.batteryPercentageText);
        powerImage = findViewById(R.id.powerImage);

        exitImage = findViewById(R.id.exitImage);


        int batteryPercentage = getBatteryPercentage();
        batteryPercentageText.setText(batteryPercentage + "%");
        setBatteryTextColor(batteryPercentage);


        batterySeekBar = findViewById(R.id.batterySeekBar);
        setButton = findViewById(R.id.setButton);
        displayText = findViewById(R.id.displayText);



        batterySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                desiredBatteryLevel = progress;
                displayText.setText(progress + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        setButton.setOnClickListener(v -> {
            startMonitoringBatteryLevel();
            showConfirmationDialog();
        });
        exitImage.setOnClickListener(v -> showExitConfirmationDialog());
        checkChargingStatus();


    }

    //    private void showExitConfirmationDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("Exit")
//                .setMessage("Are you sure you want to exit?")
//                .setPositiveButton("Yes", (dialog, which) -> finish()).set // Close the app
//                .setNegativeButton("No", null) // Close the dialog
//                .show();
//    }
    private void showExitConfirmationDialog() {
        SpannableString yesText = new SpannableString("Yes");
        yesText.setSpan(new ForegroundColorSpan(Color.GREEN), 0, yesText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString noText = new SpannableString("No");
        noText.setSpan(new ForegroundColorSpan(Color.RED), 0, noText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> finish()) // Close the app
                .setNegativeButton("No", null) // Close the dialog
                .show();
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

    private void setBatteryTextColor(int batteryPercentage) {
        if (batteryPercentage <= 20) {
            batteryPercentageText.setTextColor(Color.RED);
        }  else if (batteryPercentage <= 50) {
            batteryPercentageText.setTextColor(Color.YELLOW);
        } else if (batteryPercentage <= 100) {
            batteryPercentageText.setTextColor(Color.GREEN);
        }
    }

    private void startMonitoringBatteryLevel() {
        BatteryLevelReceiver batteryLevelReceiver = new BatteryLevelReceiver(desiredBatteryLevel);
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, filter);
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Alert set to " + desiredBatteryLevel + "%.")
                .setPositiveButton("OK", null)
                .show();
    }


    private void checkChargingStatus() {

        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;

            if (isCharging) {
                powerImage.setVisibility(ImageView.VISIBLE);
            } else {
                powerImage.setVisibility(ImageView.GONE);
            }
        }
    }

}
