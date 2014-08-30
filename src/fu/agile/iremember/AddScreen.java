package fu.agile.iremember;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.Time;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.VideoView;

@SuppressLint({ "ClickableViewAccessibility", "SimpleDateFormat" })
public class AddScreen extends Activity implements OnClickListener{

	//Button Declare
	private ImageButton btAddVideo;
	private ImageButton btCameraVideo;
	private ImageButton btPlayAudio;
	private ImageButton btPauseAudio;
	private ImageButton btRecordAudio;
	private ImageButton btStopRecordAudio;
	private ImageButton btAddImage;
	private ImageButton btOpenCamera;
	private ImageButton btAddAudio;
	private ImageButton btAddLocation;
	private ImageButton btCreate;
	private ImageButton btClear;
	private ImageButton btAddTime;
	private ImageButton btSelectImage;
	private ImageButton btSelectVideo;
	
	
	
	//Edit Text Declare
	private EditText etTitle;
	private EditText etBody;
	
	
	//String Declare
	private String time = "Unkown";
	private String audioPath = "Unknow";
	private String imagePath = "Unknow";
	private String videoPath = "Unknow";
	private static String tag = "Hello";
	private String latitute;
	private String longitude;
	
	//ImageView
	private ImageView imageView;
	//VideoView
	private VideoView videoView;
	//Animation Declaration
	private Animation anim;
	private Animation animSlideUp;
	//Something else
	private Card newRecord;
	private DataBase db;
	private Time calender;
	private DatePicker timePicker;
	static private int TAKE_PICTURE_CODE = 1;
	static private int TAKE_VIDEO_CODE = 2;
	private SeekBar seekBar;
	private Handler myhaHandler = new Handler();
	private Recorder recordAudio = new Recorder();
	private GPS gps;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		init();
	    latitute = "Location not available";
    	longitude = "Location not available";
	    // Initialize the location fields
	    db = new DataBase(getApplicationContext());
	    calender = new Time();
	    calender.setToNow();
	}	
	
	public void init() {
		time = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		btAddVideo = (ImageButton)findViewById(R.id.btAddVideo);
		btAddVideo.setOnClickListener(this);
		btCameraVideo = (ImageButton)findViewById(R.id.btCameraVideo);
		btCameraVideo.setOnClickListener(this);
		btAddAudio = (ImageButton) findViewById(R.id.btAddAudio);
		btAddAudio.setOnClickListener(this);
		btPlayAudio = (ImageButton) findViewById(R.id.btAddPlayAudio);
		btPlayAudio.setOnClickListener(this);
		btPauseAudio = (ImageButton) findViewById(R.id.btPauseAudio);
		btPauseAudio.setOnClickListener(this);
		btRecordAudio = (ImageButton) findViewById(R.id.btAddRecordAudio);
		btRecordAudio.setOnClickListener(this);
		btStopRecordAudio = (ImageButton) findViewById(R.id.btStopRecordAudio);
		btStopRecordAudio.setOnClickListener(this);
		btAddImage = (ImageButton) findViewById(R.id.btAddImage);
		btAddImage.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.AddViewImage);
		videoView = (VideoView) findViewById(R.id.AddViewVideo);
		btAddTime = (ImageButton)findViewById(R.id.btAddTime);
		btAddTime.setOnClickListener(this);
		btOpenCamera = (ImageButton) findViewById(R.id.btCameraImage);
		btOpenCamera.setOnClickListener(this);
		btAddLocation = (ImageButton)findViewById(R.id.btAddLocation);
		btAddLocation.setOnClickListener(this);
		btCreate = (ImageButton) findViewById(R.id.btCreateEvent);
		btCreate.setOnClickListener(this);
		btClear = (ImageButton) findViewById(R.id.btClear);
		btClear.setOnClickListener(this);
		btSelectImage = (ImageButton) findViewById(R.id.btSelectImage);
		btSelectImage.setOnClickListener(this);
		btSelectVideo = (ImageButton) findViewById(R.id.btSelectVideo);
		btSelectVideo.setOnClickListener(this);
		timePicker = (DatePicker)findViewById(R.id.timePicker);
		//timePicker.updateDate(calender.year, calender.month, calender.monthDay);
		anim = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);
		animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
		etTitle = (EditText) findViewById(R.id.title);
		etBody = (EditText) findViewById(R.id.body);
		seekBar = (SeekBar)findViewById(R.id.seekBarAudioPreview);
		seekBar.setProgress(0);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == TAKE_PICTURE_CODE) {
			if(resultCode == RESULT_OK){      
		    	 imagePath = data.getStringExtra("resultOfPhoto");
		    	 Bitmap bm = BitmapFactory.decodeFile(imagePath);
		    	 imageView.setImageBitmap(bm);
		    	 btSelectImage.setVisibility(View.INVISIBLE);
		     }
		} 
		if(requestCode == TAKE_VIDEO_CODE) {
			if(resultCode == RESULT_OK){      
		    	 videoPath = data.getStringExtra("resultOfVideo");
		    	 videoView.setVideoPath(videoPath);
		    	 btSelectVideo.setVisibility(View.INVISIBLE);
		    	 videoView.setVisibility(View.VISIBLE);
		    	 videoView.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						videoView.start();
						return false;
					}
				});
		    	 Log.d(tag, videoPath);
		     }
		} 
	} 

	 
	@Override
	public void onClick(View v) {
		
		switch(v.getId()) {
			case R.id.btAddAudio: {
				findViewById(R.id.timeAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.imageAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.videoAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.locationAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.audioAddLayout).setVisibility(View.VISIBLE);
				findViewById(R.id.imagebtAudio).startAnimation(anim);
				openAddContentLayout();
			} break;
			case R.id.btAddPlayAudio: {
				
				if(audioPath.equalsIgnoreCase("Unknow") == true || audioPath == null) {
					Toast.makeText(getApplicationContext(), "You must record first", Toast.LENGTH_LONG).show();
				} else {
					recordAudio.startPlaying();
					findViewById(R.id.bt_play_audio_effect).startAnimation(anim);
					findViewById(R.id.btPauseAudio).setVisibility(View.VISIBLE);
					findViewById(R.id.btAddPlayAudio).setVisibility(View.INVISIBLE);
					findViewById(R.id.bt_play_audio_effect).setVisibility(View.INVISIBLE);
					startPlayProgressUpdater();
					seekBar.setMax(recordAudio.getDuration());
				}
			} break;
			case R.id.btPauseAudio: {
				findViewById(R.id.bt_pause_audio_effect).startAnimation(anim);
				findViewById(R.id.btAddPlayAudio).setVisibility(View.VISIBLE);
				findViewById(R.id.btPauseAudio).setVisibility(View.INVISIBLE);
				break;
			}
			case R.id.btAddRecordAudio: {
				findViewById(R.id.bt_record_audio_effect).startAnimation(anim);
				findViewById(R.id.btStopRecordAudio).setVisibility(View.VISIBLE);
				findViewById(R.id.btAddRecordAudio).setVisibility(View.INVISIBLE);			
				recordAudio.startingRecord();
			} break;
			case R.id.btStopRecordAudio: {
				findViewById(R.id.bt_stop_record_audio_effect).startAnimation(anim);
				findViewById(R.id.btAddRecordAudio).setVisibility(View.VISIBLE);
				findViewById(R.id.btStopRecordAudio).setVisibility(View.INVISIBLE);
				recordAudio.stopRecord();
				audioPath = recordAudio.getAudioPath();
			} break;
			case R.id.btAddImage : {
				findViewById(R.id.audioAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.timeAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.videoAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.locationAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.imageAddLayout).setVisibility(View.VISIBLE);
				findViewById(R.id.imagebtImage).startAnimation(anim);
				openAddContentLayout();
			} break;
			case R.id.btSelectImage: {
				AlertDialog.Builder builder = new AlertDialog.Builder(btSelectImage.getContext());
				builder.setTitle(R.string.new_game_title);
				builder.setItems(R.array.Option_Name, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int position) {
						// TODO Auto-generated method stub
						if(position == 0) {
							openCameraForImage(1);
						}else {
							openCameraForImage(0);
						}
					}
				});
				builder.show();
				break;
			}
			case R.id.btSelectVideo: {
				AlertDialog.Builder builder = new AlertDialog.Builder(btSelectImage.getContext());
				builder.setTitle(R.string.new_game_title);
				builder.setItems(R.array.Option_Name, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int position) {
						// TODO Auto-generated method stub
						if(position == 0) {
							openCameraForVideo(2);
						}else {
							openCameraForVideo(4);
						}
					}
				});
				builder.show();
				break;
			}
			case R.id.btAddVideo : {
				findViewById(R.id.audioAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.timeAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.imageAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.locationAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.videoAddLayout).setVisibility(View.VISIBLE);
				findViewById(R.id.imagebtVideo).startAnimation(anim);
				openAddContentLayout();
			} break;
			case R.id.btCreateEvent : {
				time = String.format("%d-%d-%d", timePicker.getYear(),timePicker.getMonth()+1,timePicker.getDayOfMonth());
				if(etTitle.getText().toString().length() > 0 && etBody.getText().toString().length() > 0)
					try {
						newRecord = new Card(etTitle.getText().toString(), etBody.getText().toString(), audioPath.toString(), imagePath.toString(), videoPath.toString(), time.toString(),latitute,longitude);
						db.insertNewRecord(newRecord);
						Intent intent = new Intent();
						setResult(RESULT_OK, intent);
						findViewById(R.id.bt_create_effect).startAnimation(anim);
						finish();
					}catch(Exception exc) {
						Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
					}
				else {
					Toast.makeText(getApplicationContext(), "Make Sure You full fill the title field and body field", Toast.LENGTH_LONG).show();
				}
				break;
			} 
			case R.id.btAddTime : {
				findViewById(R.id.audioAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.imageAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.videoAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.locationAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.timeAddLayout).setVisibility(View.VISIBLE);
				findViewById(R.id.imagebtTime).startAnimation(anim);
				openAddContentLayout();
				break;
			} case R.id.btClear : {
				clearContentFunction();
				findViewById(R.id.bt_clear_effect).startAnimation(anim);
				break;
			} 
			
			case R.id.btAddLocation : {	
				findViewById(R.id.imagebtLocation).startAnimation(anim);
				findViewById(R.id.timeAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.audioAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.imageAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.videoAddLayout).setVisibility(View.INVISIBLE);
				findViewById(R.id.locationAddLayout).setVisibility(View.VISIBLE);
				
				openAddContentLayout();
				
				gps = new GPS(AddScreen.this);
				// check if GPS enabled
				if (gps.canGetLocation()) {
					latitute = gps.getLatitude() +"";
					longitude = gps.getLongitude() +"";
					Toast.makeText(getApplicationContext(), "Getting Done", Toast.LENGTH_LONG).show();
				} else {
					gps.showSettingsAlert();
				}
				break;
			}
			
	}
		
		
		btClear.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				if(v.getId() == R.id.btClear) {
					findViewById(R.id.title).requestFocus();
					findViewById(R.id.bt_clear_effect).startAnimation(anim);
					Toast.makeText(getApplicationContext(), "Clear All", Toast.LENGTH_SHORT).show();
					etBody.setText("");
					etTitle.setText("");
					imagePath = "Unknow";
					audioPath = "Unknow";
					audioPath = "Unknow";
					timePicker.updateDate(calender.year, calender.month, calender.monthDay);
					audioPath = "Unknow";
					seekBar.setProgress(0);
					findViewById(R.id.bt_pause_audio_effect).startAnimation(anim);
					findViewById(R.id.btAddPlayAudio).setVisibility(View.VISIBLE);
					findViewById(R.id.btPauseAudio).setVisibility(View.INVISIBLE);
					if(recordAudio.isPlaying())
						recordAudio.stopPlaying();
					imageView.setImageResource(R.drawable.nexus);
					btSelectImage.setVisibility(View.VISIBLE);
					videoView.setVisibility(View.INVISIBLE);
					btSelectVideo.setVisibility(View.VISIBLE);
					longitude = "Location not available";
					latitute = "Location not available";
				}
				return true;
			}
		});
		
		
	}
	private void clearContentFunction() {
		if(findViewById(R.id.timeAddLayout).getVisibility() == View.VISIBLE) {
			Toast.makeText(this, "reset time", Toast.LENGTH_SHORT).show();
			findViewById(R.id.title).requestFocus();
			timePicker.updateDate(calender.year, calender.month, calender.monthDay);
		}
		if(findViewById(R.id.audioAddLayout).getVisibility() == View.VISIBLE) {
			Toast.makeText(this, "clear audio", Toast.LENGTH_SHORT).show();
			audioPath = "Unknow";
			seekBar.setProgress(0);
			findViewById(R.id.bt_pause_audio_effect).startAnimation(anim);
			findViewById(R.id.btAddPlayAudio).setVisibility(View.VISIBLE);
			findViewById(R.id.btPauseAudio).setVisibility(View.INVISIBLE);
			if(recordAudio.isPlaying())
				recordAudio.stopPlaying();
		}
		if(findViewById(R.id.imageAddLayout).getVisibility() == View.VISIBLE) {
			Toast.makeText(this, "clear image", Toast.LENGTH_SHORT).show();
			imagePath = "Unknow";
			imageView.setImageResource(R.drawable.nexus);
			btSelectImage.setVisibility(View.VISIBLE);
		}
		if(findViewById(R.id.videoAddLayout).getVisibility() == View.VISIBLE) {
			Toast.makeText(this, "clear video", Toast.LENGTH_SHORT).show();
			videoPath = "Unknow";
			videoView.setVisibility(View.INVISIBLE);
			btSelectVideo.setVisibility(View.VISIBLE);
		}
		if(findViewById(R.id.locationAddLayout).getVisibility() == View.VISIBLE) {
			Toast.makeText(this, "clear location", Toast.LENGTH_SHORT).show();
			longitude = "Location not available";
			latitute = "Location not available";
		}
		
	}
	public void openCameraForImage(int which) {
		Intent intent = new Intent(AddScreen.this,PhotoCapture.class);
		intent.putExtra("key", which);
		startActivityForResult(intent, TAKE_PICTURE_CODE);
	}
	
	public void openCameraForVideo(int which) {
		Intent intent = new Intent(AddScreen.this,PhotoCapture.class);
		intent.putExtra("Video", which);
		startActivityForResult(intent, TAKE_VIDEO_CODE);
	}
	public void openAddContentLayout() {
		if(findViewById(R.id.AddContentLayout).getVisibility() != View.VISIBLE) {
			findViewById(R.id.AddContentLayout).setVisibility(View.VISIBLE);
			findViewById(R.id.AddContentLayout).startAnimation(animSlideUp);
		}
	}
	
	public void startPlayProgressUpdater() {
	    
		Runnable notification = new Runnable() {
			public void run() {
				seekBar.setProgress(recordAudio.getCurrentPosition());
				startPlayProgressUpdater();
				if(recordAudio.isPlaying() == false) {
					seekBar.setProgress(recordAudio.getDuration());
				}
			}
		};
		myhaHandler.postDelayed(notification,1000);
	}
	
}
