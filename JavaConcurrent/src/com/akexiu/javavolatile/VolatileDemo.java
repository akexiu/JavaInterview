package com.akexiu.javavolatile;

/**
 * 描述:
 * 谈谈你对 volatile 的理解
 * volatile 是 Java 虚拟机提供的轻量级的同步机制，
 * 1,保证可见性，各个线程对主内存中共享变量的操作都是各个线程各自拷贝到自己的工作内存操作后再写回主内存中的.
 * 2,不保证原子性
 * 3,禁止指令重排
 *
 *这就可能存在一个线程 AAA 修改了共享变量 X 的值还未写回主内存中时 ,另外一个线程
 * BBB 又对内存中的一个共享变量 X 进行操作,但此时 A 线程工作内存中的共享比那里 X 对
 * 线程 B 来说并不不可见.这种工作内存与主内存同步延迟现象就造成了可见性问题
 *
 * 谈谈JMM 是什么
 * JMM(Java 内存模型 Java Memory Model,简称 JMM)本身是一种抽象的概念 并不真实存在,它
 * 描述的是一组规则或规范通过规范定制了程序中各个变量(包括实例字段,静态字段和构成数
 * 组对象的元素)的访问方式.
 * JMM 关于同步规定:
 * 1.线程解锁前,必须把共享变量的值刷新回主内存
 * 2.线程加锁前,必须读取主内存的最新值到自己的工作内存
 * 3.加锁解锁是同一把锁
 * 由于 JVM 运行程序的实体是线程,而每个线程创建时 JVM 都会为其创建一个工作内存(有些
 * 地方成为栈空间),工作内存是每个线程的私有数据区域,而 Java 内存模型中规定所有变量都
 * 存储在主内存,主内存是共享内存区域,所有线程都可访问,但线程对变量的操作(读取赋值等)
 * 必须在工作内存中进行,首先要将变量从主内存拷贝到自己的工作空间,然后对变量进行操作,
 * 操作完成再将变量写回主内存,不能直接操作主内存中的变量,各个线程中的工作内存储存着
 * 主内存中的变量副本拷贝,因此不同的线程无法访问对方的工作内存,此案成间的通讯(传值)
 * 必须通过主内存来完成
 *
 * @outhor akexiu
 * @create 2020-03-07 19:58
 */
public class VolatileDemo {
    private static  int num = 0;//没有加volatile关键字，多线程操作变量，其他线程对变量进行修改，而没有通知其他线程，导致线程被阻塞
    //private static volatile int num = 0;//
    public static void addNum() {
        num = 10;
    }
    public static void main(String[] args) {
        //1,可见性代码证明
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName() + "\t我来了");
                    Thread.sleep(3000);
                    addNum();//修改值
                    System.out.println(Thread.currentThread().getName() + "\t我把数据改成了"+num);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "thread").start();

        //如果没有加volatile，线程会被阻塞，
        while (num == 0){}
        System.out.println(Thread.currentThread().getName() + "\t结束了");

    }
}

