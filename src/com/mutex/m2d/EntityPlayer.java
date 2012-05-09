package com.mutex.m2d;

import java.util.List;

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
		/* Check for collisions */
		List list = world.getEntitiesWithinBoundingBoxEcxluding(this, boundingBox.expand(1, 1));
		if (list != null)
		{
			for (int i = 0; i < list.size(); i++)
			{
				Entity entity = (Entity) list.get(i);
				if (!entity.isDead)
				{
					collideWithPlayer(entity);
				}
			}
		}
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
	
	private void collideWithPlayer(Entity entity)
	{
		entity.onCollideWithPlayer(this);
	}
}