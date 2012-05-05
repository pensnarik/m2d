package com.mutex.m2d;

public class ItemContainer
{
	public int itemsCount;
	public int itemID;
	public final int containerSize = 10;
	
	public ItemContainer(int itemID_, int itemsCount_)
	{
		itemID = itemID_;
		itemsCount = itemsCount_;
	}
}