package com.mutex.m2d;

import com.mutex.m2d.Block;;

public class Chunk {

	//public Block blocks[];
	public byte blocks[];
	public final int x;
	public final int y;
	public World world;
	public static final int width = 16;
	public static final int height = 16;	
	
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