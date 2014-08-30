package fu.agile.iremember;

import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressLint({ "SetJavaScriptEnabled", "ClickableViewAccessibility" })
public class ViewEvent extends Activity implements OnClickListener {

	//Button Declare
	private ImageButton btEdit;
	private ImageButton btPlayAudio;
	private ImageButton btPauseAudio;
	//Edit Text Declare
	private MediaPlayer mMedia;
	//TextView Declare
	private TextView tvTime;
	private TextView viewTitle;
	private TextView tvBody;
	private TextView tvlongitude;
	private TextView tvlatitute;
	
	//String Declare
	private String audioPath;
	private String imagePath;
	private String videoPath;
	private String latitute;
	private String longitude;
	private DataBase db;
	private Card card;
	private WebView viewMap;
	//Animation Declaration
	private Animation anim;
	private static int position;
	
	//Image Declare
	private ImageView viewImage;
	
	
	//Video
	private VideoView viewVideo;
	//something else declare
	private SeekBar seekBar;
	Handler myhaHandler = new Handler();
	private static int REQUEST_EDIT = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_event);
		
		Intent intent = getIntent();
		position = intent.getIntExtra("position", -1);
		if(position == -1) {
			Toast.makeText(getApplicationContext(), "You are encounter an error", Toast.LENGTH_LONG).show();
			Intent inte = new Intent();
			setResult(RESULT_CANCELED, inte);
			finish();
		}
		intit();  
		
		implementInit();
	}

	private void intit() {
		btEdit = (ImageButton) findViewById(R.id.btEditEvent);
		btEdit.setOnClickListener(this);
		btPlayAudio = (ImageButton)findViewById(R.id.btAddPlayAudio);
		btPlayAudio.setOnClickListener(this);
		btPauseAudio = (ImageButton)findViewById(R.id.btPauseAudio);
		btPauseAudio.setOnClickListener(this);
		viewTitle = (TextView)findViewById(R.id.viewTitle);
		tvBody  = (TextView)findViewById(R.id.tvBody);
		viewImage = (ImageView)findViewById(R.id.viewImage);
		viewVideo = (VideoView)findViewById(R.id.viewVideo);
		tvlongitude = (TextView)findViewById(R.id.tvLongitude);
		tvlatitute = (TextView)findViewById(R.id.tvlatitude);
		viewMap = (WebView) findViewById(R.id.webView);
		viewMap.setWebViewClient(new WebViewClient());
		tvTime = (TextView)findViewById(R.id.tvTime);
		anim = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);
		seekBar = (SeekBar)findViewById(R.id.seekBarAudio);
		db = new DataBase(this);
		mMedia = new MediaPlayer();
	}
	
	private void implementInit() {
		List<Card> RecordList = db.getAllRecords();
		card = RecordList.get(position);

		viewTitle.setText(card.getTitle());
		tvBody.setText(card.getBody());
		audioPath = card.getAudioFile();
		imagePath = card.getImageFile();
		videoPath = card.getVideoFile();
		if(imagePath.equalsIgnoreCase("unknow") == false) {
			findViewById(R.id.imageLayout).setVisibility(View.VISIBLE);
			Bitmap bit = BitmapFactory.decodeFile(imagePath);
			viewImage.setImageBitmap(bit);
		}else {
			findViewById(R.id.imageLayout).setVisibility(View.GONE);
		}
		
		if(audioPath.equalsIgnoreCase("unknow") == true) {
			findViewById(R.id.audioAddLayout).setVisibility(View.GONE);
		}else{
			findViewById(R.id.audioAddLayout).setVisibility(View.VISIBLE);
		}
		
		if(videoPath.equalsIgnoreCase("unknow") == false) {
			findViewById(R.id.videoLayout).setVisibility(View.VISIBLE);
			viewVideo.setVideoPath(videoPath);
			viewVideo.setVideoPath(videoPath);
			viewVideo.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					viewVideo.start();
					return false;
				}
			});
		}
		else {
			findViewById(R.id.videoLayout).setVisibility(View.GONE);
		}
		
		tvTime.setText(card.getTime());
		latitute = card.getLatitute();
		longitude = card.getLongitude();
		tvlatitute.setText(latitute);
		tvlongitude.setText(longitude);
		
		if(isOnline() == true && latitute.equalsIgnoreCase("Location not available") != true) {
			findViewById(R.id.locationLayout).setVisibility(View.VISIBLE);
			String mapPath = "http://maps.google.com/maps?q=";
			mapPath += card.getLatitute() +"," + card.getLongitude() + "&t=m&z=7";
			viewMap.getSettings().setJavaScriptEnabled(true);
			viewMap.loadUrl(mapPath);
		}
		
		else {
			findViewById(R.id.locationLayout).setVisibility(View.GONE);
		}
	}

	protected boolean isOnline() {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        return true;
	    } else {
	        return false;
	    }
	}
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {

			case R.id.btEditEvent : {
				findViewById(R.id.bt_edit_effect).startAnimation(anim);
				Intent intent = new Intent(ViewEvent.this,Edit_view.class);
				intent.putExtra("position", position);
				startActivityForResult(intent, REQUEST_EDIT);
				break;
			} 
			case R.id.btAddPlayAudio : {
				findViewById(R.id.bt_play_audio_effect).startAnimation(anim);
				findViewById(R.id.btPauseAudio).setVisibility(View.VISIBLE);
				findViewById(R.id.btAddPlayAudio).setVisibility(View.INVISIBLE);
				findViewById(R.id.bt_play_audio_effect).setVisibility(View.INVISIBLE);
				mMedia.reset();
				mMedia = MediaPlayer.create(this, Uri.parse(audioPath));
				mMedia.start();
				seekBar.setProgress(0);
				seekBar.setMax(mMedia.getDuration());				
				startPlayProgressUpdater();
				//btPauseAudio.setVisibility(View.VISIBLE);
				//btPlayAudio.setVisibility(View.INVISIBLE);
				break;
			}case R.id.btPauseAudio : {
				findViewById(R.id.bt_pause_audio_effect).startAnimation(anim);
				findViewById(R.id.btAddPlayAudio).setVisibility(View.VISIBLE);
				findViewById(R.id.btPauseAudio).setVisibility(View.INVISIBLE);
				try {
					mMedia.stop();
				}catch(Exception exc) {
					
				}
				break;
			}
		}
	}

	public void startPlayProgressUpdater() {
	    
		Runnable notification = new Runnable() {
			public void run() {
				seekBar.setProgress(mMedia.getCurrentPosition());
				startPlayProgressUpdater();
				if(mMedia.isPlaying() == false) {
					seekBar.setProgress(mMedia.getDuration());
				}
			}
		};
		myhaHandler.postDelayed(notification,1000);
	}
	
	
	@Override
	protected void onRestart() {
		super.onRestart();
		implementInit();
	}
}
