package com.mutex.m2d;

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
		//System.out.println("provideChunk(" + x_ + ", " + y_ + ")");
		int hash = chunkCoordsToHash(x_, y_);		
		Chunk chunk = (Chunk) loadedChunks.getValueByKey(hash);
		int x; int y;
		
		if(chunk == null) {		
			byte blocks[] = new byte[Chunk.width * Chunk.height];
			generateTerrain(x_, y_, blocks);
			chunk = new Chunk(world, blocks, x_, y_);
			loadedChunks.add(hash,  chunk);
		}
		
		return chunk;
	}
	
	public void generateTerrain(int x_, int y_, byte[] blocks_)
	{
		for(int x = 0; x < Chunk.width; x++)
		{
			for(int y = 0; y < Chunk.height; y++)
			{
				blocks_[x + y*Chunk.height] = (byte) 1;
			}
		}
	}
	
	public int chunkCoordsToHash(int x_, int y_)
	{
		return x_ & 0xffff | (y_ & 0xffff) << 16;
	}
	
}