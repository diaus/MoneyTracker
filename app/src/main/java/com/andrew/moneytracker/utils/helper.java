package com.andrew.moneytracker.utils;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

}
