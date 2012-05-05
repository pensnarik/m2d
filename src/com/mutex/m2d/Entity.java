package com.mutex.m2d;

import java.util.*;

public abstract class Entity
{
	/*
	 * TODO: pushOutOfBlocks()
	 */
	private static int nextEntityID = 0;
	public int entityID;
	public World world;
	
	public double posX;
	public double posY;
	public int chunkX;
	public int chunkY;
	public double prevPosX;
	public double prevPosY;
	public double motionX;
	public double motionY;
	protected float width;
	protected float height;
	
	public boolean isCollided;
	public boolean isCollidedHorizontally;
	public boolean isCollidedVertically;
	
	public float fallDistance;
	
	public final BoundingBox boundingBox = BoundingBox.getBoundingBox(0, 0, 0, 0);
	
	public boolean onGround;
	public int entityAge;
	public boolean isDead;
	
	
	public Entity(World world_)
	{
		width = 0.9f;
		height = 1;
		//bb = new BoundingBox(0, 0, width, height);
		entityID = nextEntityID++;
		world = world_;
		isCollided = false;
		isCollidedHorizontally = false;
		isCollidedVertically = false;
		onGround = false;
		fallDistance = 0;
		entityAge = 0;
		isDead = false;
	}
	
	protected void setWidth(float width_)
	{
		width = width_;
	}
	
	protected void setHeight(float height_)
	{
		height = height_;
	}
	
	public void setSize(float width_, float height_)
	{
		width = width_; height = height_;
		updateBB();
	}
	
	protected final void updateBB()
	{
		boundingBox.setBounds(posX - width/2, posY - height / 2, posX + width/2, posY + height/2);
	}
	
	public void setPosition(double x, double y)
	{
		posX = x;
		posY = y;
		/* update bounding boxes */
		updateBB();
	}
	
	public void onUpdate()
	{
		entityAge++;
		onEntityUpdate();			
	}
	
	public void onEntityUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
	}
	
	public void moveEntity(double X_, double Y_)
	{
		double initialX_ = X_;
		double initialY_ = Y_;
		//BoundingBox bb = boundingBox.copy();
		List listBB = world.getCollidingBoundingBoxes(this, boundingBox.addCoord(X_, Y_));
		/* debug */
		//Game.r.renderListAABB(listBB);
		/* end debug */
		for (int i = 0; i < listBB.size(); i++)
		{
			Y_ = ((BoundingBox) listBB.get(i)).calculateYOffset(boundingBox, Y_);
		}
		boundingBox.offset(0, Y_);
		boolean onGroundNow = onGround || (initialY_ != Y_ && Y_ < 0);
		for (int j = 0; j < listBB.size(); j++)
		{
			X_ = ((BoundingBox) listBB.get(j)).calculateXOffset(boundingBox, X_);
		}
		boundingBox.offset(X_, 0);
		
		posX = (boundingBox.minX + boundingBox.maxX) / 2;
		posY = (boundingBox.minY + boundingBox.maxY) / 2;
		isCollidedHorizontally = initialX_ != X_;
		isCollidedVertically = initialY_ != Y_;
		isCollided = isCollidedHorizontally || isCollidedVertically;
		onGround = initialY_ != Y_ && initialY_ < 0;
		updateFallState(Y_, onGround);
		if (initialX_ != X_)
		{
			motionX = 0;
		}
		if (initialY_ != Y_)
		{
			motionY = 0;
		}
	}
	
	protected void fall(float distance)
	{
		
	}
	
	public void updateFallState(double y, boolean onGround_)
	{
		if (onGround_)
		{
			if (fallDistance > 0)
			{
				fall(fallDistance);
				fallDistance = 0;
			}
		}
		else if (y < 0)
		{
			fallDistance -= y;
		}
	}
	
	public void collidedWithBlock(Block b)
	{
		isCollided = true;
	}
	
	public void onCollideWithPlayer(EntityPlayer entityPlayer)
	{
		
	}
	
	public void setEntityDead()
	{
		isDead = true;
	}
}