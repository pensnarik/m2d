package com.mutex.m2d;

import com.mutex.m2d.Blocks.*;

public class Block {
	public final int blockID;
	public int x;
	public int y;
	public float width;
	public float height;
	private boolean highlighted;
	public static Block[] blocksList;
	
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
		//System.out.println("New block: " + x + ", " + y);
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
}