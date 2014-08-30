package fu.agile.iremember;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {

	private static int DATABASE_VERSION = 2;
	private static final String DATABASE_NAME = "NEWRecordManager";
	private static final String TABLE_NAME = "NEWRecord";
	private static final String KEY_ID = "id";
	private static final String KEY_TILE = "title";
    private static final String KEY_BODY = "body";
    private static final String KEY_TIME = "time";
    private static final String KEY_AUDIO_FILE = "audio_path";
    private static final String KEY_VIDEO_FILE = "video_path";
    private static final String KEY_IMAGE_FILE = "image_path";
    private static final String KEY_LATITUTE = "latitute";
    private static final String KEY_LONGITUDE = "longitude";
	public DataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String createTable = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TILE + " TEXT," + KEY_BODY + " TEXT,"
		+ KEY_TIME + " TEXT," + KEY_AUDIO_FILE + " TEXT," + KEY_IMAGE_FILE + " TEXT," + KEY_VIDEO_FILE + " TEXT," + KEY_LATITUTE + " TEXT," + KEY_LONGITUDE + " TEXT" + ")";
		db.execSQL(createTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	public void insertNewRecord(Card newRecord) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TILE, newRecord.getTitle());
		values.put(KEY_BODY, newRecord.getBody());
		values.put(KEY_TIME, newRecord.getTime());
		values.put(KEY_AUDIO_FILE, newRecord.getAudioFile());
		values.put(KEY_IMAGE_FILE, newRecord.getImageFile());
		values.put(KEY_VIDEO_FILE, newRecord.getVideoFile());
		values.put(KEY_LATITUTE, newRecord.getLatitute());
		values.put(KEY_LONGITUDE, newRecord.getLongitude());
		db.insert(TABLE_NAME, null, values);
		db.close();
	}
	
	public List<Card> getAllRecords() {
        List<Card> recordList = new ArrayList<Card>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	int i = 0;
            	Card newRecord = new Card();
            	newRecord.setID(Integer.parseInt(cursor.getString(i++)));
                newRecord.setTitle(cursor.getString(i++));
                newRecord.setBody(cursor.getString(i++));
                newRecord.setStoryTime(cursor.getString(i++));
                newRecord.setAudioFile(cursor.getString(i++));
                newRecord.setImageFile(cursor.getString(i++));
                newRecord.setVideoFile(cursor.getString(i++));
                newRecord.setLatitute(cursor.getString(i++));
                newRecord.setLongitude(cursor.getString(i++));
                // Adding contact to list
                recordList.add(newRecord);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return recordList;
    }
	
	public void deleteRecord(Card mRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?",
                new String[] { String.valueOf(mRecord.getID()) });
        db.close();
    }

	public int getRecordCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
	
	public void UPDATE_Record(Card card, int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TILE, card.getTitle());
		values.put(KEY_BODY, card.getBody());
		values.put(KEY_TIME, card.getTime());
		values.put(KEY_AUDIO_FILE,card.getAudioFile());
		values.put(KEY_VIDEO_FILE, card.getVideoFile());
		values.put(KEY_IMAGE_FILE, card.getImageFile());
		values.put(KEY_LATITUTE, card.getLatitute());
		values.put(KEY_LONGITUDE, card.getLongitude());
		db.update(TABLE_NAME, values, "id = " + id, null);
	}
	
}
