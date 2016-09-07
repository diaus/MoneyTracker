package com.andrew.moneytracker.views;

import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.ProductDao;
import com.andrew.moneytracker.database.SpendingDao;

/**
 * Created by andrew on 07.09.2016.
 */
public interface IMainActivity {
	AccountDao getAccountDao();
	ProductDao getProductDao();
	SpendingDao getSpendingDao();
}
