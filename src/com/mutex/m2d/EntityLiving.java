package com.mutex.m2d;

public class EntityLiving extends Entity
{

	public float maxSpeed;
	protected boolean isJumping;
	protected float moveX;
	protected float moveY;
	protected double newPosX;
	protected double newPosY;
	private int jumpTimer;
	
	public EntityLiving(World world_) {
		super(world_);
		maxSpeed = 0.1f;
		isJumping = false;	
		jumpTimer = 0;
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
		//motionX = moveX_ * 0.01; // debug
		//motionY = moveY_ * 0.01; // debug
		moveEntity(motionX, motionY);
		motionY -= 0.08;
		motionY *= 0.98;
		//motionX *= 0.80 * moveX_;
		motionX = moveX_ * 0.2;		
	}
	
	public void onLivingUpdate()
	{
		if (jumpTimer > 0)
		{
			jumpTimer--;
		}
		updateEntityActionState();
		
		if(isJumping)
		{
			if(onGround && jumpTimer == 0)
			{
				jump();
				jumpTimer = 100;
			}
		}
		else
		{
			jumpTimer = 0;
		}
		
		moveX *= 0.98;
		moveY *= 0.98;
		moveEntityWithHeading(moveX, moveY);
	}
	
	protected void updateEntityActionState()
	{
		moveX = 0;
		moveY = 0;
	}
	
	protected void jump()
	{
		motionY = 0.5;
	}
	
	protected void fall(float distance)
	{
		super.fall(distance);
	}
	
}