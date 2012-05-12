package com.mutex.m2d;

public class ItemContainer
{
	public int itemsCount;
	public int itemID;
	/* Вид item'a - например, ID блока для ItemBlock */
	public int subItemID;
	public final int containerSize = 10;
	
	public ItemContainer(int itemID_, int subItemID_, int itemsCount_)
	{
		itemID = itemID_;
		itemsCount = itemsCount_;
		subItemID = subItemID_;
	}
}