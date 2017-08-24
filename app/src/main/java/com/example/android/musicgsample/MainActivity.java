package com.example.android.musicgsample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.media.AudioFormat;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioGenerator;
import be.tarsos.dsp.io.android.AndroidFFMPEGLocator;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import cafe.adriel.androidaudioconverter.AndroidAudioConverter;
import cafe.adriel.androidaudioconverter.callback.IConvertCallback;
import cafe.adriel.androidaudioconverter.callback.ILoadCallback;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_EXTERNAL_STORAGE = 4;
    private MediaPlayer mMediaPlayer , mSoundPlayer;
    private MediaRecorder mMediaRecorder;
    FingerprintSimilarity mFinger;
    private String mUrl;
    private boolean vPrepared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new AndroidFFMPEGLocator(this);

        /*AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                    Toast.makeText(getApplicationContext() , "supported" , Toast.LENGTH_LONG).show();
                FingerprintSimilarity mFinger;
                //AudioFormat audioFormat =
                InputStream is1 =  getResources().openRawResource(R.raw.sample1);
                AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromPipe("sample1.mp3" , 16 , 4016 , 4016);
                try {
                    Log.i("length" , "available" + is1.available());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                File flacFile = new File(Environment.getExternalStorageDirectory(), "my_audio.flac");
                IConvertCallback callback = new IConvertCallback() {
                    @Override
                    public void onSuccess(File convertedFile) {
                        Log.i("FILE TYPE:" , convertedFile.getName());
                    }
                    @Override
                    public void onFailure(Exception error) {
                        error.printStackTrace();
                        // Oops! Something went wrong
                    }
                };
                AndroidAudioConverter.with(getApplicationContext())
                        // Your current audio file
                        .setFile(flacFile)

                        // Your desired audio format
                        .setFormat(cafe.adriel.androidaudioconverter.model.AudioFormat.WAV)
                        // An callback to know when conversion is finished
                        .setCallback(callback)

                        // Start conversion
                        .convert();

            }
            @Override
            public void onFailure(Exception error) {
                Toast.makeText(getApplicationContext() , "error" , Toast.LENGTH_LONG).show();
            }
        });
*/
        //AudioFormat audioFormat =
        InputStream is1 =  getResources().openRawResource(R.raw.c1s2);

        Wave wave1 = new Wave(is1);
        Wave wave2 = new Wave(getResources().openRawResource(R.raw.c1s2));
        mFinger = wave1.getFingerprintSimilarity(wave2);
        Toast.makeText(this , Float.toString(mFinger.getScore()) , Toast.LENGTH_LONG).show();
        getPermissions();
        setUpSoundPlayer();
    }
    private void setUpSoundPlayer()
    {
        mSoundPlayer = new MediaPlayer();
        mSoundPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                vPrepared = true;
            }
        });
        try {
            mSoundPlayer.setDataSource("file:///storage/emulated/0/Download/c1s2.wav");
        } catch (IOException e) {
            e.printStackTrace();
        }
        mSoundPlayer.prepareAsync();
    }
    private void setUpMediaRecorder()
    {

    }
    private void setUpMediaPlayer()
    {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
            }
        });
        try {
            mMediaPlayer.setDataSource(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.prepareAsync();

    }

    public void onClickPlay(View view) {
        setUpMediaPlayer();
    }

    public void onClickStop(View view) {
        mMediaRecorder.stop();
        compare();
    }

    private void compare() {
        AndroidAudioConverter.load(this, new ILoadCallback() {
            @Override
            public void onSuccess() {
                //AudioFormat audioFormat =
                AudioDispatcher audioDispatcher = AudioDispatcherFactory.fromPipe(mUrl , 16 , 4016 , 4016);
                File flacFile = new File(mUrl);
                IConvertCallback callback = new IConvertCallback() {
                    @Override
                    public void onSuccess(File convertedFile) {
                        Wave wave1 = new Wave(getResources().openRawResource(R.raw.c1s2));

                        Wave wave2 = new Wave(convertedFile.getAbsolutePath());
                        mFinger = wave1.getFingerprintSimilarity(wave2);
                        Toast.makeText(getApplicationContext() , Float.toString(mFinger.getScore()) , Toast.LENGTH_LONG).show();                    }
                    @Override
                    public void onFailure(Exception error) {
                        error.printStackTrace();
                        // Oops! Something went wrong
                    }
                };
                AndroidAudioConverter.with(getApplicationContext())
                        // Your current audio file
                        .setFile(flacFile)

                        // Your desired audio format
                        .setFormat(cafe.adriel.androidaudioconverter.model.AudioFormat.WAV)
                        // An callback to know when conversion is finished
                        .setCallback(callback)

                        // Start conversion
                        .convert();

            }
            @Override
            public void onFailure(Exception error) {
                Toast.makeText(getApplicationContext() , "error" , Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getPermissions()
    {
        if(ContextCompat.checkSelfPermission(this , Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this , Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO} , WRITE_EXTERNAL_STORAGE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                } else {


                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void onClickRecord(View view) {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        try {
            mUrl = getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath() + "/2.3gp";
            Log.i("url",mUrl);
            mMediaRecorder.setOutputFile(mUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaRecorder.start();
    }

    public void onClickVoice(View view) {
        if(!mSoundPlayer.isPlaying() && vPrepared)mSoundPlayer.start();
    }
}
