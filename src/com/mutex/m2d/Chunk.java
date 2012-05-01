package com.mutex.m2d;

import com.mutex.m2d.Block;;

public class Chunk
{
	public byte blocks[];
	public final int x;
	public final int y;
	public World world;
	public static final int width = 64;
	public static final int height = 64;	
	
	public Chunk (World world_, int x_, int y_)
	{
		world = world_;
		x = x_; y = y_;
		//blocks = new Block[width*height];
	}
	
	public Chunk (World world_, byte blocks_[], int x_, int y_)
	{
		this(world_, x_, y_);
		blocks = blocks_;
	}
	
	public int getBlockID(int x_, int y_)
	{
		return blocks[x_ + y_ * width];
	}
}