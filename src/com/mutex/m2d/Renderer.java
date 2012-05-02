package com.mutex.m2d;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.opengl.Display;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.io.IOException;

public class Renderer {
	public Texture textureDirt;
	public Texture textureStone;
	public Texture texturePlayer;
	public Texture textureSky;
	public Texture textureGray;
	public World world;
	public final int scale = 2;
	public final int blockSize = 16;
	
	public Renderer(World world_)
	{
		world = world_;
	}
	
	public void init()
	{
		loadTextures();
		loadFonts();
	}
	
	public void loadFonts()
	{	
	}
	
	public void loadTextures()
	{
		try {
			textureDirt   = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/dirt.png"));
			textureStone  = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/stone.png"));
			texturePlayer = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/player.png"));
			textureSky    = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/sky.png"));			
			textureGray   = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/gray.png"));			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void renderSky()
	{
		DrawTexturedBox(0 - Game.DisplayWidth / 2, 0 - Game.DisplayHeight / 2, Game.DisplayWidth / 2, Game.DisplayHeight / 2, textureSky.getTextureID());
	}
	
	public void testVBO()
	{
		Screever screever = Screever.instance;

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-12.5 + (float)world.player.posX, 12.5 + (float)world.player.posX, -9.375 + (float)world.player.posY, 9.375 + (float)world.player.posY, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, textureStone.getTextureID());
		screever.startDrawingQuads();
		screever.addVertexWithUV(-1, -1, -1, -1);
		screever.addVertexWithUV(-1,  1, -1,  1);
		screever.addVertexWithUV( 1,  1,  1,  1);
		screever.addVertexWithUV( 1, -1,  1, -1);
		screever.draw();
		glDisable(GL_TEXTURE_2D);
	}
	
	public void drawGrid()
	{
		Screever screever = Screever.instance;
		screever.startDrawing(GL_LINES);
		glPushMatrix();
		glColor3f(0, 1, 0);
		
		for (int y = -10; y <= 10; y++)
		{
			for (int x = -10; x <= 10; x ++)
			{
				screever.addVertex(x, -10);
				screever.addVertex(x, 10);
			}
			screever.addVertex(-10, y);
			screever.addVertex(10, y);
		}
		screever.draw();
		glPopMatrix();
	}
	
	public void renderPlayer()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-12.5 + (float)world.player.posX, 12.5 + (float)world.player.posX, -9.375 + (float)world.player.posY, 9.375 + (float)world.player.posY, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		float x1 = (float) world.player.posX - 0.5f;
		float y1 = (float) world.player.posY - 0.5f;
		float x2 = (float) world.player.posX + 0.5f;
		float y2 = (float) world.player.posY + 0.5f;
		DrawTexturedBox(x1, y1, x2, y2, texturePlayer.getTextureID());
	}
	
