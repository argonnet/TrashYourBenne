package com.il4.acteur;

import com.il4.WaitingBenne;
import javafx.application.Platform;

import java.util.ArrayList;

/**
 * Created by Argon on 31.03.17.
 */
public class Transporteur extends Acteur{

    public ArrayList<ITransporteurListener> listeners;

    private void takeBenne(String benneName){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onTakeBenne(benneName);
            });
        });
    }

    private void goToOuvrier(){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onGoOuvrier();
            });
        });
    }

    private void goToBucheron(){

        listeners.forEach( (listener) -> {
            Platform.runLater(() -> {
                listener.onGoBucheron();
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

    public WaitingBenne waitingBenneFromBucheron;
    public WaitingBenne bucheronWaitingBenne;
    public WaitingBenne waitingBenneFromOuvrier;
    public WaitingBenne ouvrierWaitingBenne;


    private final static int TRAJET_DISTANCE = 50;

    public Transporteur(String name,
                        WaitingBenne waitingBenneFromBucheron,
                        WaitingBenne bucheronWaitingBenne,
                        WaitingBenne waitingBenneFromOuvrier,
                        WaitingBenne ouvrierWaitingBenne){
        super(name);

        this.listeners = new ArrayList<>();

        this.waitingBenneFromBucheron = waitingBenneFromBucheron;
        this.bucheronWaitingBenne = bucheronWaitingBenne;
        this.waitingBenneFromOuvrier = waitingBenneFromOuvrier;
        this.ouvrierWaitingBenne = ouvrierWaitingBenne;
    }

    @Override
    public void run(){

        while(filledBenCount < benToFill) {


            this.setBenne(this.waitingBenneFromBucheron.TakeBenne());

            takeBenne(this.getBenne().name);
            System.out.println(this.name + "-> Prise en charge de la benne :  " + this.getBenne().name + " à destination de l'ouvrier");


            for(int i = 0; i <= TRAJET_DISTANCE; i++){
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                goToOuvrier();
                System.out.println(this.name + "-> Acheminement de la benne : " + this.getBenne().name + " - etat : " +
                        i + " / " + TRAJET_DISTANCE);
            }



            this.ouvrierWaitingBenne.GiveBenne(this.getBenne());
            giveBenne(this.getBenne().name);

            this.setBenne(this.waitingBenneFromOuvrier.TakeBenne());

            takeBenne(this.getBenne().name);
            System.out.println(this.name + "-> Prise en charge de la benne :  " + this.getBenne().name + " à destination du bucheron");


            for(int i = 0; i <= TRAJET_DISTANCE; i++){
                try {
                    sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                goToBucheron();
                System.out.println(this.name + "-> Acheminement de la benne : " + this.getBenne().name + " - etat : " +
                        i + " / " + TRAJET_DISTANCE);
            }


            this.bucheronWaitingBenne.GiveBenne(this.getBenne());
            giveBenne(this.getBenne().name);

            this.setBenne(null);

        }
    }

}