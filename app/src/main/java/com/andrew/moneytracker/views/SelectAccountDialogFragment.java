package com.andrew.moneytracker.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.andrew.moneytracker.App;
import com.andrew.moneytracker.R;
import com.andrew.moneytracker.commons.IFactory;
import com.andrew.moneytracker.commons.ISimpleViewHolder;
import com.andrew.moneytracker.commons.SimpleListAdapter;
import com.andrew.moneytracker.commons.SimpleListView;
import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.DaoSession;
import com.andrew.moneytracker.utils.dbHelper;
import com.andrew.moneytracker.utils.helper;

/**
 * Created by andrew on 07.09.2016.
 */
public class SelectAccountDialogFragment extends DialogFragment {
	private static final String ARG_ID = "id";

	public static SelectAccountDialogFragment newInstance(long id){
		SelectAccountDialogFragment fragment = new SelectAccountDialogFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_ID, id);
		fragment.setArguments(args);
		return fragment;
	}

	long mAccountId;

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_select_account, null);

		mAccountId = getArguments().getLong(ARG_ID);

		DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();

		SimpleListView listAccounts = (SimpleListView) v.findViewById(R.id.accounts_list);

		listAccounts.setAdapter(new SimpleListAdapter<>(dbHelper.accountsList(daoSession.getAccountDao()), new IFactory<ISimpleViewHolder>() {
			@Override
			public ISimpleViewHolder create() {
				return new AccountViewHolder();
			}
		}), R.layout.dialog_select_account_item);

		AlertDialog dialog = new AlertDialog.Builder(getActivity())
				  .setView(v)
				  .setTitle(R.string.dialog_select_account_title)
				  .setNegativeButton(android.R.string.cancel, null)
				  .create();

		return dialog;
	}

	public class AccountViewHolder implements ISimpleViewHolder<Account> {
		Button btnAccount;
		Account mAccount;

		@Override
		public void bindView(View view) {
			btnAccount = (Button) view.findViewById(R.id.btnAccount);
			btnAccount.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					doSelectAccount(mAccount.getId());
				}
			});
		}

		@Override
		public void bindData(Account account) {
			mAccount = account;
			btnAccount.setText(account.getName());
			if (mAccountId == account.getId()){
				btnAccount.setTypeface(btnAccount.getTypeface(), Typeface.BOLD_ITALIC);
			} else {
				btnAccount.setTypeface(Typeface.DEFAULT);
			}
		}
	}

	private void doSelectAccount(long accountId) {
		if (accountId != mAccountId){
			Intent result = new Intent();
			result.putExtra(ARG_ID, accountId);
			getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, result);
		}
		getDialog().dismiss();
	}

	public static Long getResult(Intent data){
		return helper.getLongExtra(data, ARG_ID);
	}
}
