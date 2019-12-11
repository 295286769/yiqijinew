package com.yiqiji.money.modules.common.entity;

public abstract class Product<T> {

	public abstract T getCostTypeByName(String name);

	public abstract int getIdByName(String name);

	public abstract int getNoSelectImageResourceIncomel(int id);

	public abstract int getSelectImageResource(int id);

	public abstract String getNameById(int id);
}
