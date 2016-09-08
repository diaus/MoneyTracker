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

/**
 * Created by andrew on 07.09.2016.
 */
public class ProductsFragment extends MainTabFragment {

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_products, container, false);

		SimpleListView listProducts = (SimpleListView) v.findViewById(R.id.products_list);

		listProducts.setAdapter(new SimpleListAdapter<>(dbHelper.productsList(productDao()), new IFactory<ISimpleViewHolder>() {
					  @Override
					  public ISimpleViewHolder create() {
						  return new ProductViewHolder();
					  }
				  }),
				  R.layout.account_list_item);

		return v;
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

}
