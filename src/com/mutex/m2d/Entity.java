package com.mutex.m2d;

public class Entity
{
	private static int nextEntityID = 0;
	public int entityID;
	public World world;
	
	public double posX;
	public double posY;
	public double speedX;
	public double speedY;
	
	public int stopTimer; /* change to private */
	
	public boolean isCollided;
	public BoundingBox boundingBox;
	
	
	public Entity(World world_)
	{
		entityID = nextEntityID++;
		world = world_;
		isCollided = false;
	}
	
	public void setPosition(double x, double y)
	{
		posX = x;
		posY = y;
		/* update bounding boxes */
	}
	
	public void onUpdate()
	{
		onEntityUpdate();			
	}
	
	public void onEntityUpdate()
	{
		posX += speedX;
		posY += speedY;
		speedX *= 0.99;
		speedY *= 0.99;
		if (stopTimer-- == 0)
		{
			speedX = 0;
			speedY = 0;
		}
	}
	
	public void moveEntity(double speedX_, double speedY_)
	{
		speedX = speedX_;
		speedY = speedY_;
		stopTimer = 100;
	}
	
	public void collidedWithBlock(Block b)
	{
		isCollided = true;
		if (b.x < posX) {
			speedX = 0;
			posX = b.x + 1 + 0.01;
		}
	}
}