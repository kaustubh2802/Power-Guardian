package com.kaustubh.battery2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import androidx.appcompat.app.AlertDialog;


public class BatteryLevelReceiver extends BroadcastReceiver {

    private int desiredBatteryLevel;

    public BatteryLevelReceiver(int desiredBatteryLevel) {
        this.desiredBatteryLevel = desiredBatteryLevel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) ((level / (float) scale) * 100);

        if (batteryPct >= desiredBatteryLevel) {
            triggerAlarm(context);
        }

//        if (batteryPct >= desiredBatteryLevel) {
//            triggerAlarm(context);
//        }
    }

    private void triggerAlarm(Context context) {
        // Play ringtone and display message
        // Create an alert dialog and play ringtone in loop
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.soundone);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        // Display alert dialog
        new AlertDialog.Builder(context)
                .setTitle("Battery Full")
                .setMessage("Please turn off your switch")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                })
                .show();

    }
}



