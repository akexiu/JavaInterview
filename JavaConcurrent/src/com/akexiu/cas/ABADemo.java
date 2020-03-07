package com.akexiu.cas;

import java.sql.SQLOutput;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 描述:
 * ABA问题
 *
 * @outhor akexiu
 * @create 2020-03-07 20:40
 */
public class ABADemo {
    private static AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
    //用于解决ABA问题的原子引用，加版本号
    private static AtomicStampedReference<Integer> stampedReference = new AtomicStampedReference<>(100, 1);

    public static void main(String[] args) {
        System.out.println("===以下是 ABA 问题的产生===");
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "t1").start();
        new Thread(() -> {
            //先暂停 1 秒 保证完成 ABA
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch
            (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(atomicReference.compareAndSet(100,
                    2019) + "\t" + atomicReference.get());
        }, "t2").start();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch
        (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("===以下是 ABA 问题的解决===");
        new Thread(() -> {
            int stamp = stampedReference.getStamp();

            System.out.println(Thread.currentThread().getName() + "\t 第 1 次版本号" + stamp + "\t 值是" + stampedReference.getReference());
            //暂停 1 秒钟 t3 线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch
            (InterruptedException e) {
                e.printStackTrace();
            }

            stampedReference.compareAndSet(100, 101, stampedReference.getStamp(), stampedReference.getStamp() + 1);

            System.out.println(Thread.currentThread().getName() + "\t 第 2 次 版 本 号 " + stampedReference.getStamp() + "\t 值 是  " + stampedReference.getReference());

            stampedReference.compareAndSet(101, 100, stampedReference.getStamp(), stampedReference.getStamp() + 1);

            System.out.println(Thread.currentThread().getName() + "\t 第 3 次 版 本 号 " + stampedReference.getStamp() + "\t 值 是 " + stampedReference.getReference());
        }, "t3").start();
        new Thread(() -> {
            int stamp = stampedReference.getStamp();

            System.out.println(Thread.currentThread().getName() + "\t 第 1 次版本号" + stamp + "\t 值是" + stampedReference.getReference());
            //保证线程 3 完成 1 次 ABA
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch
            (InterruptedException e) {
                e.printStackTrace();
            }
            boolean result =
                    stampedReference.compareAndSet(100, 2019, stamp, stamp + 1);

            System.out.println(Thread.currentThread().getName() + "\t 修 改成功否" + result + "\t 最新版本号" + stampedReference.getStamp());
            System.out.println(" 最 新 的 值 \t" + stampedReference.getReference());
        }, "t4").start();
    }

}
