package fu.agile.iremember;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.widget.Toast;

@SuppressLint({ "SimpleDateFormat", "SdCardPath" })
public class Recorder {
	MediaRecorder Recoder;
	String audioPath;
	MediaPlayer mPlayer;
	Recorder() {
		mPlayer = new MediaPlayer();
	}
	
	public boolean startingRecord() {
		String prefix = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String audioName = "AD_AU_" + prefix + "_.mp3";
		audioPath = "/mnt/sdcard/IRemember/Audio" + "/" + audioName;
		Recoder = new MediaRecorder();
		Recoder.setAudioSource(MediaRecorder.AudioSource.MIC);
		Recoder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		Recoder.setOutputFile(audioPath);
		Recoder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		try {
			Recoder.prepare();
        } catch (IOException e) {
            return false;
        }
		Recoder.start();
		return true;
	}
	
	public void stopRecord() {
		Recoder.stop();
		Recoder.release();
		Recoder = null;
    }
	
	public void startPlaying() {
		mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(audioPath);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
        	Toast.makeText(getApplicationContext(), "SomeThing went wrong", Toast.LENGTH_LONG).show();
        }
    }
	
	public void stopPlaying() {
		try {
            mPlayer.stop();
        } catch (Exception e) {
        	
        }
	}
	
	private Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAudioPath() {
		return audioPath;
	}
	
	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}
	
	public int getDuration() {
		return mPlayer.getDuration();
	}
	
	public int getCurrentPosition() {
		return mPlayer.getCurrentPosition();
	}
	
	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}
	
 }
