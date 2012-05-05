package com.mutex.m2d;

import java.util.*;
import com.mutex.m2d.Items.*;

public class Item
{
	public int itemID;
	public static Item[] itemsList;
	public final static ItemBlock itemBlock;
	
	static
	{
		itemsList = new Item[255];
		itemBlock = new ItemBlock(1);
	}
	
	public Item(int itemID_)
	{
		itemID = itemID_;
	}
}