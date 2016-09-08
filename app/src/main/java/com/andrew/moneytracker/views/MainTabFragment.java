package com.andrew.moneytracker.views;

import android.support.v4.app.Fragment;

import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.ProductDao;
import com.andrew.moneytracker.database.SpendingDao;

/**
 * Created by andrew on 07.09.2016.
 */
public abstract class MainTabFragment extends Fragment {

	public abstract void onTabSelected();

	public IMainActivity getActivityMain(){
		return (IMainActivity) getActivity();
	}

	public AccountDao accountDao(){
		return getActivityMain().getAccountDao();
	}

	public ProductDao productDao(){
		return getActivityMain().getProductDao();
	}

	public SpendingDao spendingDao(){
		return getActivityMain().getSpendingDao();
	}
}
