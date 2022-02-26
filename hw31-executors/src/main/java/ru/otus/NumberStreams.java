package ru.otus;

public class NumberStreams {
    
    private static final int STREAM_LIMIT = 10;
    private volatile int forwardIndex = 1;
    private volatile int backwardIndex = STREAM_LIMIT-1;
    private volatile int printRepeatForward = 0;
    private volatile int printRepeatBackward = 0;
    boolean switchThread = true;

    public static void main(String[] args){
        NumberStreams h = new NumberStreams();
        new Thread(()-> h.action(false),"Северный поток 1").start();
        new Thread(()-> h.action(true),"Северный поток 2").start();
    }

    synchronized void action(boolean switchThread){
        try {
            while (!Thread.currentThread().isInterrupted()){
                while (forwardIndex<=STREAM_LIMIT){
                    while (printRepeatForward++<2 && forwardIndex<=STREAM_LIMIT){
                        while (this.switchThread == switchThread){
                            notifyAll();
                            this.switchThread = !switchThread;
                        }
                        System.out.println(Thread.currentThread().getName() + " : " + forwardIndex);
                        sleep();
                        while (this.switchThread == !switchThread){
                            wait();
                        }
                    }
                    forwardIndex++;
                    printRepeatForward = 0;
                }

                backwardIndex = STREAM_LIMIT -1;

                while (backwardIndex>0){
                    while (printRepeatBackward++<2 && backwardIndex>0){
                        while (this.switchThread == switchThread){
                            notifyAll();
                            this.switchThread = !switchThread;
                        }
                        System.out.println(Thread.currentThread().getName() + " : " + (backwardIndex));
                        sleep();
                        while (this.switchThread == !switchThread){
                            wait();
                        }
                    }
                    backwardIndex--;
                    printRepeatBackward = 0;
                }
                forwardIndex = 2;
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
