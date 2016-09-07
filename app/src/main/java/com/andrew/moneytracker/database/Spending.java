package com.andrew.moneytracker.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by andrew on 07.09.2016.
 */
@Entity
public class Spending {
	@Id(autoincrement = true)
	private Long id;

	@NotNull
	private long productId;

	@NotNull
	private long accountId;

	@NotNull
	private int cash;

	@Generated(hash = 1351924817)
	public Spending(Long id, long productId, long accountId, int cash) {
		this.id = id;
		this.productId = productId;
		this.accountId = accountId;
		this.cash = cash;
	}

	@Generated(hash = 2056300050)
	public Spending() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getProductId() {
		return this.productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getAccountId() {
		return this.accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public int getCash() {
		return this.cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}
}
