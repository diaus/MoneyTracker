package com.andrew.moneytracker;

import android.app.Application;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.DaoMaster;
import com.andrew.moneytracker.database.DaoSession;
import com.andrew.moneytracker.database.MyDatabaseOpenHelper;

import org.greenrobot.greendao.database.Database;

import java.util.Locale;

/**
 * Created by andrew on 07.09.2016.
 */
public class App extends Application {

	private DaoSession daoSession;

	@Override
	public void onCreate() {
		super.onCreate();
		setLocale();

		// DATABASE
		MyDatabaseOpenHelper helper = new MyDatabaseOpenHelper(this, "moneytracker-db");
		Database db = helper.getEncryptedWritableDb("cyFr5a,It57lkem2$k-hJ^ad*");
		daoSession = new DaoMaster(db).newSession();

		if (helper.isJustCreated()){
			AccountDao dao = daoSession.getAccountDao();
			boolean isUkraine = Locale.getDefault().getLanguage().equals("uk");
			dao.insert(new Account(null, isUkraine ? "Гаманець" : "Wallet"));
			dao.insert(new Account(null, isUkraine ? "Картка" : "Card"));
		}
	}

	private void setLocale() {
		Locale defaultLocale = Locale.getDefault();
		String lang = defaultLocale.getLanguage();
		if (!lang.equals("ru")) return;

		Locale locale = new Locale("uk");
		Locale.setDefault(locale);

		Resources resources = getResources();

		Configuration configuration = resources.getConfiguration();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			configuration.setLocale(locale);
		} else {
			configuration.locale = locale;
		}

		resources.updateConfiguration(configuration, resources.getDisplayMetrics());
	}

	public DaoSession getDaoSession() {
		return daoSession;
	}
}
