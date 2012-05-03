package com.mutex.m2d;

import java.util.*;

public class BoundingBox
{
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
	
	/*
	 * Метод возвращает расстояние вдоль оси X, на котором пересекаются b-box-ы,
	 * если это расстояние больше, чем offset, возвращается offset, если b-box-ы не
	 * пересекаются, возвращается offset
	 */
	public double calculateXOffset(BoundingBox bb, double offset)
	{
		if (bb.maxY <= minY || bb.minY >= maxY)
		{
			return offset;
		}
		if (offset > 0 && bb.maxX <= minX)
		{
			double delta1 = minX - bb.maxX;
			if(delta1 < offset)
			{
				offset = delta1;
			}
		}
		if (offset < 0 && bb.minX >= maxX)
		{
			double delta2 = maxX - bb.minX;
			if (delta2 > offset)
			{
				offset = delta2;
			}
		}
		return offset;
	}
	
	public double calculateYOffset(BoundingBox bb, double offset)
	{
		if (bb.maxX <= minX || bb.minX >= maxX)
		{
			return offset;
		}
		if (offset > 0 && bb.maxY <= minY)
		{
			double delta1 = minY - bb.maxY;
			if (delta1 < offset)
			{
				offset = delta1;
			}
		}
		if (offset < 0 && bb.minY >= maxY)
		{
			double delta2 = maxY - bb.minY;
			if(delta2 > offset)
			{
				offset = delta2;
			}
		}
		return offset;
	}
	
	public boolean intersectsWith(BoundingBox bb)
	{
		if (bb.maxX <= minX || bb.minX >= maxX)
		{
			return false;
		}
		if (bb.maxY <= minY || bb.minY >= maxY)
		{
			return false;
		}
		return true;
	}
	
	/*
	 * Смещает b-box на величину (x_, y_)
	 */
	public BoundingBox offset(double x_, double y_)
	{
		minX += x_;
		minY += y_;
		maxX += x_;
		maxY += y_;
		return this;
	}
	
	public BoundingBox copy()
	{
		return getBoundingBoxFromPool(minX, minY, maxX, maxY);
	}
}