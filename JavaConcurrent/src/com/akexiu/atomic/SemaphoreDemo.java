package com.akexiu.atomic;

import java.util.concurrent.Semaphore;

/**
 * 描述:
 * Semaphore信号灯的用法
 * <p>
 * Semaphore当前在多线程环境下被扩放使用，操作系统的信号量是个很重要的概念，在进程控制方面都有应用。
 * Java 并发库 的Semaphore 可以很轻松完成信号量控制，Semaphore可以控制某个资源可被同时访问的个数，
 * 通过 acquire() 获取一个许可，如果没有就等待，而 release() 释放一个许可。比如在Windows下可以设置共享文件的最大客户端访问个数。
 * <p>
 * Semaphore维护了当前访问的个数，提供同步机制，控制同时访问的个数。在数据结构中链表可以保存“无限”的节点，
 * 用Semaphore可以实现有限大小的链表。另外重入锁 ReentrantLock 也可以实现该功能，但实现上要复杂些。
 *
 * @outhor akexiu
 * @create 2020-03-07 18:08
 */
public class SemaphoreDemo {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 1; i < 6; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName()+"\t"+ finalI +"抢到车位");
                        int time = new java.util.Random().nextInt(10)+1;
                        Thread.sleep(time);
                        System.out.println(Thread.currentThread().getName()+"\t"+ finalI +"车"+time+"分钟后离开车位");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        semaphore.release();
                    }

                }
            },String.valueOf(i)).start();
        }
    }
}
