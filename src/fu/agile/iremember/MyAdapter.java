package fu.agile.iremember;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<Card> {

	List<Card> list = null;
	int layoutid;
	Activity myAcitivity = null;
	public MyAdapter(Activity context, int resource, List<Card> objects) {
		super(context, resource, objects);
		// TODO Auto-generated constructor stub
		myAcitivity = context;
		layoutid = resource;
		list = objects;
	}
	
	public View getView(int position,View convertView, ViewGroup parent ) {
		if(convertView == null) {
			LayoutInflater inflater = myAcitivity.getLayoutInflater();
			convertView = inflater.inflate(layoutid, null);
		} 
		Card card = list.get(position);
		ImageView lwCustomeImageView= (ImageView) convertView.findViewById(R.id.lwCustomeImageView);
		String imagePath = card.getImageFile();
		if(imagePath.equalsIgnoreCase("unknow") == false) {
			Bitmap bit = BitmapFactory.decodeFile(imagePath);
			lwCustomeImageView.setImageBitmap(bit);
		}else {
			lwCustomeImageView.setImageResource(R.drawable.nexus);
		}
		
		TextView tvCustomeStoryName = (TextView) convertView.findViewById(R.id.tvCustomeStoryName);
		tvCustomeStoryName.setText(card.getTitle());
		TextView twCustomeTime = (TextView) convertView.findViewById(R.id.twCustomeTime);
		twCustomeTime.setText(card.getTime());
		return convertView;
	}

}
