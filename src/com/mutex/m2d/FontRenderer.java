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
	public static final int CharWidth = 8;
	public static final int CharHeight = 8;
	
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
		int pos; int col; int row;
		
		for (int i = 0; i < s.length(); i++)
		{
			int c = (int) s.charAt(i);
			if (c >= 0x30 && c <= 0x39) {
				pos = c - 0x30;
			} else if (c == 0x2e) {
				pos = 80;
			} else if (c == 0x2d) { /* '-' */
				pos = 81;
			} else if (c == 0x2b) { /* '+' */
				pos = 82;
			} else if (c >= 0x41 && c <= 0x5a) { /* 'A' - 'Z' */
				pos = c - 0x41 + 17;
			} else if (c >= 0x61 && c <= 0x7a) { /* 'a' - 'z' */
				pos = c - 0x61 + 49;
			} else if (c == 0x3a) {
				pos = 10;
			} else {
				pos = -1;
			}
			if (pos < 0) continue;
			col = pos % 16;
			row = (int)(pos / 16);
			float tx1 = (float)(col*(float)1.0f/16);
			float tx2 = (float)((col + 1) *(float)1.0f/16);
			float ty1 = (float)(row*(float)1.0f/16);
			float ty2 = (float)((row + 1) *(float)1.0f/16);
			/* TODO: Change to renderTexturedRect() */
			renderer.drawPartOfTexture(x + CharWidth * scale * i,
					   y,
					   x + CharWidth * scale * (i + 1),
					   y - CharHeight * scale,
					   tx1,
					   ty1,
					   tx2,
					   ty2,
					   textFont.getTextureID());
		}
		
		glPopMatrix();
	}
}