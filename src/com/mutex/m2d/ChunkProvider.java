package com.mutex.m2d;

import java.io.*;

//import com.mutex.m2d.Chunk;

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
		
		if(chunk == null) {		
			chunk = new Chunk(world, x_, y_);
			for (int i = 0; i < Chunk.width * Chunk.height; i++)
			{
				chunk.blocks[i] = new Block(0, i % Chunk.width, (i - (i % Chunk.width))/Chunk.width); 
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