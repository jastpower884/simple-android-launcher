package org.jast.simpleandroidlauncher;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.SeekBar;

public class SettingActivity extends AppCompatActivity implements SettingsContentObserver.SettingChanger {

    protected SeekBar mSeekBarBrightness;
    protected SeekBar mSeekBarRingVolume;
    protected SeekBar mSeekBarNotificationVolume;
    protected SeekBar mSeekBarMusicVolume;
    protected SeekBar mSeekBarSystemVolume;


    protected Handler mHandler = new Handler();

    WindowManager.LayoutParams layoutParams;
    AudioManager audioManager;

    SettingsContentObserver mSettingsContentObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initMember();
        initViewAndViewEvent();
        setScreenBrightnessToModeManual();


    }

    private void initMember() {
        layoutParams = getWindow().getAttributes();
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mSettingsContentObserver = new SettingsContentObserver(this, mHandler, this);

        // register to listen volume change
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver);

    }

    private void initViewAndViewEvent() {

        mSeekBarBrightness = (SeekBar) findViewById(R.id.seek_bar_brightness);
        mSeekBarRingVolume = (SeekBar) findViewById(R.id.seek_bar_ring_volume);
        mSeekBarNotificationVolume = (SeekBar) findViewById(R.id.seek_bar_notification_volume);
        mSeekBarMusicVolume = (SeekBar) findViewById(R.id.seek_bar_music_volume);
        mSeekBarSystemVolume = (SeekBar) findViewById(R.id.seek_bar_system_volume);


        try {
            // set brightness value
            int brightnessValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
            mSeekBarBrightness.setProgress(brightnessValue * 100 / 255);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        setVolumeOfEachSeekbar();

        mSeekBarBrightness.setOnSeekBarChangeListener(seekBarChangeListener);
        mSeekBarRingVolume.setOnSeekBarChangeListener(seekBarChangeListener);
        mSeekBarNotificationVolume.setOnSeekBarChangeListener(seekBarChangeListener);
        mSeekBarMusicVolume.setOnSeekBarChangeListener(seekBarChangeListener);
        mSeekBarSystemVolume.setOnSeekBarChangeListener(seekBarChangeListener);

    }

    /**
     * set screen brightness to manual mode
     */
    private void setScreenBrightnessToModeManual() {

        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

    }

    SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if (fromUser) {
                switch (seekBar.getId()) {
                    case R.id.seek_bar_brightness:
                        changeBrightness(progress);
                        break;
                    case R.id.seek_bar_ring_volume:
                        changeVolume(progress, AudioManager.STREAM_RING);
                        break;
                    case R.id.seek_bar_notification_volume:
                        changeVolume(progress, AudioManager.STREAM_NOTIFICATION);
                        break;
                    case R.id.seek_bar_music_volume:
                        changeVolume(progress, AudioManager.STREAM_MUSIC);
                        break;
                    case R.id.seek_bar_system_volume:
                        changeVolume(progress, AudioManager.STREAM_SYSTEM);
                        break;
                }
            }

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };


    /**
     * change screen brightness and system brightness
     */
    private void changeBrightness(int progress) {


        float newBrightness = progress / 100f;
        if (progress <= 0) {
            newBrightness = 0.004f;
        }
        // set the brightness on this screen

        // Window setting range : 0.0 ~ 1.0
        layoutParams.screenBrightness = newBrightness;
        getWindow().setAttributes(layoutParams);

        // set the system brightness
        // System setting range : 0 ~ 255
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, (int) (newBrightness * 255));


    }

    /**
     * change volume by mode
     */
    private void changeVolume(int progress, int mode) {


        int maxRingVolume = audioManager.getStreamMaxVolume(mode);
        audioManager.setStreamVolume(mode, progress * maxRingVolume / 100, 0);

    }

    private void setVolumeOfEachSeekbar() {
        // set ring volume
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        int maxRingVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        mSeekBarRingVolume.setProgress(volume * 100 / maxRingVolume);
        // set notification volume
        volume = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int maxNotificationVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION);
        mSeekBarNotificationVolume.setProgress(volume * 100 / maxNotificationVolume);
        // set music volume
        volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxMusicVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mSeekBarMusicVolume.setProgress(volume * 100 / maxMusicVolume);
        // set system volume
        volume = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int maxSystemVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
        mSeekBarSystemVolume.setProgress(volume * 100 / maxSystemVolume);

    }


    @Override
    public void onChange() {
        // When Volume Change

        setVolumeOfEachSeekbar();

    }
}
