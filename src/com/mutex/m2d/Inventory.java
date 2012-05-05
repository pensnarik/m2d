package com.mutex.m2d;

public abstract class Inventory
{
	public ItemContainer[] itemContainers;
	protected int numContainers;
	
	public Inventory(int numContainers_)
	{
		numContainers = numContainers_;
		itemContainers = new ItemContainer[numContainers_];
	}
	
	public int getNumContainers()
	{
		return numContainers;
	}
	
	public boolean addContainerToInventory(ItemContainer itemContainer)
	{
		/* TODO: Add check for maximum container size */
		for (int i = 0; i < numContainers; i++)
		{
			if (itemContainers[i].itemID == itemContainer.itemID)
			{
				ItemContainer ic = itemContainers[i];
				ic.itemsCount += itemContainer.itemsCount;
			}
		}
		return true;
	}
}