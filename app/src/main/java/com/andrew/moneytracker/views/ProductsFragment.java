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
public class ProductsFragment extends MainTabFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_products, container, false);

		return v;
	}
}
