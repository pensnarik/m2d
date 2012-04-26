package com.mutex.m2d;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import static org.lwjgl.opengl.GL11.*;

public class FontRenderer
{
	private Texture textFont;
	public Renderer renderer;
	public static final int CharWidth = 7;
	public static final int CharHeight = 7;
	
	public FontRenderer(Renderer renderer_)
	{
		renderer = renderer_;
		try {
			textFont = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("./res/font.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * x and y - coordinates of the left bottom corner of the text label
	 */
	
	public void renderString(String s, int x, int y, int scale)
	{
		glPushMatrix();
		renderer.drawPartOfTexture(x, y, x + CharWidth * scale, y - CharWidth * scale, 0.0f, 0.0f, 1.0f/16, 1.0f/16, textFont.getTextureID());
		
		for (int i = 0; i < s.length(); i++)
		{
			int pos = 0;
			int c = (int) s.toLowerCase().charAt(i);
			if (c >= 0x30 && c <= 0x39) {
				pos = c - 0x30;
				int x1 = 5 * pos;
				int x2 = 5 * pos + 5;
				int y1 = 0;
				int y2 = 7;
//				renderer.drawPartOfTexture(100, 200, 228, 100, 0.0f, 0.0f, 1.0f, 1.0f, textFont.getTextureID());
			} else {
				return;
			}
		}
		
		glPopMatrix();
	}
}