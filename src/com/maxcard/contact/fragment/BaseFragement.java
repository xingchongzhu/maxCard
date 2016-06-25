package com.maxcard.contact.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragement extends Fragment{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		return null;
	}
	public boolean onCreateOptionsMenu(Menu menu ) {
		return true;
	}
}
