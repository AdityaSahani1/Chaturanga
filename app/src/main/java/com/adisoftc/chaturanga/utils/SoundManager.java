package com.adisoftc.chaturanga.utils;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.content.SharedPreferences;

public class SoundManager {
    private static SoundPool soundPool;
    private static int soundMove;
    private static int soundCapture;
    private static int soundDiceRoll;
    private static int soundCheck;
    private static int soundGameOver;
    private static int soundVictory;
    private static int soundButtonClick;
    private static boolean soundEnabled = true;
    
    public static void initialize(Context context) {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
        
        soundPool = new SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build();
        
        SharedPreferences prefs = context.getSharedPreferences(Constants.PREF_NAME, Context.MODE_PRIVATE);
        soundEnabled = prefs.getBoolean(Constants.PREF_SOUND_ENABLED, true);
    }
    
    public static void loadSounds(Context context) {
        if (soundPool == null) {
            initialize(context);
        }
    }
    
    public static void playMoveSound() {
        if (soundEnabled && soundPool != null && soundMove != 0) {
            soundPool.play(soundMove, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
    
    public static void playCaptureSound() {
        if (soundEnabled && soundPool != null && soundCapture != 0) {
            soundPool.play(soundCapture, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
    
    public static void playDiceRollSound() {
        if (soundEnabled && soundPool != null && soundDiceRoll != 0) {
            soundPool.play(soundDiceRoll, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
    
    public static void playCheckSound() {
        if (soundEnabled && soundPool != null && soundCheck != 0) {
            soundPool.play(soundCheck, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
    
    public static void playGameOverSound() {
        if (soundEnabled && soundPool != null && soundGameOver != 0) {
            soundPool.play(soundGameOver, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
    
    public static void playVictorySound() {
        if (soundEnabled && soundPool != null && soundVictory != 0) {
            soundPool.play(soundVictory, 1.0f, 1.0f, 1, 0, 1.0f);
        }
    }
    
    public static void playButtonClickSound() {
        if (soundEnabled && soundPool != null && soundButtonClick != 0) {
            soundPool.play(soundButtonClick, 0.5f, 0.5f, 1, 0, 1.0f);
        }
    }
    
    public static void setSoundEnabled(boolean enabled) {
        soundEnabled = enabled;
    }
    
    public static boolean isSoundEnabled() {
        return soundEnabled;
    }
    
    public static void release() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }
}
