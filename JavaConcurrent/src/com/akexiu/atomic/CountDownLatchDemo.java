package com.akexiu.atomic;


import java.util.concurrent.CountDownLatch;

/**
 * 描述:
 * 计数器
 * 枚举的使用实现
 *CountDownLatch  减法操作
 * @outhor akexiu
 * @create 2020-03-07 16:55
 */
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {

        System.out.println(Fruit.forEachFruit(1).getMessage());
        final int num = 6;
        CountDownLatch countDownLatch = new CountDownLatch(num);
        try {
            for (int i = 1; i < 7; i++) {
                int finalI = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(finalI + "点吃:" + Thread.currentThread().getName());
                        //计数
                        countDownLatch.countDown();
                    }
                }, Fruit.forEachFruit(i).getMessage()).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
        countDownLatch.await();//等待前面线程执行完毕，用来阻塞主线程，当计数器为0时，不在阻塞
        System.out.println(Thread.currentThread().getName() + "已经吃完");
    }

    //枚举类，水果
    //枚举类的使用
    public enum Fruit {
        ONE(1, "apple"), TWO(2, "Apricot"), THREE(3, "litchi"), FOUR(4, "orgin"), FIVE(5, "cane"), SIX(6, "Kiwi");

        //定义枚举的构造函数
        Fruit(int code, String message) {
            this.code = code;
            this.message = message;
        }

        public static Fruit forEachFruit(int index) {
            Fruit[] array = Fruit.values();
            for (Fruit fruit : array) {
                if (index == fruit.getCode()) {
                    return fruit;
                }
            }
            return null;
        }

        private int code;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        private String message;


    }
}

