package com.mutex.m2d;

import java.math.*;

public class MathHelper {
	public static int round_float(float f)
	{
		int i = (int) f;
		if (Math.abs(f - i) >= 0.5)
		{
			return i + 1;
		}
		else
		{
			return i;
		}
	}
	
    public static int floor_double(double d)
    {
        int i = (int) d;
        return d >= (double) i ? i : i - 1;
    }	
}