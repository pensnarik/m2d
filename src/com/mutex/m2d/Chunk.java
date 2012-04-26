package com.mutex.m2d;

import com.mutex.m2d.Block;;

public class Chunk {

	public Block blocks[];
	public final int x;
	public final int y;
	public World world;
	public static final int width = 32;
	public static final int height = 32;	
	
	public Chunk (World world_, int x_, int y_)
	{
		world = world_;
		x = x_; y = y_;
		blocks = new Block[width*height];
	}
	
	
}