package com.akexiu.setlistmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 描述:
 * 我们知道ArrayList是线程不安全,请编写一个不安全的 案例并给出解决方案
 *
 * @outhor akexiu
 * @create 2020-03-07 20:57
 */

/**
 * 解决方案有三种,建议用第三种
 * 1，  new Vector<>()
 * 2,Collections.synchronizedList(new  ArrayList<>());
 * 3 new CopyOnWriteArrayList<>();
 * <p>
 * <p>
 * 写时复制 copyOnWrite 容器即写时复制的容器 往容器添加元素的时候,不直接往当前容器 object[]添加,而是先将当前容器 object[]进行
 * copy 复制出一个新的 object[] newElements 然后向新容器 object[] newElements 里面添加元素 添加元素后,
 * * 再将原容器的引用指向新的容器 setArray(newElements); 这样的好处是可以对 copyOnWrite 容器进行并发的读,而不需要加锁
 * 因为当前容器不会添加任何容器.所以 copyOnwrite 容器也是一种 读写分离的思想,读和写不同的容器.
 * 写时复制，以下为底层实现
 * * public boolean add(E e) {
 * * final ReentrantLock lock = this.lock;
 * * lock.lock();
 * * try {
 * * Object[] elements = getArray();
 * * int len = elements.length;
 * * Object[] newElements =
 * Arrays.copyOf(elements, len + 1);
 * * newElements[len] = e;
 * * setArray(newElements);
 * * return true;
 * * } finally {
 * * lock.unlock();
 * * }
 * * }
 * <p>
 * <p>
 * List 线程 copyOnWriteArrayList
 * set 线程 CopyOnwriteHashSet
 * map 线程 ConcurrentHashMap
 */

public class ArrayListDemo {
    //集合类不安全的问题,高并发情况下会出现ConcurrentModificationException异常
    public static void main(String[] args) {
        //notThreadSafe();
        // safeListThread();
        //List 线程 copyOnWriteArrayList
        // set 线程 CopyOnwriteHashSet
        // map 线程 ConcurrentHashMap
        Map<String,Object> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                   map.put(Thread.currentThread().getName(),UUID.randomUUID().toString());
                    System.out.println(map);
                }
            }, String.valueOf(i)).start();
        }
    }

    //线程安全的List
    private static void safeListThread() {
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(UUID.randomUUID().toString());
                    System.out.println(list);
                }
            }, String.valueOf(i)).start();
        }
    }

    private static void notThreadSafe() {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    list.add(UUID.randomUUID().toString());
                    System.out.println(list);
                }
            }, String.valueOf(i)).start();
        }
    }
}