	public void renderMap()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-12.5 + (float)world.player.posX, 12.5 + (float)world.player.posX, -9.375 + (float)world.player.posY, 9.375 + (float)world.player.posY, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//glTranslatef((float)world.player.posX, (float)world.player.posY, 0);
		glPushMatrix();
		for (int i = 0; i < world.chunkProvider.loadedChunks.getSize(); i++)
		{
			Chunk chunk = (Chunk) world.chunkProvider.loadedChunks.getValueByIndex(i);
			for (int j = 0; j < Chunk.width * Chunk.height; j++) {
					int blockID = chunk.blocks[j];
					
					float x1 = (chunk.x * Chunk.width + ((j & 0x3f)));
					float x2 = x1 + 1;
					float y1 = (chunk.y * Chunk.height + ((j & 0xfc0) >> 6));
					float y2 = y1 + 1;
					if (!isCoordInScreen(x1, y1)) continue;
					if (blockID == Block.blockDirt.blockID) {
						DrawTexturedBox(x1, y1, x2, y2, textureDirt.getTextureID());
					} else if (blockID == Block.blockStone.blockID) {
						DrawTexturedBox(x1, y1, x2, y2, textureStone.getTextureID());
					} else {
						continue;
					}
					//b = world.getBlockUnderEntity((Entity) world.player);
					//if (b != null) {
					//	b.setHighlighted(true);
					//	highlightBlock(b);
					//}
					
			}
		}
		glPopMatrix();
	}
	
	public boolean isCoordInScreen(float x, float y)
	{
		return ((world.player.posX - x) > -13 & (world.player.posX - x) < 14 &
				(world.player.posY - y) > -12 & (world.player.posY - y)  < 12);
	}
	
	public void highlightBlock(Block b)
	{
		float bx = getScreenCoordX(b.x);
		float by = getScreenCoordY(b.y);
		glColor3f(0, 0.8f, 0);
		glBegin(GL_LINES);
			glVertex2f(bx - blockSize / 2 * scale, by - blockSize / 2 * scale);
			glVertex2f(bx - blockSize / 2 * scale, by + blockSize / 2 * scale);
			glVertex2f(bx - blockSize / 2 * scale, by + blockSize / 2 * scale);
			glVertex2f(bx + blockSize / 2 * scale, by + blockSize / 2 * scale);
			glVertex2f(bx + blockSize / 2 * scale, by + blockSize / 2 * scale);
			glVertex2f(bx + blockSize / 2 * scale, by - blockSize / 2 * scale);
			glVertex2f(bx + blockSize / 2 * scale, by - blockSize / 2 * scale);
			glVertex2f(bx - blockSize / 2 * scale, by - blockSize / 2 * scale);			
		glEnd();
	}
	
	public void DrawTexturedBox(float x1, float y1, float x2, float y2, int textureID)
	{
		Screever screever = Screever.instance;
		
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);	
		
		glColor3f(1, 1, 1);
		screever.startDrawingQuads();
		screever.addVertexWithUV(x1, y1, 0, 1);
		screever.addVertexWithUV(x2, y1, 1, 1);
		screever.addVertexWithUV(x2, y2, 1, 0);
		screever.addVertexWithUV(x1, y2, 0, 0);
		screever.draw();

		glDisable(GL_TEXTURE_2D);		
	}
	
	public void drawPartOfTexture(float x1, float y1, float x2, float y2, float tx1, float ty1, float tx2, float ty2, int textureID)
	{
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		glColor3f(1, 1, 0.5f);
		glBegin(GL_QUADS);
			glTexCoord2f(tx1, ty1); glVertex2f(x1, y1);
			glTexCoord2f(tx2, ty1); glVertex2f(x2, y1);
			glTexCoord2f(tx2, ty2); glVertex2f(x2, y2);
			glTexCoord2f(tx1, ty2); glVertex2f(x1, y2);
		glEnd();
	}
	
	public void renderOverlay()
	{
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-400, 400, -300, 300, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glPushMatrix();
		glColor3f(1, 0, 0);
		glTranslatef(0, 0, 0);
		
		Screever screever = Screever.instance;
		
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureGray.getTextureID());
		
		screever.startDrawingQuads();
		screever.setColorOpaque(0, 0, 0);
		screever.addVertexWithUV(-390, 190, 0, 1);
		screever.addVertexWithUV(-140, 190, 1, 1);
		screever.addVertexWithUV(-140, 300, 1, 0);
		screever.addVertexWithUV(-390, 300, 0, 0);
		screever.draw();
		glDisable(GL_TEXTURE_2D);				
		
		int x = Mouse.getX();
		int y = Mouse.getY();
		Game.fr.renderString("m2d, version 0.0.1a", -380, 290, 1);
		Game.fr.renderString("Mouse X:" + getSceneCoordX(x), -380, 270, 1);
		Game.fr.renderString("Mouse Y:" + getSceneCoordY(y), -380, 260, 1);
		Game.fr.renderString("Player X:" + world.player.posX, -380, 250, 1);
		Game.fr.renderString("Player Y:" + world.player.posY, -380, 240, 1);
		Game.fr.renderString("fps: " + Game.lastFps, -380, 230, 1);
		Game.fr.renderString("Memory: " + Game.memoryUsed + "MB", -380, 220, 1);
		Game.fr.renderString("SpeedY: " + Game.player.speedY, -380, 210, 1);
		//Game.fr.renderString("" + world.player.stopTimer, -370, 220, 1);
		glPopMatrix();
	}
	
	public int getScreenCoordX(double x_)
	{
		return (int) (x_ * scale * blockSize - world.player.posX * scale * blockSize);
	}
	
	public int getScreenCoordY(double y_)
	{
		return (int) (y_ * scale * blockSize - world.player.posY * scale * blockSize);
	}
	
	public double getSceneCoordX(int x_)
	{
		return (double) world.player.posX + (double) ((x_ - Game.DisplayWidth / 2) / ((double) (scale * blockSize)));
	}
	
	public double getSceneCoordY(int y_)
	{
		return (double) world.player.posY + (double) ((y_ - Game.DisplayHeight / 2) / ((double) (scale * blockSize)));	
	}
	
	public boolean isBlockVisible(Block b)
	{
		int bx = getScreenCoordX(b.x);
		int by = getScreenCoordY(b.y);
		return  !(bx < - Game.DisplayWidth / 2 - blockSize * scale | 
				  by < - Game.DisplayHeight / 2 - blockSize * scale | 
				  bx > Game.DisplayWidth / 2 + blockSize * scale |
				  by > Game.DisplayHeight / 2 + blockSize * scale);
	}
}