package com.andrew.moneytracker.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andrew.moneytracker.R;
import com.andrew.moneytracker.commons.IFactory;
import com.andrew.moneytracker.commons.ISimpleViewHolder;
import com.andrew.moneytracker.commons.SimpleListAdapter;
import com.andrew.moneytracker.commons.SimpleListView;
import com.andrew.moneytracker.database.Account;
import com.andrew.moneytracker.database.Product;
import com.andrew.moneytracker.utils.dbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 07.09.2016.
 */
public class ProductsFragment extends MainTabFragment {

	SimpleListView mListProducts;


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_products, container, false);

		mListProducts = (SimpleListView) v.findViewById(R.id.products_list);

		updateData();

		return v;
	}

	void updateData(){
		mListProducts.setAdapter(new ProductsAdapter(dbHelper.productsList(productDao())), R.layout.account_list_item);
	}

	@Override
	public void onTabSelected() {
		updateData();
	}

	public class ProductViewHolder implements ISimpleViewHolder<Product> {
		TextView mName;
		Product mProduct;

		@Override
		public void bindView(View view) {
			mName = (TextView) view.findViewById(R.id.name);
		}

		@Override
		public void bindData(Product product) {
			mProduct = product;
			mName.setText(product.getName());
		}
	}

	class ProductsAdapter extends SimpleListAdapter<Product> {

		public ProductsAdapter(List<Product> products) {
			super(products, new IFactory<ISimpleViewHolder>() {
				@Override
				public ISimpleViewHolder create() {
					return new ProductViewHolder();
				}
			});
		}
	}
}
