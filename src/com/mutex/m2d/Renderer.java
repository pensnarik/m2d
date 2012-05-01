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
	public Texture texDirt;
	public Texture texStone;
	public Texture textPlayer;
	public Texture textSky;
	public Texture textGray;
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
			texDirt = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/dirt.png"));
			texStone = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/stone.png"));
			textPlayer = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/player.png"));
			textSky = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/sky.png"));			
			textGray = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/gray.png"));			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void renderSky()
	{
		DrawTexturedBox(0 - Game.DisplayWidth / 2, 0 - Game.DisplayHeight / 2, Game.DisplayWidth / 2, Game.DisplayHeight / 2, textSky.getTextureID());
	}
	
	public void drawGrid()
	{
		glColor3f(0, 1, 0);
		for (int y = 0; y < Display.getHeight(); y+= 16) {
			for (int x = 0; x < Display.getWidth(); x += 16) {
				glBegin(GL_LINES);
					glVertex2f(x, 0);
					glVertex2f(x, Display.getHeight());
				glEnd();
			}
			glBegin(GL_LINES);
				glVertex2f(0, y);
				glVertex2f(Display.getWidth(), y);
			glEnd();
		}
	}
	
	public void renderPlayer()
	{
		double x = world.player.posX;
		double y = world.player.posY;
		float x1 = (float) getScreenCoordX( world.player.bb.x1 );
		float y1 = (float) getScreenCoordY( world.player.bb.y1 );
		float x2 = (float) getScreenCoordX( world.player.bb.x2 );
		float y2 = (float) getScreenCoordY( world.player.bb.y2 );
		DrawTexturedBox(x1, y1, x2, y2, textPlayer.getTextureID());
	}
	
	public void renderMap()
	{
		for (int i = 0; i < world.chunkProvider.loadedChunks.getSize(); i++)
		{
			Chunk chunk = (Chunk) world.chunkProvider.loadedChunks.getValueByIndex(i);
			for (int j = 0; j < Chunk.height * Chunk.height; j++) {
					int blockID = chunk.blocks[j];
					//if (!isBlockVisible(b)) continue;
					int x1 = getScreenCoordX(chunk.x + (j & 0xf0) >> 4) - blockSize / 2 * scale;
					int x2 = getScreenCoordX(chunk.x + (j & 0xf0) >> 4) + blockSize / 2 * scale;
					int y1 = getScreenCoordY(chunk.y + (j & 0x0f)) - blockSize / 2 * scale;
					int y2 = getScreenCoordY(chunk.y + (j & 0x0f)) + blockSize / 2 * scale;
					if (blockID == 0) {
						DrawTexturedBox(x1, y1, x2, y2, texDirt.getTextureID());
					} else if (blockID == 1) {
						DrawTexturedBox(x1, y1, x2, y2, texStone.getTextureID());
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
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, textureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);			
		
		glColor3f(1, 1, 1);
		glBegin(GL_QUADS);	
			glTexCoord2f(0, 1);
			glVertex2f(x1, y1);
			glTexCoord2f(1, 1);
			glVertex2f(x2, y1);
			glTexCoord2f(1, 0);
			glVertex2f(x2, y2);
			glTexCoord2f(0, 0);
			glVertex2f(x1, y2);
		glEnd();
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
		/*
		 This doesn't works properly now.
		 
		 Color.black.bind(); 
	     font.drawString(100, 200, "Hello!");
		*/
		DrawTexturedBox(-390, 190, -140, 300, textGray.getTextureID());
		glPushMatrix();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0,  800, 0, 600, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glTranslatef(0, 0, 0);
		int x = Mouse.getX();
		int y = Mouse.getY();
		Game.fr.renderString("m2d, version 0.01 alpha", -380, 290, 1);
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