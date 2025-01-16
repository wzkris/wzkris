package com.wzkris.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author : wzkris
 * @version : V1.0.0
 * @description : 并发测试
 * @date : 2023/9/21 16:13
 */
@DisplayName("并发测试")
public class ConcurrentTest {

    final static Integer max = 1000;

    final static Object obj = new Object();

    static Integer num = 0;

    static Lock lock = new ReentrantLock();

    static Condition condition = lock.newCondition();

    @Test
    @DisplayName("线程交替打印测试")
    public void simpleTest() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            synchronized (obj) {
                while (num < max) {
                    System.out.println(Thread.currentThread().getName() + num);
                    num++;
                    obj.notify();
                    try {
                        if (num < max) {
                            obj.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (obj) {
                while (num < max) {
                    System.out.println(Thread.currentThread().getName() + num);
                    num++;
                    obj.notify();
                    try {
                        if (num < max) {
                            obj.wait();
                        }
                    } catch (InterruptedException e) {
                    }
                }
            }
        });
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

    public void conditionWait() throws InterruptedException {
        lock.lock();
        try {
            System.out.println("await");
            condition.await();
            System.out.println("wake up");
        } finally {
            lock.unlock();
            System.out.println("await unlock");
        }
    }

    public void conditionSignal() throws InterruptedException {
        lock.lock();
        try {
            condition.signal();
            System.out.println("signal");
        } finally {
            lock.unlock();
            System.out.println("signal unlock");
        }
    }

    @Test
    @DisplayName("future-Test")
    public void futureTest() throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName());
            return "111";
        });
        futureTask.run();
        String s = futureTask.get();
        System.out.println(s);
    }

    @Test
    @DisplayName("conditionTest")
    public void conditionTest() throws InterruptedException {
        new Thread(() -> {
            try {
                conditionWait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(() -> {
            try {
                conditionSignal();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Test
    @DisplayName("cyclicBarrier-Test, 线程屏障测试")
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
    @DisplayName("semaphore-Test, 并发流量控制测试")
    public void semaphoreTest() {
        Semaphore semaphore = new Semaphore(1);
        Thread t1 = new Thread(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "：获取信号量");
            System.out.println(Thread.currentThread().getName() + "：释放信号量");
            semaphore.release();
        });
        Thread t2 = new Thread(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
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
    @DisplayName("exchanger-Test, 线程间交换数据测试")
    public void exchangeTest() throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> {
            String b = "bbb";
            try {
                String exchange = exchanger.exchange(b);
                System.out.println(Thread.currentThread().getName() + ":" + b);
                System.out.println("exchange000:" + exchange);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();

        String a = "aaa";
        String exchange = exchanger.exchange(a);
        System.out.println(Thread.currentThread().getName() + ":" + a);
        System.out.println("exchange111:" + exchange);
    }

    @Test
    @DisplayName("forkJoin-Test, 分治计算测试")
    public void forkJoinTest() throws InterruptedException, ExecutionException {
        RTask rTask = new RTask(50, 1, 100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(rTask);
        Integer res = forkJoinTask.get();
        System.out.println(res);
    }

    static class RTask extends RecursiveTask<Integer> {

        private final int threshold;// 阈值

        private final int start;

        private final int end;

        public RTask(int threshold, int start, int end) {
            this.threshold = threshold;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            int sum = 0;
            if (end - start <= threshold) {
                for (int i = start; i <= end; i++) {
                    sum += i;
                }
            } else {
                System.out.println(Thread.currentThread().getName());
                // 如果任务大于阈值，就分裂成两个子任务计算
                int middle = (start + end) / 2;
                RTask leftTask = new RTask(threshold, start, middle);
                RTask rightTask = new RTask(threshold, middle + 1, end);
                // 执行子任务
                leftTask.fork();
                rightTask.fork();
                // 等待子任务执行完，并得到其结果
                int leftResult = leftTask.join();
                int rightResult = rightTask.join();
                // 合并子任务
                sum = leftResult + rightResult;
            }
            return sum;
        }
    }

}
