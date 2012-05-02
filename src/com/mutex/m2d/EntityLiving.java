package com.mutex.m2d;

public class EntityLiving extends Entity
{

	public float maxSpeed;
	protected boolean isJumping;
	protected float moveX;
	protected float moveY;
	
	public EntityLiving(World world_) {
		super(world_);
		maxSpeed = 0.1f;
		isJumping = false;		
	}
	
	public boolean getIsJumping()
	{
		return isJumping;
	}
	
	public void onUpdate()
	{
		super.onUpdate();
		onLivingUpdate();
	}
	
	public void moveEntityWithHeading(float moveX_, float moveY_)
	{
		if (motionX > 0)
		{
			int a = 0;
		}
		moveEntity(motionX, motionY);
		motionY -= 0.08;
		motionY *= 0.98;
		motionX *= 0.98;
	}
	
	public void onLivingUpdate()
	{
		updateEntityActionState();
		moveX *= 0.98;
		moveY *= 0.98;
		moveEntityWithHeading(moveX, moveY);
	}
	
	public void updateEntityActionState()
	{
		
	}
	
	protected void jump()
	{
	
	}
	
}