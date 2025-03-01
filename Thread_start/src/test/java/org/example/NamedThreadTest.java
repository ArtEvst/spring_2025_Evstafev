package org.example;

import org.junit.jupiter.api.Test;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Function;

class Tests {
    static final long millisToWait = 2000L;
    /**
     * Простой запуск потоков.
     * Average timespan is: 461 μs
     * Просто запускает
     */
    @Test
    void simpleStartThreadsTest() {
        Thread t1 = getSomeThread(Tests::makeTalkativeThread, 1);
        Thread t2 = getSomeThread(Tests::makeTalkativeThread, 2);

        long startNanos = System.nanoTime();

        startThreads(t1, t2);
        joinThreads(t1, t2);

        long finishNanos = System.nanoTime();
        System.out.println("Timespan is " + (finishNanos - startNanos - millisToWait * 1_000_000L) / 1000L + " μs");
    }

    @Test
    void simpleStartThreadsAverageTimespanTest() {
        Thread t1, t2;
        long startNanos, finishNanos;

        long AverageTimespan = 0L;

        for (int i = 0; i < 100; i++) {
            t1 = getSomeThread(Tests::makeSilentThread, 1);
            t2 = getSomeThread(Tests::makeSilentThread, 2);

            startNanos = System.nanoTime();

            startThreads(t1, t2);
            joinThreads(t1, t2);

            finishNanos = System.nanoTime();
            AverageTimespan += (finishNanos - startNanos - millisToWait * 1_000_000L) / 1000L;
        }
        System.out.println("Average timespan is: " + AverageTimespan / 100L + " μs");
    }

    /**
     * Одноврменный запуск потоков с помощью CountDownLatch.
     * Average timespan is: 235 μs
     * Сначала main ждет пока все не будут готовы с помощью ready, а потом
     * даёт отмашку с помощью start, на котором ждали t1 и t2.
     * По отмашке запускаются t1 и t2.
     */
    @Test
    void simultaneouslyStartThreadsCDLTest() throws InterruptedException {
        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);

        Thread t1 = makeTalkativeThreadCDL(1, ready, start);
        Thread t2 = makeTalkativeThreadCDL(2, ready, start);

        startThreads(t1, t2);
        ready.await();

        long startNanos = System.nanoTime();

        start.countDown();
        joinThreads(t1, t2);

