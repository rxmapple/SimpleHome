package com.sprd.simple.model;

import android.content.ContentResolver;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.provider.Settings;
import android.util.Log;

import java.util.Locale;

/**
 * Created by SPRD on 16-8-24.
 */
public class TextSpeech {
    private static final String TAG = "TextSpeech";
    private static TextToSpeech textToSpeech;

    public static void read(final int position, final int pm[], final Context context) {
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                try {
                    Log.d(TAG, "mTextToSpeech.speak && VOICE_FOR_MENU = " + (
                            Settings.System.getInt(context.getContentResolver(),
                            Settings.System.VOICE_FOR_MENU)));
                    if ((Settings.System.getInt(context.getContentResolver(),
                            Settings.System.VOICE_FOR_MENU)) == 1) {
                        Log.d(TAG, "onInit()  ");
                        if (status == TextToSpeech.SUCCESS) {
                            Log.d(TAG, "TextToSpeech.SUCCESS");
                            int result = textToSpeech.setLanguage(Locale.ENGLISH);
                            if (result == TextToSpeech.LANG_MISSING_DATA ||
                                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Log.d(TAG, "Language is not available.");
                            } else {
                                textToSpeech.speak(context.getResources().getString(pm[position]),
                                        TextToSpeech.QUEUE_FLUSH, null);
                                Log.d(TAG, "package name = " + context.getResources().getString(pm[position]));
                            }
                        } else {
                            Log.d(TAG, "Could not initialize TextToSpeech.");
                        }
                    }
                } catch (Settings.SettingNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
