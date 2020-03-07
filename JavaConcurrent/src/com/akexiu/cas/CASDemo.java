package com.akexiu.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 描述:
 * 面试题：
 * 1，你知道CAS吗
 * 比较并且交换
 * CAS 的全称为 Compare-And-Swap ,它是一条 CPU 并发原语.
 * 它的功能是判断内存某个位置的值是否为预期值,如果是则更新为新的值,这个过程是原子的.
 * CAS 并发原语提现在 Java 语言中就是 sun.miscUnSaffe 类中的各个方法.调用 UnSafe 类中
 * 的 CAS 方法,JVM 会帮我实现 CAS 汇编指令.这是一种完全依赖于硬件 功能,通过它实现了
 * 原子操作,再次强调,由于 CAS 是一种系统原语,原语属于操作系统用于范畴,是由若干条指
 * 令组成,用于完成某个功能的一个过程,并且原语的执行必须是连续的,在执行过程中不允许
 * 中断,也即是说 CAS 是一条原子指令,不会造成所谓的数据不一致的问题
 *
 *,2，CAS 底层原理?如果知道,谈谈你对 UnSafe 的理解
     * 1.UnSafe
     * 是 CAS 的核心类 由于 Java 方法无法直接访问底层 ,需要通过本地(native)方法来访
     * 问,UnSafe 相当于一个后面,基于该类可以直接操作特额定的内存数据.UnSafe 类在于
     * sun.misc 包中,其内部方法操作可以向 C 的指针一样直接操作内存,因为 Java 中 CAS 操作的
     * 助兴依赖于 UNSafe 类的方法.
     * 注意 UnSafe 类中所有的方法都是 native 修饰的,也就是说 UnSafe 类中的方法都是直接调
     * 用操作底层资源执行响应的任务
     * 2.变量 ValueOffset,便是该变量在内存中的偏移地址,因为 UnSafe 就是根据内存偏移地址
     * 获取数据的
     * 3.变量 value 和 volatile 修饰,保证了多线程之间的可见性.
 *CAS 缺点
 * 循环时间长开销很大，因为compareAndSwapInt(Object var1, long var2, int var4, int var5)
 * 这个方法的实现是一个自旋锁，一直到成功才退出循环
 * 只能保证一个共享变量的原子性
 *
 * 引出来 ABA 问题
 *原子类 AtomicInteger 的 ABA 问题谈谈?原子更新引用是什么
 *CAS算法实现一个重要前提需要取出内存中某时刻的数据，而在下时刻比较并替换，那么在这个时间差类会导致数据的变化。
 *
 * 比如说一个线程one从内存位置V中取出A，这时候另一个线程two也从内存中取出A，并且two进行了一些操作变成了B，然后two又将V位置的数据变成A，
 * 这时候线程one进行CAS操作发现内存中仍然是A，然后one操作成功。尽管线程one的CAS操作成功，但是不代表这个过程就是没有问题的。
 *
 * 如果链表的头在变化了两次后恢复了原值，但是不代表链表就没有变化。因此前面提到的原子操作AtomicStampedReference/AtomicMarkableReference就很有用了。
 * 这允许一对变化的元素进行原子操作。
 *
 * 在运用CAS做Lock-Free操作中有一个经典的ABA问题：
 *
 * 线程1准备用CAS将变量的值由A替换为B，在此之前，线程2将变量的值由A替换为C，又由C替换为A，然后线程1执行CAS时发现变量的值仍然为A，
 * 所以CAS成功。但实际上这时的现场已经和最初不同了，尽管CAS成功，但可能存在潜藏的问题，
 * @outhor akexiu
 * @create 2020-03-07 20:17
 */
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        //比较并且交换，如果期望值int expect是一样的, int update则改成需要的值
        atomicInteger.compareAndSet(5,9);
        System.out.println(atomicInteger.get());
    }
}
