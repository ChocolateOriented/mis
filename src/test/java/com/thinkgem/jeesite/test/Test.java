package com.thinkgem.jeesite.test;

import java.util.Date;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

public class Test {

	public static void main(String[] args) {
		TaskScheduler taskScheduler = new ConcurrentTaskScheduler();
		taskScheduler.schedule(new Runnable() {
			@Override
			public void run() {
				System.out.println("执行");
			}
		}, new Date(System.currentTimeMillis()+1000));
//		taskScheduler.schedule(task, trigger)
	}
}
