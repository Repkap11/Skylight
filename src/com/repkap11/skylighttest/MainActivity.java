package com.repkap11.skylighttest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements GpsStatus.Listener, LocationListener {

	private class MyPhoneStateListener extends PhoneStateListener {

		@Override
		public void onSignalStrengthsChanged(SignalStrength signalStrength) {
			mCellStrength.setText("GsmSignalStrength(0 low, 31 high):" + signalStrength.getGsmSignalStrength());
			super.onSignalStrengthsChanged(signalStrength);
		}

	}

	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView mSatList;
	private LocationManager mLocationManager;
	private TextView mAccuracy;
	private TextView mCellStrength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mAccuracy = (TextView) findViewById(R.id.main_accuracy_text);
		mCellStrength = (TextView) findViewById(R.id.main_cell_strength_text);

		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		mLocationManager.addGpsStatusListener(this);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,//
				1000L,// update every 1000 milliseconds
				1.0f,// or every 1 meter change in position
				this);
		onLocationChanged(mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		boolean isGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isGPS) {
			Toast.makeText(this, "Please turn on GPS", Toast.LENGTH_LONG).show();
		}
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onGpsStatusChanged(int event) {
		GpsStatus gpsStatus = mLocationManager.getGpsStatus(null);
		if (gpsStatus != null) {
			Iterator<GpsSatellite> sat = gpsStatus.getSatellites().iterator();
			List<GpsSatellite> satsArray = new ArrayList<GpsSatellite>();
			while (sat.hasNext()) {
				GpsSatellite curSatellite = sat.next();
				if (curSatellite.usedInFix()) {
					satsArray.add(curSatellite);
				}
			}
			mSatList = (ListView) this.findViewById(R.id.main_list_view);
			Toast.makeText(this, "Num Sats:" + satsArray.size(), Toast.LENGTH_SHORT).show();
			mSatList.setAdapter(new SatelliteAdapter(this, R.layout.main_list_element, satsArray));
		} else {
			Toast.makeText(this, "GpsStatus updated with null.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		mAccuracy.setText(//
				"Accuracy: " + (location == null ? "null " : location.getAccuracy()) + "meters\n"//
						+ "Lat:" + location.getLatitude() + " Long:" + location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		//Log.e(TAG, "onProviderDisabled");
		Toast.makeText(this, "onProviderDisabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onProviderEnabled(String provider) {
		//Log.e(TAG, "onProviderEnabled");
		Toast.makeText(this, "onProviderEnabled", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		//Log.e(TAG, "onStatusChanged");
		Toast.makeText(this, "onStatusChanged", Toast.LENGTH_SHORT).show();

	}
}
