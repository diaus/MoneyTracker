package com.andrew.moneytracker.utils;

import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.AccountDao;

import java.util.List;

/**
 * Created by andrew on 07.09.2016.
 */
public class dbHelper {


	public static List<Account> accountsList(AccountDao accountDao) {
		return accountDao.queryBuilder().orderAsc(AccountDao.Properties.Name).list();
	}
}
