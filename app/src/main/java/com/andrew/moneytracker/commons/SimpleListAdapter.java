package com.andrew.moneytracker.commons;

import android.view.View;

import com.andrew.moneytracker.database.Account;

import java.util.List;

/**
 * Created by andrew on 07.09.2016.
 */
public class SimpleListAdapter<ObjectClass> implements SimpleListView.IAdapter<ISimpleViewHolder> {

	List<ObjectClass> items;
	IFactory<ISimpleViewHolder> factory;

	public SimpleListAdapter(List<ObjectClass> items, IFactory<ISimpleViewHolder> factory) {
		this.factory = factory;
		this.items = items;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void bindViewHolder(ISimpleViewHolder holder, int position) {
		holder.bindData(items.get(position));
	}

	@Override
	public ISimpleViewHolder createViewHolder(View view) {
		ISimpleViewHolder holder = factory.create();
		holder.bindView(view);
		return holder;
	}
}
