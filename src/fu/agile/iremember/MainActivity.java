package fu.agile.iremember;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements  android.view.View.OnClickListener, OnItemLongClickListener {


	DataBase db;
	private ImageButton btAdd;
	private Animation anim;
	private ListView lw;
	private List<Card> RecordList;
	
	private AutoCompleteTextView autoComplete;
	private static int ACTION_ADD_EVENT = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		btAdd = (ImageButton)findViewById(R.id.btAdd);
		btAdd.setOnClickListener(this);
		checkFile();
		lw = (ListView)findViewById(R.id.lwListRemember);
		
		anim = AnimationUtils.loadAnimation(this, R.anim.zoom_animation);
		autoComplete = (AutoCompleteTextView)findViewById(R.id.etFilter);

		
		
		db = new DataBase(this);
		display();
		
		
		//Handle event
		autoComplete.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				displaySelect(arg0.getItemAtPosition(arg2).toString());
			}
		});
		lw.setOnItemLongClickListener(this);
		autoComplete.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if(autoComplete.getText().toString().length() == 0) {
					display();
				}
				return false;
			}
		});
	}
		
	
	public void checkFile() {
		File dir = new File(getString(R.string._mnt_sdcard_iremember));
		File phtotoDir = new File(getString(R.string._mnt_sdcard_iremember_photo));
		File videoDir = new File(getString(R.string._mnt_sdcard_iremember_video));
		File audioRecorder = new File(getString(R.string._mnt_sdcard_iremember_audio));
		if(dir.isDirectory() == false) {
		createNewDir(dir);
		}
		if(phtotoDir.isDirectory() == false) {
			createNewDir(phtotoDir);
		}
		if(videoDir.isDirectory() == false) {
			createNewDir(videoDir);
		}
		if(audioRecorder.isDirectory() == false) {
			createNewDir(audioRecorder);
		}
	}
	
	public void getStringAdapter() {
		List<String> stringAdapter;
		stringAdapter = new ArrayList<String>();
		for(Card d : RecordList) { 
			stringAdapter.add(d.getTitle());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,stringAdapter);
		autoComplete.setAdapter(adapter);
	}
	
	public void displaySelect(String Name) {
		List<Card> list = new ArrayList<Card>();
		for(Card d : RecordList) {
			if(d.getTitle().equalsIgnoreCase(Name)) {
				list.add(d);
			}
		}
		MyAdapter adapter = new MyAdapter(this, R.layout.list_customise, list);
		lw.setAdapter(adapter);
	}
	
	public void display() {	
		RecordList = new ArrayList<Card>();
		RecordList = db.getAllRecords();
		MyAdapter adapter = new MyAdapter(this, R.layout.list_customise, RecordList);
		lw.setAdapter(adapter);
		lw.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(MainActivity.this,ViewEvent.class);
				intent.putExtra("position", arg2);
				startActivityForResult(intent, ACTION_ADD_EVENT);
			}
		});
		getStringAdapter();
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
	}
	
	public void createNewDir(File newDir) {
		try {
			newDir.mkdir();
		} catch(Exception exc) {
			Toast.makeText(getApplicationContext(), "Error with sdcard " + newDir.getName(), Toast.LENGTH_LONG).show();
			finish();
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) { 
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
			case R.id.btAdd: {
				findViewById(R.id.bt_add_effect).startAnimation(anim);
				Intent intent = new Intent(MainActivity.this,AddScreen.class);
				startActivityForResult(intent, ACTION_ADD_EVENT);
			
			}break;
	
			default:
				break;
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View context, final int position,
			long arg3) {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context.getContext());
        builder1.setMessage("Are You Sure To Display");
        builder1.setCancelable(true);
        builder1.setPositiveButton("No",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	
                dialog.cancel();
            }
        });
        builder1.setNegativeButton("Yes",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	db.deleteRecord(RecordList.get(position));
            	display();
            	getStringAdapter();
                dialog.cancel();
            }
        });

        AlertDialog alert11 = builder1.create();
        alert11.show();
		return false;
	}
	
	@Override 
	protected void onRestart() {
		super.onRestart();
		autoComplete.setText("");
		display();
	}
}
