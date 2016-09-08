package com.andrew.moneytracker.utils;

import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.Product;
import com.andrew.moneytracker.database.ProductDao;
import com.andrew.moneytracker.database.Spending;
import com.andrew.moneytracker.database.SpendingDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
		sortProducts(products);
		return products;
	}

	public static Product resolveProduct(ProductDao productDao, String productText) {
		Product product = productDao.queryBuilder().where(ProductDao.Properties.NameNormalized.eq(productText.toLowerCase())).limit(1).unique();
		if (product == null) {
			product = new Product(null, productText, productText.toLowerCase(), null);
			productDao.insert(product);
		}
		return product;
	}

	public static Spending createSpending(SpendingDao spendingDao, Date date, long productId, long accountId, int sum, String notes) {
		Spending spending = new Spending(null, productId, accountId, sum, date, notes);
		spendingDao.insert(spending);
		return spending;
	}

	public static Spending updateSpending(SpendingDao spendingDao, long spendingId, Date date, long productId, long accountId, int sum, String notes) {
		Spending spending = new Spending(spendingId, productId, accountId, sum, date, notes);
		spendingDao.update(spending);
		return spending;
	}

	public static Spending saveSpending(SpendingDao spendingDao, Long spendingId, Date date, long productId, long accountId, int sum, String notes) {
		return spendingId == null ? createSpending(spendingDao, date, productId, accountId, sum, notes) : updateSpending(spendingDao, spendingId, date, productId, accountId, sum, notes);
	}

	public static List<Product> searchProductsSuggestions(ProductDao productDao, String filter, int maxCount) {
		try {
			filter = filter.trim().toLowerCase();
			if (filter.length() == 0) return null;
			List<Product> result = productDao.queryBuilder().where(ProductDao.Properties.NameNormalized.like(filter + "%"))
					  .limit(maxCount).list();
			sortProducts(result);
			if (result.size() < maxCount) {
				List<Long> ids = new ArrayList<>();
				for (Product p : result) {
					ids.add(p.getId());
				}
				List<Product> result2 = productDao.queryBuilder()
						  .where(ProductDao.Properties.Id.notIn(ids))
						  .where(ProductDao.Properties.NameNormalized.like("%" + filter + "%"))
						  .limit(maxCount - result.size()).list();
				sortProducts(result2);
				result.addAll(result2);
			}
			return result;
		} catch (Exception e) {
			return null;
		}
	}


	private static Comparator<Product> productsComparator = new Comparator<Product>() {
		@Override
		public int compare(Product p1, Product p2) {
			return helper.collator.compare(p1.getNameNormalized(), p2.getNameNormalized());
		}
	};

	private static void sortProducts(List<Product> products) {
		Collections.sort(products, productsComparator);
	}
}
