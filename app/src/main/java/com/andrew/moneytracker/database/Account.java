package com.andrew.moneytracker.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by andrew on 07.09.2016.
 */
@Entity
public class Account {
	@Id(autoincrement = true)
	private Long id;

	@Index
	@NotNull
	private String name;

	@Generated(hash = 951981252)
	public Account(Long id, @NotNull String name) {
		this.id = id;
		this.name = name;
	}

	@Generated(hash = 882125521)
	public Account() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
