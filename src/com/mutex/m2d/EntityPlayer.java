package com.mutex.m2d;

public class EntityPlayer extends EntityLiving
{	
	public MovementInput movementInput;
	
	public EntityPlayer(World world_)
	{
		super(world_);
		maxSpeed = 0.2f;
		height = 0.9f;
		isJumping = false;
		updateBB();
	}
	
	public void onUpdate()
	{
		movementInput.update();
		updateEntityActionState();
		super.onUpdate();
	}
	
	public void updateEntityActionState()
	{
		speedX = movementInput.moveX;
		speedY = movementInput.moveY;		
		isJumping = movementInput.jump;
	}
}