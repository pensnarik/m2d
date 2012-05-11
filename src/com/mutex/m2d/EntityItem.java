package com.mutex.m2d;

public class EntityItem extends Entity
{
	public ItemContainer item;
	
	public EntityItem(World world, double x, double y, ItemContainer item_)
	{
		super(world);
		posX = x;
		posY = y;
		item = item_;
		setSize(0.5f, 0.5f);
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		motionY -= 0.04;
		pushOutOfBlocks((boundingBox.minX + boundingBox.maxX)/2, (boundingBox.minY + boundingBox.maxY)/2);
		moveEntity(motionX, motionY);
		motionY *= 0.98;
		motionX *= 0.98;
	}
	
	public void onCollideWithPlayer(EntityPlayer entityPlayer)
	{
		entityPlayer.inventory.addContainerToInventory(item);
		if (true || item.itemsCount == 0)
		{
			setEntityDead();
		}
	}
}