package com.mutex.m2d;

import java.util.Iterator;
import java.util.*;

public abstract class Entity
{
	private static int nextEntityID = 0;
	public int entityID;
	public World world;
	
	public double posX;
	public double posY;
	public double prevPosX;
	public double prevPosY;
	public double speedX;
	public double speedY;
	public double prevSpeedX;
	public double prevSpeedY;
	protected float width;
	protected float height;
	
	public boolean isCollided;
	public BoundingBox bb;
	
	public boolean onGround;
	
	
	public Entity(World world_)
	{
		width = height = 1;
		bb = new BoundingBox(0, 0, width, height);
		entityID = nextEntityID++;
		world = world_;
		isCollided = false;
	}
	
	protected void setWidth(float width_)
	{
		width = width_;
	}
	
	protected void setHeight(float height_)
	{
		height = height_;
	}
	
	protected final void updateBB()
	{
		bb.posX = posX;
		bb.posY = posY;
		bb.setWidth(width);
		bb.setHeight(height);
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
		onEntityUpdate();			
	}
	
	public void onEntityUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevSpeedX = speedX;
		prevSpeedY = speedY;
		//speedX = 0;
		//speedY = 0;
		moveEntity(speedX, speedY);
		updateBB();
	}
	
	/*
	 * Adujusting entity speed and coords, taking care for physics
	 */
	private void adjustSpeedAndCoords()
	{
		List<Block> nearestBlocks = new ArrayList();
		int x = MathHelper.round_float((float) posX);
		int y = MathHelper.round_float((float) posY);
		for(int i = x - 1; i < 3; i++)
		{
			for(int j = y - 1; j < 3; j++)
			{
				
			}
		}
	}
	
	public void moveEntity(double speedX_, double speedY_)
	{
		Iterator<Block> iter = world.blocks.iterator();
		Block collidedBlock = null;
		boolean flag = false;
		while(iter.hasNext())
		{
			Block b = iter.next();
			flag = (bb.isIntersectWith(b.bb));
			if (flag) {
				collidedBlock = b;
				break;
			}
		}
		isCollided = flag;
		if (collidedBlock != null) {
			collidedWithBlock(collidedBlock);
			//posX = prevPosX;
			//posY = prevPosY;
			if (speedX < 0)	speedX = 0;
			/* Use speedX and speedY to stop Entity */
			
		} else {
			speedX = speedX_;
			speedY = speedY_;
			if (world.getBlockUnderEntity(this) == 0)
			{
				//speedY = -1.0f;
			}
			else 
			{
				speedY = 0;
			}
		}
		posX += speedX * 0.01;
		posY += speedY * 0.01;
	}
	
	public void collidedWithBlock(Block b)
	{
		isCollided = true;
	}
}