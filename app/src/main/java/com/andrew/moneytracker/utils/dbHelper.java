package com.andrew.moneytracker.utils;

import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.Product;
import com.andrew.moneytracker.database.ProductDao;
import com.andrew.moneytracker.database.Spending;
import com.andrew.moneytracker.database.SpendingDao;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by andrew on 07.09.2016.
 */
public class dbHelper {


	public static List<Account> accountsList(AccountDao accountDao) {
		return accountDao.queryBuilder().orderAsc(AccountDao.Properties.Name).list();
	}

	public static List<Product> productsList(ProductDao productDao) {
		List<Product> products = productDao.queryBuilder().orderAsc(ProductDao.Properties.NameNormalized).list();
		final Collator collator = Collator.getInstance();
		Collections.sort(products, new Comparator<Product>() {
			@Override
			public int compare(Product p1, Product p2) {
				return collator.compare(p1.getNameNormalized(), p2.getNameNormalized());
			}
		});
		return products;
	}

	public static Product resolveProduct(ProductDao productDao, String productText) {
		Product product = productDao.queryBuilder().where(ProductDao.Properties.NameNormalized.eq(productText.toLowerCase())).limit(1).unique();
		if (product == null){
			product = new Product(null, productText, productText.toLowerCase(), null);
			productDao.insert(product);
		}
		return product;
	}

	public static Spending createSpending(SpendingDao spendingDao, long productId, long accountId, int sum, String notes){
		Spending spending = new Spending(null, productId, accountId, sum, notes);
		spendingDao.insert(spending);
		return spending;
	}

	public static Spending updateSpending(SpendingDao spendingDao, long spendingId, long productId, long accountId, int sum, String notes){
		Spending spending = new Spending(spendingId, productId, accountId, sum, notes);
		spendingDao.update(spending);
		return spending;
	}

	public static Spending saveSpending(SpendingDao spendingDao, Long spendingId, long productId, long accountId, int sum, String notes){
		return spendingId == null ? createSpending(spendingDao, productId, accountId, sum, notes) : updateSpending(spendingDao, spendingId, productId, accountId, sum, notes);
	}
}
