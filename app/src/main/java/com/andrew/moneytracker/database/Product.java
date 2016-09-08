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
public class Product {
	@Id(autoincrement = true)
	private Long id;

	@NotNull
	private String name;

	@NotNull
	@Index
	private String nameNormalized;

	private Long parentId;

	@Generated(hash = 524282247)
	public Product(Long id, @NotNull String name, @NotNull String nameNormalized,
			Long parentId) {
		this.id = id;
		this.name = name;
		this.nameNormalized = nameNormalized;
		this.parentId = parentId;
	}

	@Generated(hash = 1890278724)
	public Product() {
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

	public Long getParentId() {
		return this.parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getNameNormalized() {
		return this.nameNormalized;
	}

	public void setNameNormalized(String nameNormalized) {
		this.nameNormalized = nameNormalized;
	}
}
