package com.mutex.m2d;

public class EntityPlayerSP extends EntityPlayer
{	
	public MovementInput movementInput;
	
	public EntityPlayerSP(World world_)
	{
		super(world_);
		maxSpeed = 0.2f;
		height = 0.9f;
		isJumping = false;
		updateBB();
	}
	
	public void moveEntity(double x, double y)
	{
		super.moveEntity(x, y);
	}
	
	public void onUpdate()
	{
		updateEntityActionState();
		super.onUpdate();
	}
	
	public void onLivingUpdate()
	{
		movementInput.update();
		super.onLivingUpdate();
	}
	
	public void updateEntityActionState()
	{
		super.updateEntityActionState();
		moveX = movementInput.moveX;
		if (moveX != 0)
		{
			@SuppressWarnings("unused")
			int a = 0;
		}
		moveY = movementInput.moveY;		
		isJumping = movementInput.jump;
	}
}