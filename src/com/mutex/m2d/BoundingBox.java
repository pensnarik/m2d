package com.mutex.m2d;

import java.util.*;

public class BoundingBox
{
	public double x1;
	public double y1;
	public double x2;
	public double y2;
	public double posX;
	public double posY;
	
	private static List<BoundingBox> boundingBoxes = new ArrayList();
	private static int numBoundingBoxesInUse = 0;
	
	public double minX;
	public double minY;
	public double maxX;
	public double maxY;
	
	public static BoundingBox getBoundingBox(double minX_, double minY_, double maxX_, double maxY_)
	{
		return new BoundingBox(minX_, minY_, maxX_, maxY_);
	}
	
	/* Используется только глобально - при освобождении памяти */
	public static void clearBoundingBoxes()
	{
		boundingBoxes.clear();
		numBoundingBoxesInUse = 0;
	}
	
	/* Вызывается при инициализации приложения */
	public static void clearBoundingBoxPool()
	{
		numBoundingBoxesInUse = 0;
	}
	
	public static BoundingBox getBoundingBoxFromPool(double minX_, double minY_, double maxX_, double maxY_)
	{
		if (numBoundingBoxesInUse >= boundingBoxes.size())
		{
			boundingBoxes.add(getBoundingBox(0, 0, 0, 0));
		}
		return ((BoundingBox) boundingBoxes.get(numBoundingBoxesInUse++)).setBounds(minX_, minY_, maxX_, maxY_);
	}
		
	private BoundingBox(double minX_, double minY_, double maxX_, double maxY_)
	{
		minX = minX_; minY = minY_; maxX = maxX_; maxY = maxY_;
	}
	
	public BoundingBox setBounds(double minX_, double minY_, double maxX_, double maxY_)
	{
		minX = minX_; minY = minY_; maxX = maxX_; maxY = maxY_;
		return this;
	}
	
	public BoundingBox addCoord(double deltaX, double deltaY)
	{
		double minX_ = minX;
		double minY_ = minY;
		double maxX_ = maxX;
		double maxY_ = maxY;
		
		if (deltaX < 0)
		{
			minX_ += deltaX;
		}
		if (deltaX > 0)
		{
			maxX_ += deltaX;
		}
		if (deltaY < 0)
		{
			minY_ += deltaY;
		}
		if (deltaY > 0)
		{
			maxY_ += deltaY;
		}
		return getBoundingBoxFromPool(minX_, minY_, maxX_, maxY_);
	}
	
	public BoundingBox expand(double deltaX, double deltaY)
	{
		return getBoundingBoxFromPool(minX - deltaX, minY - deltaY, maxX + deltaX, maxY + deltaY);
	}
	
	public BoundingBox getOffsetBoundingBox(double deltaX, double deltaY)
	{
		return getBoundingBoxFromPool(minX + deltaX, minY + deltaY, maxX + deltaX, maxY + deltaY);
	}
	
	public double calculateXOffset(BoundingBox bb, double offset)
	{
		return 0;
	}
	
	public double calculateYOffset(BoundingBox bb, double offset)
	{
		return 0;
	}
	
	public boolean isIntersectWith(BoundingBox bb)
	{
		if (y2 < bb.y1 || y1 > bb.y2 || x1 > bb.x2 || x2 < bb.x1) return false;
		return true;
	}
}