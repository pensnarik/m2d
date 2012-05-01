package com.mutex.m2d;

public class Block {
	public final int blockID;
	protected BoundingBox bb;
	public int x;
	public int y;
	public float width;
	public float height;
	private boolean highlighted;
	
	Block(int blockID_, int x_, int y_)
	{
		x = x_; y = y_;
		blockID = blockID_;
		width = height = 1;
		bb = new BoundingBox((double) x, (double) y, width, height);
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