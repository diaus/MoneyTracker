package com.andrew.moneytracker.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by andrew on 07.09.2016.
 */
public class helper {

	public static void putLongExtra(Intent intent, String key, Long value) {
		if (value != null) intent.putExtra(key, value);
	}

	@Nullable
	public static Long getLongExtra(Intent intent, String key) {
		return intent.hasExtra(key) ? intent.getLongExtra(key, -1) : null;
	}

	public static void putLongBundle(Bundle bundle, String key, Long value) {
		if (value != null) bundle.putLong(key, value);
	}

	@Nullable
	public static Long getLongBundle(Bundle bundle, String key) {
		return bundle.containsKey(key) ? bundle.getLong(key) : null;
	}

//	public static void showKeyboard(Context context) {
//		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.toggleSoftInputFromWindow(linearLayout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
//		imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//	}

	public static void focusAndShowKeyboard(Context context, View view) {
		if(view.requestFocus()){
			InputMethodManager imm =(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
		}
	}

	public static String trimEdit(EditText edit) {
		String s = edit.getText().toString().trim();
		edit.setText(s);
		return s;
	}
}
