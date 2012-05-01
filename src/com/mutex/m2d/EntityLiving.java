package com.mutex.m2d;

public class EntityLiving extends Entity
{

	public float maxSpeed;
	protected boolean isJumping;
	
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
	
	public void onLivingUpdate()
	{
		updateEntityActionState();
		if (isJumping)
		{
			jump();
		}
	}
	
	public void updateEntityActionState()
	{
		
	}
	
	protected void jump()
	{
		speedY += 1.3;
	}
	
}