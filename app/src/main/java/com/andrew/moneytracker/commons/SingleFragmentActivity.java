package com.andrew.moneytracker.commons;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by andrew on 15.08.2016.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

	protected abstract Fragment createFragment();

	@LayoutRes
	protected abstract int getLayoutResId();

	@IdRes
	protected abstract int getFragmentResId();

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(getLayoutResId());

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(getFragmentResId());
		if (fragment == null) {
			fragment = createFragment();
			fm.beginTransaction()
					  .add(getFragmentResId(), fragment)
					  .commit();
		}

	}

}
