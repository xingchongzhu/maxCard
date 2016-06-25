package com.maxcard.contact.fragment;

import com.maxcard.contact.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class ShowScannerResaultFragement extends Fragment{

	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_codemessage, container, false);
		TextView feildt=(TextView)view.findViewById(R.id.filedcode);
		TextView title=(TextView)view.findViewById(R.id.textmmm);
		Bundle data = getArguments();
		String BookCode = data.getString("string");
		title.setText("二维码中藏着内容");
		feildt.setText(BookCode);
		return view;
	}
	public boolean onCreateOptionsMenu(Menu menu ) {
		return true;
	}
}
