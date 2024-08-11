package com.kaustubh.battery2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import androidx.appcompat.app.AlertDialog;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import androidx.appcompat.app.AlertDialog;

public class BatteryLevelReceiver extends BroadcastReceiver {

    private int desiredBatteryLevel;
    private MediaPlayer mediaPlayer;

    public BatteryLevelReceiver(int desiredBatteryLevel) {
        this.desiredBatteryLevel = desiredBatteryLevel;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int batteryPct = (int) ((level / (float) scale) * 100);
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;

        if (isCharging && batteryPct >= desiredBatteryLevel) {
            triggerAlarm(context);
        }
    }

    private void triggerAlarm(Context context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.soundone);
            mediaPlayer.setLooping(true);
        }

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

        new AlertDialog.Builder(context)
                .setTitle("Battery Full")
                .setMessage("Please turn off your switch")
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                })
                .show();
    }
}
