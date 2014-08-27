package com.repkap11.skylighttest;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.location.GpsSatellite;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SatelliteAdapter extends ArrayAdapter<GpsSatellite> {

	private Context mContext;

	public SatelliteAdapter(Context context, int resourceId,
			List<GpsSatellite> satsArray) {
		super(context, resourceId, satsArray);
		this.mContext = context;

	}

	private class ViewHolder {
		TextView mTextMain;
		TextView mTextSub;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		GpsSatellite sat = getItem(position);
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.main_list_element, null);
			holder = new ViewHolder();
			holder.mTextMain = (TextView) convertView
					.findViewById(R.id.main_list_view_text_main);
			holder.mTextSub = (TextView) convertView
					.findViewById(R.id.main_list_view_text_sub);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.mTextMain.setText("ID: " + sat.getPrn() + "");
		holder.mTextSub.setText("Signal to noise: " + sat.getSnr() + "");
		return convertView;
	}

}
