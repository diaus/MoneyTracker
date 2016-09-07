package com.andrew.moneytracker.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.andrew.moneytracker.App;
import com.andrew.moneytracker.R;
import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.DaoSession;
import com.andrew.moneytracker.database.ProductDao;
import com.andrew.moneytracker.database.Spending;
import com.andrew.moneytracker.database.SpendingDao;
import com.andrew.moneytracker.utils.helper;

/**
 * Created by andrew on 07.09.2016.
 */
public class SpendingFragment extends Fragment {

	private static final String ARG_ID = "id";

	private static final String SAVED_ACCOUNT_ID = "account_id";

	private static final int REQUEST_SELECT_ACCOUNT = 1;

	private static final String DIALOG_SELECT_ACCOUNT = "select_account";

	SpendingDao spendingDao;
	AccountDao accountDao;
	ProductDao productDao;

	boolean isCreating;
	Long spendingId;

	Button btnSave, btnCancel, btnSaveAndNew;
	Button btnAccount;
	EditText editProduct;
	EditText editSumBig, editSumSmall;

	Long mAccountId;

	public static SpendingFragment newInstance(Long id){
		SpendingFragment fragment = new SpendingFragment();
		Bundle args = new Bundle();
		helper.putLongBundle(args, ARG_ID, id);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// DATABASE
		DaoSession daoSession = ((App) getActivity().getApplication()).getDaoSession();
		spendingDao = daoSession.getSpendingDao();
		accountDao = daoSession.getAccountDao();
		productDao = daoSession.getProductDao();

		Bundle args = getArguments();
		spendingId = helper.getLongBundle(args, ARG_ID);
		isCreating = spendingId == null;

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		helper.putLongBundle(outState, SAVED_ACCOUNT_ID, mAccountId);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_spending, container, false);

		btnCancel = (Button) v.findViewById(R.id.button_cancel);
		btnSave = (Button) v.findViewById(R.id.button_save);
		btnSaveAndNew = (Button) v.findViewById(R.id.button_save_and_new);
		btnAccount = (Button) v.findViewById(R.id.button_account);
		editProduct = (EditText) v.findViewById(R.id.product);
		editSumBig = (EditText) v.findViewById(R.id.sum_big);
		editSumSmall = (EditText) v.findViewById(R.id.sum_small);

		Account account = null;
		if (savedInstanceState != null) {
			mAccountId = helper.getLongBundle(savedInstanceState, SAVED_ACCOUNT_ID);
		} else {
			// on first open
			if (isCreating){
				account = accountDao.queryBuilder().limit(1).unique();
				mAccountId = account != null ? account.getId() : null;
			} else {
				Spending spending = spendingDao.load(spendingId);
				mAccountId = spending.getAccountId();
				editProduct.setText(productDao.load(spending.getProductId()).getName());
				editSumBig.setText("" + spending.getCashBig());
				int sumSmall = spending.getCashSmall();
				editSumSmall.setText((sumSmall > 0 && sumSmall < 10 ? "0" : "") + sumSmall);
			}
		}
		if (account == null && mAccountId != null){
			account = accountDao.load(mAccountId);
		}
		// TODO: case when no accounts - create
		btnAccount.setText(account == null ? "no accounts found" : account.getName());

		btnSaveAndNew.setVisibility(isCreating ? View.VISIBLE : View.GONE);

		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doCancel();
			}
		});

		btnSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doSave();
			}
		});

		btnSaveAndNew.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doSaveAndNew();
			}
		});

		btnAccount.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doSelectAccount();
			}
		});

		return v;
	}

	private void doSelectAccount() {
		if (mAccountId == null) return;
		SelectAccountDialogFragment dialog = SelectAccountDialogFragment.newInstance(mAccountId);
		dialog.setTargetFragment(this, REQUEST_SELECT_ACCOUNT);
		dialog.show(getFragmentManager(), DIALOG_SELECT_ACCOUNT);
	}

	private void doSaveAndNew() {

	}

	private void doSave() {

	}

	private void doCancel() {

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SELECT_ACCOUNT && resultCode == Activity.RESULT_OK){
			mAccountId = SelectAccountDialogFragment.getResult(data);
			btnAccount.setText(accountDao.load(mAccountId).getName());
		}
	}

}
