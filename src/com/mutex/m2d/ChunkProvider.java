package com.mutex.m2d;

import java.io.*;

public class ChunkProvider
{
	public World world;
	public HashMap loadedChunks;

	public ChunkProvider(World world_)
	{
		world = world_;
		loadedChunks = new HashMap();
	}
	
	public Chunk provideChunk(int x_, int y_)
	{
		System.out.println("provideChunk(" + x_ + ", " + y_ + ")");
		int hash = chunkCoordsToHash(x_, y_);		
		Chunk chunk = (Chunk) loadedChunks.getValueByKey(hash);
		int x; int y;
		
		if(chunk == null) {		
			chunk = new Chunk(world, x_, y_);
			for (int i = 0; i < Chunk.width * Chunk.height; i++)
			{
				int type = world.RandomGen.nextInt(3);
				x = x_ * Chunk.width + i % Chunk.width;
				y = y_ * Chunk.height + (i - (i % Chunk.width))/Chunk.width;
				if (y == 0) type = 1;
				if (type == 2 | y > 0) {
					chunk.blocks[i] = null;
				} else {
					chunk.blocks[i] = new Block(type, x, y);
				}
			}
			loadedChunks.add(hash,  chunk);
		}
		
		return chunk;
	}
	
	public int chunkCoordsToHash(int x_, int y_)
	{
		return x_ & 0xffff | (y_ & 0xffff) << 16;
	}
	
}