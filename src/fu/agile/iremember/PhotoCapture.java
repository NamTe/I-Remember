package fu.agile.iremember;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class PhotoCapture extends Activity implements OnClickListener {

	private Button btCancel;
	private String photoPath;
	private String videoPath;
	private int actionCase;
	private int caseIntent;
	private Uri mVideoUri;
	
	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_TAKE_PHOTO = 1;
	static final int REQUEST_PICK_PHOTO = 100;
	static final int ACTION_TAKE_VIDEO = 2;
	static final int REQUEST_PICK_VIDEO = 3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_capture);
		btCancel = (Button) findViewById(R.id.btCancelSaveImage);
		btCancel.setOnClickListener(this);
		Intent intent = getIntent();
		caseIntent = intent.getIntExtra("key",-1) == -1 ? intent.getIntExtra("Video", -1) : intent.getIntExtra("key",-1);
		if(caseIntent == 1) {
			takePicture();
			actionCase = 0;
		} else if(caseIntent == 0) {
			getPictureFormGallery();
			actionCase = 1;
		} else if(caseIntent == 2){
			TakeVideoIntent();
			actionCase = 2;
		} else {
			getVideoFromGallery();
			actionCase = 3;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("resultOfPhoto",photoPath);		
			setResult(RESULT_OK,returnIntent);     
	    }
		else if(requestCode == REQUEST_PICK_PHOTO && data != null && data.getData() != null) {
	        Uri _uri = data.getData();
	        //User had pick an image.
	        Cursor cursor = getContentResolver().query(_uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	        cursor.moveToFirst();
	        //Link to the image
	        photoPath = cursor.getString(0);
	        cursor.close();
	        Intent returnIntent = new Intent();
			returnIntent.putExtra("resultOfPhoto",photoPath);		
			setResult(RESULT_OK,returnIntent);     
	    } else if(requestCode == ACTION_TAKE_VIDEO && data != null && data.getData() != null) {
			Intent returnIntent = new Intent();
			returnIntent.putExtra("resultOfVideo",videoPath);			
			setResult(RESULT_OK,returnIntent);     
	    } else if(requestCode == REQUEST_PICK_VIDEO && data != null && data.getData() != null) {
	    	mVideoUri = data.getData();
			Cursor cursor = getContentResolver().query(mVideoUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
	        cursor.moveToFirst();
	        videoPath = cursor.getString(0);
	        cursor.close();
	        Intent returnIntent = new Intent();
			returnIntent.putExtra("resultOfVideo",videoPath);			
			setResult(RESULT_OK,returnIntent);     
	    }
		finish();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo_capture, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void takePicture() {
		File photoFile = null;
		try {
			photoFile = createNewPhotoFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Can not use datastorage", Toast.LENGTH_LONG).show();
			finish();
		}
		Intent takePictureByIntend = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if(takePictureByIntend.resolveActivity(getPackageManager()) != null) {
			takePictureByIntend.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
			startActivityForResult(takePictureByIntend, REQUEST_IMAGE_CAPTURE);
			galleryAddPic();
		}
	}
	
	public File createNewPhotoFile() throws IOException {
		String photoTempName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String photoName = "AD_" + photoTempName + "_";
		File dir = new File(getString(R.string._mnt_sdcard_iremember_photo));
		File image = File.createTempFile(photoName, ".JPG",dir);
		photoPath = image.getAbsolutePath();
		return image;
	}
	
	
	public File createVideoFile() throws IOException {
		String photoTempName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String photoName = "AD_VD_" + photoTempName + "_";
		File dir = new File(getString(R.string._mnt_sdcard_iremember_video));
		File image = File.createTempFile(photoName, ".mp4",dir);
		return image;
	}	
	public void getPictureFormGallery () {
		Intent takePictureByIntent;
		if(Build.VERSION.SDK_INT < 19 ) {
			takePictureByIntent = new Intent(Intent.ACTION_GET_CONTENT);
		} else {
			takePictureByIntent = new Intent(Intent.ACTION_PICK);
		}
		takePictureByIntent.setType("image/*");
		startActivityForResult(takePictureByIntent, REQUEST_PICK_PHOTO);  
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.btCancelSaveImage : {
				if(actionCase == 0) {
					File delete = new File(photoPath);
					delete.delete();
				}
				if(actionCase == 3) {
					File delete = new File(videoPath);
					delete.delete();
				}
				Intent returnIntent = new Intent();
				setResult(RESULT_CANCELED,returnIntent); 
				finish();
				break;
			}
		}
	}
	
	private void galleryAddPic() {
	    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
	    File f = new File(photoPath);
	    Uri contentUri = Uri.fromFile(f);
	    mediaScanIntent.setData(contentUri);
	    this.sendBroadcast(mediaScanIntent);
	}
	
	private void TakeVideoIntent() {
		File Video = null;
		try {
			Video = createVideoFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Can not use datastorage", Toast.LENGTH_LONG).show();
			finish();
		}
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(Video));
		startActivityForResult(takeVideoIntent, ACTION_TAKE_VIDEO);
		videoPath = Video.getAbsolutePath();
	}
	
	public void getVideoFromGallery() {
		Intent takeVideo ;
		if(Build.VERSION.SDK_INT < 19) {
			takeVideo = new Intent(Intent.ACTION_GET_CONTENT);
		}
		else {
			takeVideo = new Intent(Intent.ACTION_PICK);
		}
		takeVideo.setType("video/*");
		startActivityForResult(takeVideo, REQUEST_PICK_VIDEO);  
	}
	
}
