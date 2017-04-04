package com.il4.view.component;

import com.il4.acteur.ITransporteurListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

/**
 * Created by Argon on 02.04.17.
 */
public class TransporteurView extends AnchorPane implements ITransporteurListener {

    @FXML private Slider slider;
    @FXML public Label labelName;
    @FXML public Label labelBenne;

    public TransporteurView(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("transporteurView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


    public  void setName(String name){
        labelName.setText(name);
    }

    public void setCurrentBenne(String name){
        labelBenne.setText(name);
    }

    public void resetCurrentBenne(){
        labelBenne.setText("---");
    }

    public void progressDirectionOuvrier(){
        slider.increment();
    }

    public void progressDirectionBucheron(){
        slider.decrement();
    }


    @Override
    public void onTakeBenne(String benneName) {
        setCurrentBenne(benneName);
    }

    @Override
    public void onGoOuvrier() {
        progressDirectionOuvrier();
    }

    @Override
    public void onGoBucheron() {
        progressDirectionBucheron();
    }

    @Override
    public void onGiveBenne(String benneName) {
        resetCurrentBenne();
    }
}