package com.andrew.moneytracker.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.andrew.moneytracker.R;
import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.Spending;
import com.andrew.moneytracker.utils.dbHelper;
import com.andrew.moneytracker.utils.helper;

import java.text.Collator;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by andrew on 07.09.2016.
 */
public class BlotterFragment extends MainTabFragment {
	private static final String TAG = "m:BlotterFragment";

	private static final int REQUEST_NEW_SPENDING = 1;
	private static final int REQUEST_EDIT_SPENDING = 2;

	private static final String SAVED_SELECTED_POSITION = "selected_position";

	RecyclerView mBlotterRecyclerView;
	BlotterAdapter mBlotterAdapter;

	ImageButton btnEdit, btnDelete;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_blotter, container, false);

		if (savedInstanceState != null){
			selectedPosition = savedInstanceState.getInt(SAVED_SELECTED_POSITION);
		}

		v.findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doCreate();
			}
		});

		mBlotterAdapter = new BlotterAdapter();
		mBlotterRecyclerView = (RecyclerView) v.findViewById(R.id.blotter_list);
		mBlotterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		mBlotterRecyclerView.setAdapter(mBlotterAdapter);

		btnDelete = (ImageButton) v.findViewById(R.id.button_delete);
		btnEdit = (ImageButton) v.findViewById(R.id.button_edit);

		btnDelete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doDelete();
			}
		});

		btnEdit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				doEdit();
			}
		});

		updateData(false);

		return v;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(SAVED_SELECTED_POSITION, selectedPosition);
	}

	private void doCreate() {
		startActivityForResult(SpendingActivity.newIntent(getContext(), null), REQUEST_NEW_SPENDING);
	}

	private void doEdit() {
		Spending spending = mBlotterAdapter.getItem(selectedPosition);
		startActivityForResult(SpendingActivity.newIntent(getContext(), spending.getId()), REQUEST_EDIT_SPENDING);
	}

	private void doDelete() {
		new AlertDialog.Builder(getContext())
				  .setMessage(R.string.confirm_delete_spending)
				  .setTitle(R.string.confirm_dialog_title)
				  .setIcon(R.drawable.icon_alert)
				  .setNegativeButton(android.R.string.cancel, null)
				  .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
					  @Override
					  public void onClick(DialogInterface dialog, int which) {
						  Spending spending = mBlotterAdapter.getItem(selectedPosition);
						  dbHelper.deleteSpending(spendingDao(), spending.getId());
						  mBlotterAdapter.removeItem(selectedPosition);
						  selectedPosition = -1;
						  onSelectionChanged();
					  }
				  }).show();

	}

	Map<Long,Account> mAccounts;

	private void updateData(boolean unselect){
		Log.d(TAG, "update data");

		if (unselect){
			selectedPosition = -1;
		}
		onSelectionChanged();

		mAccounts = dbHelper.accountsMap(accountDao());
		mBlotterAdapter.setData(dbHelper.blotterList(spendingDao()));
	}

	@Override
	public void onTabSelected() {

	}

	int selectedPosition = -1;

	class BlotterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		Spending spending;
		TextView mSum, mAccount, mProduct, mTime, mNotes;

		public BlotterViewHolder(View itemView) {
			super(itemView);
			mSum = (TextView) itemView.findViewById(R.id.sum);
			mAccount = (TextView) itemView.findViewById(R.id.account);
			mProduct = (TextView) itemView.findViewById(R.id.product);
			mTime = (TextView) itemView.findViewById(R.id.time);
			mNotes = (TextView) itemView.findViewById(R.id.notes);
			itemView.setOnClickListener(this);
		}

		public void bindData(Spending spending, boolean isSelected){
			this.spending = spending;
			mSum.setText(helper.formatSumForBlotter(spending.getCash()));
			mAccount.setText(mAccounts.get(spending.getAccountId()).getName());
			mProduct.setText(spending.getProduct().getName());
			mTime.setText(helper.formatBlotterTime(spending.getDate()));
			String notes = spending.getNotes();
			mNotes.setText(notes);
			mNotes.setVisibility(notes == null || notes.length() == 0 ? View.GONE : View.VISIBLE);
			updateSelected(isSelected);
		}

		public void updateSelected(boolean isSelected) {
			itemView.setBackgroundResource(isSelected ? R.drawable.item_selected_bg : 0);
		}

		@Override
		public void onClick(View v) {
			int position = mBlotterAdapter.getItemPosition(spending.getId());
			if (position == selectedPosition){
				selectedPosition = -1;
			} else {
				int prevSelected = selectedPosition;
				selectedPosition = position;
				if (prevSelected != -1){
					mBlotterAdapter.notifyItemChanged(prevSelected);
				}
			}
			updateSelected(position == selectedPosition);
			onSelectionChanged();
		}
	}

	private void onSelectionChanged() {
		btnDelete.setVisibility(selectedPosition != -1 ? View.VISIBLE : View.GONE);
		btnEdit.setVisibility(selectedPosition != -1 ? View.VISIBLE : View.GONE);
	}

	class BlotterAdapter extends RecyclerView.Adapter<BlotterViewHolder>{

		List<Spending> blotter = new ArrayList<>();
		LayoutInflater layoutInflater;

		public BlotterAdapter() {
			layoutInflater = LayoutInflater.from(getContext());
		}

		public void setData(List<Spending> data){
			blotter = data;
			notifyDataSetChanged();
		}

		@Override
		public BlotterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			View view = layoutInflater.inflate(R.layout.blotter_list_item, parent, false);
			return new BlotterViewHolder(view);
		}

		@Override
		public void onBindViewHolder(BlotterViewHolder holder, int position) {
			holder.bindData(getItem(position), position == selectedPosition);
		}

		@Override
		public int getItemCount() {
			return blotter.size();
		}

		public Spending getItem(int position) {
			return blotter.get(position);
		}

		public void refreshItem(int position) {
			Spending newSpending = spendingDao().loadDeep(getItem(position).getId());
			blotter.remove(position);
			blotter.add(position, newSpending);
			notifyItemChanged(position);
		}

		public void removeItem(int position) {
			blotter.remove(position);
			notifyItemRemoved(position);
		}

		public int getItemPosition(Long id) {
			int count = getItemCount();
			for (int i = 0; i < count; i++) {
				if (blotter.get(i).getId().equals(id)){
					return i;
				}
			}
			return -1;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_NEW_SPENDING){
			updateData(true);
		} else if (requestCode == REQUEST_EDIT_SPENDING) {
			if (resultCode == Activity.RESULT_OK){
				mBlotterAdapter.refreshItem(selectedPosition);
			}
		}
	}
}
