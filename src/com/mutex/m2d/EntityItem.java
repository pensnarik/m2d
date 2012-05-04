package com.mutex.m2d;

public class EntityItem extends Entity
{
	public EntityItem(World world, double x, double y)
	{
		super(world);
		posX = x;
		posY = y;
		setSize(0.5f, 0.5f);
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		motionY -= 0.04;
		moveEntity(motionX, motionY);
		motionY *= 0.98;
	}
}