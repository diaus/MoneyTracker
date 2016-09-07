package com.andrew.moneytracker.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.andrew.moneytracker.R;

/**
 * Created by andrew on 07.09.2016.
 */
public class BlotterFragment extends MainTabFragment {
	private static final int REQUEST_NEW_SPENDING = 1;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_blotter, container, false);

		v.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(SpendingActivity.newIntent(getContext(), null), REQUEST_NEW_SPENDING);
			}
		});

		return v;
	}
}
