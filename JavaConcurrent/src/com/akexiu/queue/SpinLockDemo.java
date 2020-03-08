package com.akexiu.queue;

import java.util.concurrent.atomic.AtomicReference;

/**
 * 描述:
 * 手写自旋锁
 *
 * @outhor akexiu
 * @create 2020-03-08 11:36
 */
public class SpinLockDemo {
    //原子引用线程
    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void mylock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "关门");
        //此处自旋，
        while (!atomicReference.compareAndSet(null, thread)) {}
    }

    public void unlock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(thread.getName() + "开门");
    }

    public static void main(String[] args) {
        SpinLockDemo spinLockDemo = new SpinLockDemo();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    spinLockDemo.mylock();
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spinLockDemo.unlock();
            }
        }, "t1").start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    spinLockDemo.mylock();
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                spinLockDemo.unlock();
            }
        }, "t2").start();
    }
}
