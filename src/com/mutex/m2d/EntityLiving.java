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
	
	protected void jump()
	{
		if(isJumping) return;
		speedY = 0.3;
	}
	
}