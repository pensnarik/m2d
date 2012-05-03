package com.mutex.m2d;

import com.mutex.m2d.Blocks.*;
import java.util.*;

public class Block {
	public final int blockID;
	public int x;
	public int y;
	public float width;
	public float height;
	private boolean highlighted;
	public static Block[] blocksList;
	
	/* Относительные значения максимальных и минимальных координат */
	public double minX;
	public double minY;
	public double maxX;
	public double maxY;
	
	public static final BlockDirt blockDirt;
	public static final BlockStone blockStone;
	
	static
	{
		blocksList = new Block[256];
		blockDirt = new BlockDirt(1);
		blockStone = new BlockStone(2);
	}
	
	public Block(int blockID_)
	{
		//x = x_; y = y_;
		blockID = blockID_;
		width = height = 1;
		highlighted = false;
		blocksList[blockID_] = this;
		setBlockBounds(0, 0, 1, 1);
	}
	
	public void tick()
	{
		
	}
	
	public boolean isHightlighted()
	{
		return highlighted;
	}
	
	public void setHighlighted(boolean flag)
	{
		highlighted = flag;
	}
	
	public void setBlockBounds(double minX_, double minY_, double maxX_, double maxY_)
	{
		minX = minX_; minY = minY_;
		maxX = maxX_; maxY = maxY_;
	}
	
	public void getCollidingBoundingBoxes(World world, int x, int y, BoundingBox boundingBox, ArrayList list)
	{
		BoundingBox bb = getCollisionBoundingBoxFromPool(world, x, y);
		if(bb != null && boundingBox.intersectsWith(bb))
		{
			list.add(bb);
		}
	}
	
	public BoundingBox getCollisionBoundingBoxFromPool(World world, int x, int y)
	{
		return BoundingBox.getBoundingBoxFromPool(minX + x, minY + y, maxX + x, maxY + y);
	}
}