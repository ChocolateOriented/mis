package com.mo9.risk.util;

public class Count{
    Lock lock = new Lock();
    public void print() throws InterruptedException{
        lock.lock();
        System.out.println("123123");
        doAdd();
        lock.unlock();
    }
    public void doAdd() throws InterruptedException{
        lock.lock();
        System.out.println("111");
        lock.unlock();
    }
    
   
}