        long finishNanos = System.nanoTime();
        System.out.println("Timespan is " + (finishNanos - startNanos - millisToWait * 1_000_000L) / 1000L + " μs");
    }

    @Test
    void simultaneouslyStartThreadsCDLAverageTimespanTest() throws InterruptedException {
        CountDownLatch ready, start;
        Thread t1, t2;
        long startNanos, finishNanos;

        long AverageTimespan = 0L;

        for (int i = 0; i < 100; i++) {
            ready = new CountDownLatch(2);
            start = new CountDownLatch(1);

            t1 = makeSilentThreadCDL(1, ready, start);
            t2 = makeSilentThreadCDL(2, ready, start);

            startThreads(t1, t2);
            ready.await();

            startNanos = System.nanoTime();

            start.countDown();
            joinThreads(t1, t2);

            finishNanos = System.nanoTime();
            AverageTimespan += (finishNanos - startNanos - millisToWait * 1_000_000L) / 1000L;
        }

        System.out.println("Average timespan is: " + AverageTimespan / 100L + " μs");
    }

    /**
     * Одноврменный запуск потоков с помощью CyclicBarrier.
     * Average timespan is: 307 μs
     * Все ждут пока, все не дойдут до gate.
     * По отмашке запускаются main, t1 и t2.
     */
    @Test
    void simultaneouslyStartThreadsCBTest() throws InterruptedException, BrokenBarrierException {
        CyclicBarrier gate = new CyclicBarrier(3);

        Thread t1 = makeTalkativeThreadCB(1, gate);
        Thread t2 = makeTalkativeThreadCB(2, gate);

        startThreads(t1, t2);

        long startNanos = System.nanoTime();

        gate.await();
        joinThreads(t1, t2);

        long finishNanos = System.nanoTime();
        System.out.println("Timespan is " + (finishNanos - startNanos - millisToWait * 1_000_000L) / 1000L + " μs");
    }

    @Test
    void simultaneouslyStartThreadsCBAverageTimespanTest() throws InterruptedException, BrokenBarrierException {
        CyclicBarrier gate;
        Thread t1, t2;
        long startNanos, finishNanos;

        long AverageTimespan = 0L;

        for (int i = 0; i < 100; i++) {
            gate = new CyclicBarrier(3);

            t1 = makeSilentThreadCB(1, gate);
            t2 = makeSilentThreadCB(2, gate);

            startThreads(t1, t2);

            startNanos = System.nanoTime();

            gate.await();
            joinThreads(t1, t2);

            finishNanos = System.nanoTime();
            AverageTimespan += (finishNanos - startNanos - millisToWait * 1_000_000L) / 1000L;
        }

        System.out.println("Average timespan is: " + AverageTimespan / 100L + " μs");
    }

    private static void joinThreads(Thread t1, Thread t2) {
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            System.out.print("Thread has been interrupted");
        }
    }

    private static void startThreads(Thread t1, Thread t2) {
        t1.start();
        t2.start();
    }

    private static Thread getSomeThread(Function<Integer, Thread> function, Integer number) {
        return function.apply(number);
    }

    private static Thread makeTalkativeThreadCDL(int i, CountDownLatch ready, CountDownLatch start) {
        return new Thread(String.valueOf(i)) {
            public void run() {
                ready.countDown();
                try {
                    start.await();
                    System.out.printf("%s started... \n", Thread.currentThread().getName());
                    Thread.sleep(millisToWait);
                } catch (InterruptedException e) {
                    System.out.println("Thread has been interrupted");
                } finally {
                    System.out.printf("%s finished... \n", Thread.currentThread().getName());
                }
            }
        };
    }

    private static Thread makeSilentThreadCDL(int i, CountDownLatch ready, CountDownLatch start) {
        return new Thread(String.valueOf(i)) {
            public void run() {
                ready.countDown();
                try {
                    start.await();
                    Thread.sleep(millisToWait);
                } catch (InterruptedException e) {
                }
            }
        };
    }

    private static Thread makeTalkativeThreadCB(int i, CyclicBarrier gate) {
        return new Thread(String.valueOf(i)) {
            public void run() {
                try {
                    gate.await();
                    System.out.printf("%s started... \n", Thread.currentThread().getName());
                    Thread.sleep(millisToWait);
                } catch (InterruptedException e) {
                    System.out.println("Thread has been interrupted");
                } catch (BrokenBarrierException e) {
                    System.out.println("Barrier has been broken");
                } finally {
                    System.out.printf("%s finished... \n", Thread.currentThread().getName());
                }
            }
        };
    }

    private static Thread makeSilentThreadCB(int i, CyclicBarrier gate) {
        return new Thread(String.valueOf(i)) {
            public void run() {
                try {
                    gate.await();
                    Thread.sleep(millisToWait);
                } catch (InterruptedException e) {
                } catch (BrokenBarrierException e) {
                }
            }
        };
    }

    private static Thread makeTalkativeThread(int i) {
        return new Thread(String.valueOf(i)) {
            public void run() {
                try {
                    System.out.printf("%s started... \n", Thread.currentThread().getName());
                    Thread.sleep(millisToWait);
                } catch (InterruptedException e) {
                    System.out.println("Thread has been interrupted");
                } finally {
                    System.out.printf("%s finished... \n", Thread.currentThread().getName());
                }
            }
        };
    }

    private static Thread makeSilentThread(int i) {
        return new Thread(String.valueOf(i)) {
            public void run() {
                try {
                    Thread.sleep(millisToWait);
                } catch (InterruptedException e) {
                }
            }
        };
    }
}
