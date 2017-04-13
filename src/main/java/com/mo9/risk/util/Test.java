package com.mo9.risk.util;

import java.util.concurrent.locks.ReentrantLock;

public class Test implements Runnable {
//	ReentrantLock lock = new ReentrantLock();
	Lock lock  = new Lock();

	public void get() {
		try {
			lock.lock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getId());
		set();
		lock.unlock();
		set();
	}

	public void set() {
		try {
			lock.lock();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(222);
		lock.unlock();
	}

	@Override
	public void run() {
		get();
	}

	public static void main(String[] args) {
		Test ss = new Test();
//		ReentrantLock lock = new ReentrantLock();
//		Lock lock  = new Lock();
//		try {
//			lock.lock();
//			new Thread(ss).start();
//			lock.unlock();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		new Thread(ss).start();
		new Thread(ss).start();
		new Thread(ss).start();
		
	}
}

