package com.mutex.m2d;

import java.util.Iterator;

public class Entity
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
		posX += speedX * 0.01;
		posY += speedY * 0.01;
		speedX = 0;
		speedY = 0;
		updateBB();
		moveEntity(speedX, speedY);
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
			posX = prevPosX;
			posY = prevPosY;
			//posX = posX - prevSpeedX;
			//posY = posY - prevSpeedY;
			
		} else {
			speedX = speedX_;
			speedY = speedY_;
		}
	}
	
	public void collidedWithBlock(Block b)
	{
		isCollided = true;
	}
}