package com.andrew.moneytracker.views;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.andrew.moneytracker.App;
import com.andrew.moneytracker.R;
import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.AccountDao;
import com.andrew.moneytracker.database.DaoSession;
import com.andrew.moneytracker.database.Product;
import com.andrew.moneytracker.database.ProductDao;
import com.andrew.moneytracker.database.Spending;
import com.andrew.moneytracker.database.SpendingDao;
import com.andrew.moneytracker.utils.dbHelper;
import com.andrew.moneytracker.utils.helper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by andrew on 07.09.2016.
 */
public class SpendingFragment extends Fragment {

	private static final String ARG_ID = "id";

	private static final String SAVED_ACCOUNT_ID = "account_id";
	private static final String SAVED_DATE = "date";

	private static final int REQUEST_SELECT_ACCOUNT = 1;

	private static final String DIALOG_SELECT_ACCOUNT = "select_account";

	SpendingDao spendingDao;
	AccountDao accountDao;
	ProductDao productDao;

	boolean isCreating;
	Long spendingId;

	Button btnSave, btnCancel, btnSaveAndNew;
	Button btnAccount;
	Button btnDate, btnTime;
	AutoCompleteTextView editProduct;
	EditText editNotes;
	EditText editSumBig, editSumSmall;

	Long mAccountId;
	Date mDate;

	public static SpendingFragment newInstance(Long id) {
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
		outState.putLong(SAVED_DATE, mDate.getTime());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_spending, container, false);

		btnCancel = (Button) v.findViewById(R.id.button_cancel);
		btnSave = (Button) v.findViewById(R.id.button_save);
		btnSaveAndNew = (Button) v.findViewById(R.id.button_save_and_new);
		btnAccount = (Button) v.findViewById(R.id.button_account);
		btnDate = (Button) v.findViewById(R.id.button_date);
		btnTime = (Button) v.findViewById(R.id.button_time);
		editProduct = (AutoCompleteTextView) v.findViewById(R.id.product);
		editNotes = (EditText) v.findViewById(R.id.notes);
		editSumBig = (EditText) v.findViewById(R.id.sum_big);
		editSumSmall = (EditText) v.findViewById(R.id.sum_small);

