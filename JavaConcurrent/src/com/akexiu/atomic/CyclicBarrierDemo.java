package com.akexiu.atomic;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 描述:
 * CyclicBarrier的使用
 * 这个CyclicBarrier是做加法操作
 *CountDownLatch	            CyclicBarrier
 * 减计数方式	                加计数方式
 * 计算为0时释放所有等待的线程	    计数达到指定值时释放所有等待线程
 * 计数为0时，无法重置	        计数达到指定值时，计数置为0重新开始
 * 调用countDown()方法计数减一，  调用await()方法计数加1，若加1后的值不等于构造方法的值，则线程阻塞
 * 调用await()方法只进行阻塞，
 * 对计数没任何影响
 * 不可重复利用	                可重复利用
 * ————————————————
 * @outhor akexiu
 * @create 2020-03-07 17:51
 */
public class CyclicBarrierDemo {
    public static void main(String[] args) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5,()->System.out.println("可以合成福卡了"));
        for (int i = 1; i < 6; i++) {
            int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() +"\t收集到第" +finalI+"个福卡");
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            },String.valueOf(i)).start();
        }
    }
}
