package com.akexiu.javavolatile;

/**
 * 描述:
 * 你在哪些地方用到过 volatile?
 * <p>
 * 单例模式，由于单例模式多线程下不是线程安全的，需要用到双重检查锁和volatile来保证线程安全
 * 因为DCL(双端检锁) 机制不一定线程安全,原因是有指令重排的存在,加入 volatile 可以禁止指令重排
 *
 * @outhor akexiu
 * @create 2020-03-08 11:07
 */
public class SingletonDemo {
    //这个是线程安全的
    //private static volatile SingletonDemo singletonDemo = null;
    //这个是线程不安全的，
    private static SingletonDemo singletonDemo = null;

    public SingletonDemo() {
        System.out.println("我是构造函数");
    }

    public static SingletonDemo getInstance() {

        if (singletonDemo == null) {
            //线程安全
//            synchronized (SingletonDemo.class) {
//                if (singletonDemo == null) {
//                    singletonDemo = new SingletonDemo();
//                }
//            }
            //线程不安全
            singletonDemo = new SingletonDemo();
        }
        return singletonDemo;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(getInstance() == getInstance());
                }
            }).start();
        }

    }
}
