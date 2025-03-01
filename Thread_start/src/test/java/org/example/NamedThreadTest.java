package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Tests {
    @Test
    void namedThreadsTest() {
        Thread t1 = new Thread(String.valueOf(1)){
            public void run(){
                System.out.printf("%s started... \n", Thread.currentThread().getName());
                try{
                    Thread.sleep(3000);
                }
                catch(InterruptedException e){
                    System.out.println("Thread has been interrupted");
                }
                System.out.printf("%s finished... \n", Thread.currentThread().getName());
            }};
        Thread t2 = new Thread(String.valueOf(2)){
            public void run(){
                System.out.printf("%s started... \n", Thread.currentThread().getName());
                try{
                    Thread.sleep(4000);
                }
                catch(InterruptedException e){
                    System.out.println("Thread has been interrupted");
                }
                System.out.printf("%s finished... \n", Thread.currentThread().getName());
            }};
        t1.start();
        t2.start();
        try{
            t1.join();
            t2.join();
        }
        catch(InterruptedException e){
            System.out.print("One of them has been interrupted");
        }
    }
}