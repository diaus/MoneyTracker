package com.andrew.moneytracker.views;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.andrew.moneytracker.R;
import com.andrew.moneytracker.commons.SingleFragmentActivity;
import com.andrew.moneytracker.utils.helper;

/**
 * Created by andrew on 07.09.2016.
 */
public class SpendingActivity extends SingleFragmentActivity {

	private static final String EXTRA_ID = "id";

	public static Intent newIntent(Context context, Long id) {
		Intent intent = new Intent(context, SpendingActivity.class);
		helper.putLongExtra(intent, EXTRA_ID, id);
		return intent;
	}

	@Override
	protected Fragment createFragment() {
		return SpendingFragment.newInstance(helper.getLongExtra(getIntent(), EXTRA_ID));
	}

	@Override
	protected int getLayoutResId() {
		return R.layout.activity_single_fragment;
	}

	@Override
	protected int getFragmentResId() {
		return R.id.fragmentContainer;
	}
}
