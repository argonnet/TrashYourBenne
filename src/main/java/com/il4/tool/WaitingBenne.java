package com.il4.tool;

import com.il4.tool.listener.IWaitingBenneListener;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;



/**
 * Created by Argon on 01.04.17.
 */
public class WaitingBenne {

    public enum WaitingMode{
        oneWaiting,
        severalWaiting
    }

    public ArrayList<IWaitingBenneListener> listeners;

    private LinkedList<Benne> waitingBenne;

    private Lock takeOrGiveBenneLock = new ReentrantLock();
    private Condition waitIfNoBenneAvailable = takeOrGiveBenneLock.newCondition();
    private WaitingMode waitingMode = WaitingMode.oneWaiting;
    private boolean isSomeOneWaitingForABenne = false;

    private  void onBenneGivenNotify(String benneName){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onBenneGiven(benneName);
            });
        });
    }

    private void onBenneTakeNotify(String benneName){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onBenneTaken(benneName);
            });
        });
    }


    public boolean IsABenneWaiting() {
        return this.waitingBenne != null && !this.waitingBenne.isEmpty();
    }

    public void GiveBenne(Benne benne){
        takeOrGiveBenneLock.lock();
        try{
            this.waitingBenne.offer(benne);
            onBenneGivenNotify(benne.name);

            waitIfNoBenneAvailable.signalAll();
        }finally {
            takeOrGiveBenneLock.unlock();
        }

    }

    public Benne TakeBenne(){
        takeOrGiveBenneLock.lock();

        try{


            if(waitingMode == WaitingMode.oneWaiting){

                //Seul un thread peut attendre à la fois.
                if(!this.IsABenneWaiting()){ //Si aucune benne n'est là on attend qu'une arrive
                    try {
                        if(!isSomeOneWaitingForABenne){
                            isSomeOneWaitingForABenne = true;
                            waitIfNoBenneAvailable.await();
                        }
                        isSomeOneWaitingForABenne = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }else if(waitingMode == WaitingMode.severalWaiting){

                //Plusieurs thread attende simulatnément (file d'attente)
                while(!this.IsABenneWaiting()){ //Si aucune benne n'est là on attend qu'une arrive
                    try {
                        waitIfNoBenneAvailable.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }


            if(this.IsABenneWaiting()){
                Benne firstBenne = this.waitingBenne.getFirst();
                this.waitingBenne.removeFirst();

                onBenneTakeNotify(firstBenne.name);

                return firstBenne;
            }else{
                return  null;
            }

        }finally {
            takeOrGiveBenneLock.unlock();
        }
    }


    public WaitingBenne(WaitingMode waitingMode){
        this.waitingMode = waitingMode;
        this.waitingBenne = new LinkedList<>();
        this.listeners = new ArrayList<>();
    }

}
