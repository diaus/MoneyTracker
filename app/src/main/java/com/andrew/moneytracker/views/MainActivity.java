package com.andrew.moneytracker.views;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.andrew.moneytracker.App;
import com.andrew.moneytracker.R;
import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.DaoSession;
import com.andrew.moneytracker.database.ProductDao;
import com.andrew.moneytracker.database.SpendingDao;

public class MainActivity extends AppCompatActivity implements IMainActivity {
	private static final String TAG = "m:MainActivity";

	private DaoSession daoSession;
	private AccountDao accountDao;
	private ProductDao productDao;
	private SpendingDao spendingDao;

	private DrawerLayout mDrawerLayout;
	private NavigationView mNavigationView;
	private ActionBar mActionBar;
	private ViewPager mViewPager;
	private FragmentStatePagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// DATABASE
		daoSession = ((App) getApplication()).getDaoSession();

		accountDao = daoSession.getAccountDao();
		productDao = daoSession.getProductDao();
		spendingDao = daoSession.getSpendingDao();

		mActionBar = getSupportActionBar();
		mActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
		mActionBar.setDisplayHomeAsUpEnabled(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mAdapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				Log.d(TAG, "create tab fragment " + position);
				switch (position) {
					case 0:
						return new BlotterFragment();
					case 1:
						return new ProductsFragment();
					case 2:
						return new AccountsFragment();
				}
				return null;
			}

			@Override
			public int getCount() {
				return 3;
			}

			@Override
			public CharSequence getPageTitle(int position) {
				switch (position) {
					case 0:
						return getString(R.string.tab_title_blotter);
					case 1:
						return getString(R.string.tab_title_products);
					case 2:
						return getString(R.string.tab_title_accounts);
				}
				return null;
			}
		};

		mViewPager = (ViewPager) findViewById(R.id.tabs_pager);
		mViewPager.setAdapter(mAdapter);

		TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs_layout);
		tabLayout.setupWithViewPager(mViewPager);

		mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
		mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
			@Override
			public boolean onNavigationItemSelected(@NonNull MenuItem item) {
				int tabIndex;
				switch (item.getItemId()){
					case R.id.navigation_item_blotter: tabIndex = 0; break;
					case R.id.navigation_item_products: tabIndex = 1; break;
					case R.id.navigation_item_accounts: tabIndex = 2; break;
					default: return false;
				}
				mDrawerLayout.closeDrawers();
				mViewPager.setCurrentItem(tabIndex);
				return true;
			}
		});

		mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
			@Override
			public void onPageSelected(int position) {
				Log.d(TAG, "tab selected " + position);
				switch (position){
					case 0: mNavigationView.setCheckedItem(R.id.navigation_item_blotter); break;
					case 1: mNavigationView.setCheckedItem(R.id.navigation_item_products); break;
					case 2: mNavigationView.setCheckedItem(R.id.navigation_item_accounts); break;
				}
				mActionBar.setSubtitle(null);
				getTab(position).onTabSelected();
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private MainTabFragment getTab(int position){
		return (MainTabFragment) mAdapter.instantiateItem(mViewPager, position);
	}

	@Override
	public AccountDao getAccountDao() {
		return accountDao;
	}

	@Override
	public ProductDao getProductDao() {
		return productDao;
	}

	@Override
	public SpendingDao getSpendingDao() {
		return spendingDao;
	}
}
