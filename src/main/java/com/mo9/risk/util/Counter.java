package com.mo9.risk.util;

public class Counter{
	
    private Lock lock = new Lock();
    private int count = 0;
    public int inc() throws InterruptedException{
        lock.lock();
        this.count++;
        lock.unlock();
        return count;
    }
    
//    public static void main(String[] args) {
//    	Count
//	}
    
}
