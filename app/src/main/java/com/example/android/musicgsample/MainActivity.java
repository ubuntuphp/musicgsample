package com.example.android.musicgsample;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FingerprintSimilarity mFinger;
        Wave wave1 = new Wave(getResources().openRawResource(R.raw.wave2));
        Wave wave2 = new Wave(getResources().openRawResource(R.raw.wave1));
        mFinger = wave1.getFingerprintSimilarity(wave2);
        Toast.makeText(this , Float.toString(mFinger.getScore()) , Toast.LENGTH_LONG).show();
    }
}
