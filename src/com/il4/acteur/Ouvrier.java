package com.il4.acteur;

import com.il4.WaitingBenne;
import com.il4.view.MainViewController;
import javafx.application.Platform;

import java.util.ArrayList;

/**
 * Created by Argon on 31.03.17.
 */
public class Ouvrier extends Acteur {

    public ArrayList<IOuvrierListener> listeners;

    public WaitingBenne waitingBenne;
    public WaitingBenne transporteurWaitingBenne;

    private void removeBoisToBenne(){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onRemoveBoisToBenne();
            });
        });
    }


    private void giveBenne(String benneName){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onGiveBenne(benneName);
            });
        });
    }

    private void takeBenne(String benneName){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onTakeBenne(benneName);
            });
        });
    }


    public Ouvrier(String name, MainViewController mainViewController, WaitingBenne transporteurWaitingBenne, WaitingBenne waitingBenne){
        super(name,mainViewController);

        this.listeners = new ArrayList<>();

        this.waitingBenne = waitingBenne;
        this.transporteurWaitingBenne = transporteurWaitingBenne;
    }

    @Override
    public void run(){

        while(filledBenCount < BEN_TO_FILL) {

            this.setBenne(this.waitingBenne.TakeBenne());
            takeBenne(this.getBenne().name);
            System.out.println(this.name + "-> Récupération de la benne :  " + this.getBenne().name);


            while (this.getBenne().remplissage > 0) {
                this.getBenne().remplissage--;
                try {
                    sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this.name + "-> Vidage de la benne : " + this.getBenne().name + " - etat : " +
                        this.getBenne().remplissage + " / " + 0);
                removeBoisToBenne();
            }

            this.transporteurWaitingBenne.GiveBenne(this.getBenne());
            this.setBenne(null);
            giveBenne(this.getBenne().name);

        }
    }
}

