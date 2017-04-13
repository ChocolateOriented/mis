package com.mo9.risk.util;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLock1 {
	private AtomicReference<Thread> owner =new AtomicReference<Thread>();
	private int count =0;
	public void lock(){
		Thread current = Thread.currentThread();
		if(current==owner.get()) {
			count++;
			return ;
		}

		while(!owner.compareAndSet(null, current)){

		}
	}
	public void unlock(){
		Thread current = Thread.currentThread();
		if(current==owner.get()){
			if(count!=0){
				count--;
			}else{
				owner.compareAndSet(current, null);
			}

		}

	}
}

//public class SpinLock {
//	private AtomicReference<Thread> owner =new AtomicReference<Thread>();
//	public void lock(){
//		Thread current = Thread.currentThread();
//		while(!owner.compareAndSet(null, current)){
//		}
//	}
//	public void unlock (){
//		Thread current = Thread.currentThread();
//		owner.compareAndSet(current, null);
//	}
//}