package org.jast.simpleandroidlauncher;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.SeekBar;

public class SettingActivity extends AppCompatActivity {

    protected SeekBar mSeekBarBrightness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initViewAndViewEvent();
        setScreenBrightnessToModeManual();


    }


    private void initViewAndViewEvent() {

        mSeekBarBrightness = (SeekBar) findViewById(R.id.seek_bar_brightness);

        try {
            int brightnessValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);


            mSeekBarBrightness.setProgress(brightnessValue * 100 / 255);
            Log.v("AppLauncher", "brightnessValue:" + brightnessValue);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        mSeekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                float newBrightness = progress / 100f;
                Log.v("AppLauncher", "progress:" + newBrightness);

                if (progress <= 0) {
                    newBrightness = 0.004f;
                }

                WindowManager.LayoutParams lp = getWindow().getAttributes();
                // Window setting range : 0.0 - 1.0
                lp.screenBrightness = newBrightness;
                getWindow().setAttributes(lp);


                // System setting range : 0 - 255
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) (newBrightness * 255));


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    /**
     * 將螢幕亮度設定調整為手動
     */
    private void setScreenBrightnessToModeManual() {

        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

    }
}