		Account account = null;
		if (savedInstanceState != null) {
			mAccountId = helper.getLongBundle(savedInstanceState, SAVED_ACCOUNT_ID);
			mDate = new Date(savedInstanceState.getLong(SAVED_DATE));
		} else {
			// on first open
			if (isCreating) {
				mDate = Calendar.getInstance().getTime();
				account = accountDao.queryBuilder().limit(1).unique();
				mAccountId = account != null ? account.getId() : null;
			} else {
				Spending spending = spendingDao.load(spendingId);
				mDate = spending.getDate();
				mAccountId = spending.getAccountId();
				editProduct.setText(productDao.load(spending.getProductId()).getName());
				editNotes.setText(productDao.load(spending.getProductId()).getName());
				editSumBig.setText("" + spending.getCashBig());
				int sumSmall = spending.getCashSmall();
				if (sumSmall > 0) {
					editSumSmall.setText((sumSmall > 0 && sumSmall < 10 ? "0" : "") + sumSmall);
				}
			}
		}
		if (account == null && mAccountId != null) {
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

		editProduct.setAdapter(new ProductSearchAdapter(getContext()));
		editProduct.setThreshold(1);

		btnDate.setText(helper.formatShortDate(mDate));
		btnTime.setText(helper.formatShortTime(mDate));

		btnDate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickDate();
			}
		});
		btnTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickTime();
			}
		});

		return v;
	}

	private void pickTime() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(mDate);
		new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
				cal.set(Calendar.MINUTE, minute);
				mDate = cal.getTime();
				btnTime.setText(helper.formatShortTime(mDate));
			}
		}, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext())).show();
	}

	private void pickDate() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(mDate);
		new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, monthOfYear);
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				mDate = cal.getTime();
				btnDate.setText(helper.formatShortDate(mDate));
			}
		}, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
	}

	private void doSelectAccount() {
		if (mAccountId == null) return;
		SelectAccountDialogFragment dialog = SelectAccountDialogFragment.newInstance(mAccountId);
		dialog.setTargetFragment(this, REQUEST_SELECT_ACCOUNT);
		dialog.show(getFragmentManager(), DIALOG_SELECT_ACCOUNT);
	}

	private void doSaveAndNew() {
		if (saveSpending()) {
			String product = editProduct.getText().toString();
			editProduct.setText("");
			editNotes.setText("");
			editSumBig.setText("");
			editSumSmall.setText("");
			helper.focusAndShowKeyboard(getContext(), editProduct);
			Toast.makeText(getContext(), String.format(getString(isCreating ? R.string.spending_1s_created : R.string.spending_1s_updated), product)
					  , Toast.LENGTH_SHORT).show();
		}
	}

	private void doSave() {
		if (saveSpending()) {
			getActivity().setResult(Activity.RESULT_OK);
			getActivity().finish();
		}
	}

	private boolean saveSpending() {
		if (!validate()) return false;
		Product product = dbHelper.resolveProduct(productDao, helper.trimEdit(editProduct));
		editProduct.setText(product.getName());
		dbHelper.saveSpending(spendingDao, spendingId, mDate, product.getId(), mAccountId, getSum(), helper.trimEdit(editNotes));
		return true;
	}

	private boolean validate() {

		if (isEmpty(editProduct)) {
			helper.focusAndShowKeyboard(getContext(), editProduct);
			Toast.makeText(getContext(), R.string.should_enter_product, Toast.LENGTH_SHORT).show();
			return false;
		}

		if (getSum() == 0) {
			helper.focusAndShowKeyboard(getContext(), editSumBig);
			Toast.makeText(getContext(), R.string.should_enter_sum, Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private boolean isEmpty(EditText edit) {
		return helper.trimEdit(edit).length() == 0;
	}

	private void doCancel() {
		getActivity().finish();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_SELECT_ACCOUNT && resultCode == Activity.RESULT_OK) {
			mAccountId = SelectAccountDialogFragment.getResult(data);
			btnAccount.setText(accountDao.load(mAccountId).getName());
		}
	}

	public int getSum() {
		int sum = 0;
		String s = editSumBig.getText().toString();
		if (s.length() > 0) {
			sum = Integer.parseInt(s) * 100;
		}
		s = editSumSmall.getText().toString();
		if (s.length() > 0) {
			if (s.length() == 1) {
				sum += Integer.parseInt(s) * 10;
			} else {
				sum += Integer.parseInt(s);
			}
		}
		return sum;
	}

	class ProductSearchAdapter extends ArrayAdapter<Product> {

		private int viewResourceId;
		private final int textViewId;
		private LayoutInflater layoutInflater;

		@Override
		public Filter getFilter() {
			return mFilter;
		}

		private Filter mFilter = new Filter() {

			@Override
			public CharSequence convertResultToString(Object resultValue) {
				Product product = (Product)resultValue;
				Log.d("m:convertResultToString", product.getName());
				return product.getName();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();

				if (constraint != null) {
					List<Product> suggestions = dbHelper.searchProductsSuggestions(productDao, constraint.toString(), 5);
					results.values = suggestions;
					results.count = suggestions.size();
				}

				return results;
			}

			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				clear();
				if (results != null && results.count > 0) {
					addAll((List<Product>) results.values);
				}
				notifyDataSetChanged();
			}
		};

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;

			if (view == null) {
				view = layoutInflater.inflate(viewResourceId, null);
			}

			Product product = getItem(position);

			TextView field = (TextView) view.findViewById(textViewId);
			field.setText(product.getName());

			return view;
		}

		public ProductSearchAdapter(Context context) {
			super(context, R.layout.dropdown_list_item);
			viewResourceId = R.layout.dropdown_list_item;
			textViewId = android.R.id.text1;
			layoutInflater = LayoutInflater.from(context);
		}
	}
}
