package com.thingslink.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 锁测试
 * @date : 2023/9/21 16:13
 */
@DisplayName("锁测试")
public class LockTest {

    @Test
    @DisplayName("取余测试")
    public void test() {
        System.out.println(8 % 5);
    }

    @Test
    @DisplayName("线程交替打印测试")
    public void threadPrint() {
        System.out.println("898604F2102382153500".substring("898604F2102382153500".length() - 16));
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
