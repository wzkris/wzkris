package com.thingslink.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 锁测试
 * @date : 2023/9/21 16:13
 */
@DisplayName("锁测试")
public class LockTest {
    final static Integer max = 1000;
    static Integer num = 0;

    @Test
    public void simpleTest() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (max) {
                while (num < max) {
                    System.out.println(Thread.currentThread().getName() + num);
                    num++;
                    max.notify();
                    try {
                        if (num < max) {
                            max.wait();
                        }
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (max) {
                while (num < max) {
                    System.out.println(Thread.currentThread().getName() + num);
                    num++;
                    max.notify();
                    try {
                        if (num < max) {
                            max.wait();
                        }
                    }
                    catch (InterruptedException e) {
                    }
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    @Test
    public void cyclicTest() throws BrokenBarrierException, InterruptedException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);
        cyclicBarrier.await();
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        condition.await();
        condition.signal();
    }

    @Test
    public void semaphoreTest() {
        Semaphore semaphore = new Semaphore(1);
        Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "：获取信号量");
            System.out.println(Thread.currentThread().getName() + "：释放信号量");
            semaphore.release();
        });
        Thread t2 = new Thread(() -> {
            try {
                semaphore.acquire();
            }
            catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "：获取信号量");
            System.out.println(Thread.currentThread().getName() + "：释放信号量");
            semaphore.release();
        });
        t1.start();
        t2.start();
    }

    @Test
    public void forkJoinTest() throws InterruptedException {
        Thread thread = new Thread(() -> {
            boolean interrupted = Thread.interrupted();
            if (interrupted) {
                System.out.println("clear resource");
                System.out.println(Thread.interrupted());
                return;
            }
            System.out.println("1122121");
        });
        thread.start();

        thread.interrupt();
        Thread.sleep(2000);
    }

    class Counter {
        int num;

        public Counter(int num) {
            this.num = num;
        }
    }

    class Task implements Runnable {
        private Lock lock;
        private Condition condition;
        private Counter counter;
        private int divide;
        private int index;
        private int total;

        public Task(Counter counter, Lock lock, Condition condition, int divide, int index, int total) {
            this.counter = counter;
            this.lock = lock;
            this.condition = condition;
            this.divide = divide;
            this.index = index;
            this.total = total;
        }

        @Override
        public void run() {
            while (counter.num < total) {
                lock.lock();
                try {
                    if (counter.num % divide == index) {
                        System.out.println(Thread.currentThread().getName() + "：第" + ++counter.num + "次");
                    }
                    try {
                        condition.signalAll();
                        condition.await();
                    }
                    catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                finally {
                    lock.unlock();
                }
            }
        }
    }

    class RTask extends RecursiveTask<Long> {

        int size = 100;
        private long[] arr;
        private int start;
        private int end;

        public RTask(long[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start < size) {
                long res = 0;
                try {
                    Thread.sleep(3);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return res;
            }
            int mid = (start + end) / 2;
            RTask left = new RTask(arr, start, mid);
            RTask right = new RTask(arr, mid, end);
            return left.fork().join() + right.fork().join();
        }
    }

}
