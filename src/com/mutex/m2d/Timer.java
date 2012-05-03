package com.mutex.m2d;

public class Timer
{
	public int elapsedTicks;
	private long lastSyncSysClock;
	private long lastSyncHRClock;
	private double lastHRTime;
	private long tmp;
	private double timeSyncAdjustment;
	public float elapsedPartialTicks;	
	public float timerSpeed;
	public float renderPartialTicks;
	float ticksPerSecond;
	
	public Timer(int ticksPerSecond_)
	{
		ticksPerSecond = ticksPerSecond_;
		lastSyncSysClock = System.currentTimeMillis();
		lastSyncHRClock = System.nanoTime() / 1000000;
		timeSyncAdjustment = 1;
		elapsedPartialTicks = 0;
		timerSpeed = 1;
		System.out.println("Timer initialized with value = " + ticksPerSecond);		 
	}
	
	public void updateTimer()
	{
		long now = System.currentTimeMillis();
		long delta = now - lastSyncSysClock;
		long deltaSec = System.nanoTime() / 1000000;
		double deltaHR = (double) deltaSec / 1000D;
		if (delta > 1000)
		{
			lastHRTime = deltaHR;
		} else if (delta < 0) {
			lastHRTime = deltaHR;
		} else {
			tmp += delta;
			if (tmp > 1000)
			{
				long t1 = deltaSec - lastSyncHRClock;
				double t2 = (double) tmp / (double)  t1;
				timeSyncAdjustment += (t2 - timeSyncAdjustment) * 0.20000000298023224D;
				lastSyncHRClock = deltaSec;
				tmp = 0;
			}
			if (tmp < 0)
			{
				lastSyncHRClock = deltaSec;
			}
		}
		lastSyncSysClock = now;
		double delta2 = (deltaHR - lastHRTime) * timeSyncAdjustment;
		lastHRTime = deltaHR;
		if (delta2 < 0)
		{
			delta2 = 0;
		}
		if (delta2 > 1)
		{
			delta2 = 1;
		}
		elapsedPartialTicks += delta2 * (double) timerSpeed * (double) ticksPerSecond;
		elapsedTicks = (int) elapsedPartialTicks;
		elapsedPartialTicks -= elapsedTicks;
		if (elapsedTicks > 10)
		{
			elapsedTicks = 10;
		}
		renderPartialTicks = elapsedPartialTicks;		
	}
}