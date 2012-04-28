package com.mutex.m2d;

public class BoundingBox
{
	public double x1;
	public double y1;
	public double x2;
	public double y2;
	public double posX;
	public double posY;
	
	public BoundingBox(double x1_, double y1_, double x2_, double y2_)
	{
		x1 = x1_; y1 = y1_; x2 = x2_; y2 = y2_;
		posX = (x1 + x2) / 2;
		posY = (y1 + y2) / 2;
	}
	
	public BoundingBox(double posX_, double posY_, float width_, float height_)
	{
		posX = posX_; posY = posY_;
		x1 = posX - width_/2;
		x2 = posX + width_/2;
		y1 = posY - height_/2;
		y2 = posY + height_/2;
	}
	
	public void setWidth(double width_)
	{
		x1 = posX - width_/2;
		x2 = posX + width_/2;		
	}
	
	public void setHeight(double height_)
	{
		y1 = posY - height_/2;
		y2 = posY + height_/2;		
	}
	
	public boolean isIntersectWith(BoundingBox bb)
	{
		if (y2 < bb.y1 || y1 > bb.y2 || x1 > bb.x2 || x2 < bb.x1) return false;
		return true;
	}
}