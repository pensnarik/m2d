package com.mutex.m2d;

public class EntityPlayer extends EntityLiving
{
	public InventoryPlayer inventory;
	
	public EntityPlayer(World world_)
	{
		super(world_);
		inventory = new InventoryPlayer();
	}
	
	public void onUpdate()
	{
		super.onUpdate();
	}
	
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
	}
	
	public void updateEntityActionState()
	{
		/* Swing progress */
	}
	
	public void moveEntityWithHeading(float x, float y)
	{
		super.moveEntityWithHeading(x, y);
	}
	
	protected void fall(float distance)
	{
		super.fall(distance);
	}
}