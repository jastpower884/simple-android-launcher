package org.jast.simpleandroidlauncher;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

/**
 * Created by ptc_02008 on 2016/12/8.
 */

public class SettingsContentObserver extends ContentObserver {

    public interface SettingChanger {
        public void onChange();
    }

    private SettingChanger mSettingChanger;

    private AudioManager audioManager;

    public SettingsContentObserver(Context context, Handler handler, SettingChanger mSettingChanger) {
        super(handler);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.mSettingChanger = mSettingChanger;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false;
    }

    @Override
    public void onChange(boolean selfChange) {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (mSettingChanger != null) {
            mSettingChanger.onChange();
        }
        Log.d("Volume Change ", "Volume now " + currentVolume);
    }
}
