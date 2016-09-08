package com.andrew.moneytracker.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrew.moneytracker.R;
import com.andrew.moneytracker.commons.IFactory;
import com.andrew.moneytracker.commons.SimpleListAdapter;
import com.andrew.moneytracker.commons.SimpleListView;
import com.andrew.moneytracker.commons.ISimpleViewHolder;
import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.utils.dbHelper;

/**
 * Created by andrew on 07.09.2016.
 */
public class AccountsFragment extends MainTabFragment {

	private static final String TAG = "m:AccountsFragment";

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_accounts, container, false);

		SimpleListView listAccounts = (SimpleListView) v.findViewById(R.id.accounts_list);

		listAccounts.setAdapter(new SimpleListAdapter<>(dbHelper.accountsList(accountDao()), new IFactory<ISimpleViewHolder>() {
					  @Override
					  public ISimpleViewHolder create() {
						  return new AccountViewHolder();
					  }
				  }),
				  R.layout.account_list_item);

		return v;
	}

	@Override
	public void onTabSelected() {
		// nothing currently
	}

	public class AccountViewHolder implements ISimpleViewHolder<Account> {
		TextView mName;
		Account mAccount;

		@Override
		public void bindView(View view) {
			mName = (TextView) view.findViewById(R.id.name);
		}

		@Override
		public void bindData(Account account) {
			mAccount = account;
			mName.setText(account.getName());
		}
	}


}
