package com.mutex.m2d;

import java.util.Random;

public class World {
	
	public ChunkProvider chunkProvider;
	public Player player;
	public Random RandomGen;
	
	World()
	{
		RandomGen = new Random(1);
		chunkProvider = new ChunkProvider(this);
		int hash = chunkProvider.chunkCoordsToHash(2, 2);
		System.out.println("hash = " + hash);
		player = new Player(0.0, 0.0);
		prepareVisibleChunks();
	}
	
	private void prepareVisibleChunks()
	{
		int Cx = (int)(player.x / Chunk.width);
		int Cy = (int)(player.y / Chunk.height);
		for (int x = Cx - 1; x <= Cx + 1; x++) {
			for (int y = Cy - 1; y <= Cy + 1; y++) {
				Chunk chunk = chunkProvider.provideChunk(x,  y);
			}
		}
		System.out.println("Chunks loaded: " + chunkProvider.loadedChunks.getSize());
	}
		
}