package ru.otus;

public class NumberStreams {

    private static final int STREAM_LIMIT = 10;
    private volatile int forwardIndex = 1;
    private volatile int backwardIndex = STREAM_LIMIT-1;
    private volatile int printRepeatForward = 0;
    private volatile int printRepeatBackward = 0;

    public static void main(String[] args) {
        NumberStreams h = new NumberStreams();
        for (int i = 1; i<3; i++){
            new Thread(h::action,"Северный поток " + i).start();
        }
    }

    private synchronized void action(){
        try {
            while (!Thread.currentThread().isInterrupted()){
                while (forwardIndex<=STREAM_LIMIT){
                    while (printRepeatForward++<2 && forwardIndex<=STREAM_LIMIT){
                        System.out.println(Thread.currentThread().getName() + " : " + forwardIndex);
                        sleep();
                        if (printRepeatForward>1){
                            notifyAll();
                        }
                        wait();
                    }
                    forwardIndex++;
                    printRepeatForward = 0;
                    notifyAll();
                }

                backwardIndex = STREAM_LIMIT -1;

                while (backwardIndex>0){
                    while (printRepeatBackward++<2 && backwardIndex>0){
                        System.out.println(Thread.currentThread().getName() + " : " + (backwardIndex));
                        sleep();
                        if (printRepeatBackward>1){
                            notifyAll();
                        }
                        wait();
                    }
                    backwardIndex--;
                    printRepeatBackward = 0;
                    notifyAll();
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
