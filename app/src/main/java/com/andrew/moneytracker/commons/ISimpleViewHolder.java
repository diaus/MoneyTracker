package com.andrew.moneytracker.commons;

import android.view.View;

/**
 * Created by andrew on 07.09.2016.
 */
public interface ISimpleViewHolder<T> {

	void bindView(View view);

	void bindData(T data);

}
