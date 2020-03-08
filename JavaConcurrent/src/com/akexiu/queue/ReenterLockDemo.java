package com.akexiu.queue;

import sun.nio.ch.Net;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述:
 * 重入锁
 *
 * @outhor akexiu
 * @create 2020-03-08 12:06
 */

public class ReenterLockDemo {
    public static class Phone {
        //##################synchronized实现重入锁####################
        public synchronized void sendEmail() {
            System.out.println("email");
            sendSMS();
        }

        public synchronized void sendSMS() {
            System.out.println("sms");
        }
        //##################synchronized实现重入锁####################


        //##################ReentrantLock实现重入锁####################
        private Lock lock = new ReentrantLock();
        public void get() {
            try {
                lock.lock();
                set();
                System.out.println("get");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void set() {
            try {
                lock.lock();
                System.out.println("set");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void runMethod() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    get();
                }
            }).start();
        }
        //##################ReentrantLock实现重入锁####################

    }

    public static void main(String[] args) {
        Phone phone = new Phone();
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    phone.sendEmail();
                }
            }).start();
        }

        //##################ReentrantLock实现重入锁####################
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        phone.runMethod();
        phone.runMethod();
    }
}
