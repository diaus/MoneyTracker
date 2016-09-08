package com.andrew.moneytracker.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.DaoException;

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

	@NotNull
	private Date date;

	private String notes;

	@ToOne(joinProperty = "productId")
	private Product product;

	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/** Used for active entity operations. */
	@Generated(hash = 593183288)
	private transient SpendingDao myDao;

	@Generated(hash = 587652864)
	private transient Long product__resolvedKey;

	@Generated(hash = 297196532)
	public Spending(Long id, long productId, long accountId, int cash,
			@NotNull Date date, String notes) {
		this.id = id;
		this.productId = productId;
		this.accountId = accountId;
		this.cash = cash;
		this.date = date;
		this.notes = notes;
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

	public String getNotes() {
		return this.notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getCashBig() {
		return getCash() / 100;
	}

	public int getCashSmall() {
		return getCash() % 100;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/** To-one relationship, resolved on first access. */
	@Generated(hash = 926515087)
	public Product getProduct() {
		long __key = this.productId;
		if (product__resolvedKey == null || !product__resolvedKey.equals(__key)) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			ProductDao targetDao = daoSession.getProductDao();
			Product productNew = targetDao.load(__key);
			synchronized (this) {
				product = productNew;
				product__resolvedKey = __key;
			}
		}
		return product;
	}

	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 1202438341)
	public void setProduct(@NotNull Product product) {
		if (product == null) {
			throw new DaoException(
					"To-one property 'productId' has not-null constraint; cannot set to-one to null");
		}
		synchronized (this) {
			this.product = product;
			productId = product.getId();
			product__resolvedKey = productId;
		}
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 128553479)
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 1942392019)
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 713229351)
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 471661326)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getSpendingDao() : null;
	}
}
