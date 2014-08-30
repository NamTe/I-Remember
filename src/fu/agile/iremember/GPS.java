package fu.agile.iremember;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;


public class GPS extends Service implements LocationListener {
	private final Context context;
	boolean isGPSEnabled = false;
	boolean canGetLocation = false;
	Location location; // location
	double latitude; // latitude
	double longitude; // longitude
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
	protected LocationManager locationManager;
	
	public GPS(Context context) {
		this.context = context;
		getLocation();
	}
	
	
	public Location getLocation() {
		try {
			locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
			isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			if (!isGPSEnabled) {
				
			}
			else {
				this.canGetLocation = true;
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER,
						MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						if (locationManager != null) {
							location = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return location;
	}
	/**
	* Stop using GPS listener
	* Calling this function will stop using GPS in your app
	* */
	
	
	public void stopGPS(){
		if(locationManager != null){
		locationManager.removeUpdates(GPS.this);
	}
	}
	/**
	* Function to get latitude
	* */
	public double getLatitude(){
		if(location != null){
			latitude = location.getLatitude();
		}
		return latitude;
	}

	
	public double getLongitude(){
		if(location != null){
			longitude = location.getLongitude();
		}
	return longitude;
	}

	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	public void showSettingsAlert(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Settings");
		builder.setMessage("You need to enabled GPS?");
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				context.startActivity(intent);
			}
		});
		builder.show();
	}
	
	@Override
	public void onLocationChanged(Location location) {
		longitude=getLongitude();
		latitude=getLatitude();
	}
	
	@Override
	public void onProviderDisabled(String provider) {
	}
	
	@Override
	public void onProviderEnabled(String provider) {
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
}