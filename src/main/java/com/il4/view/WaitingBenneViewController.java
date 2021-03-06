package com.il4.view;

import com.il4.tool.listener.IWaitingBenneListener;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.ArrayList;

/**
 * Created by Argon on 02.04.17.
 */
public class WaitingBenneViewController implements IWaitingBenneListener{

    private ListView<String> listView;

    public WaitingBenneViewController(ListView<String> listView){
        this.listView = listView;
    }

    @Override
    public void onBenneGiven(String benneName) {
       // listView.getItems().add(benneName);
    }

    @Override
    public void onBenneTaken(String benneName) {
        //listView.getItems().remove(benneName);
    }

    @Override
    public void onWaitingBennesChange(ArrayList<String> bennes) {
        listView.setItems(FXCollections.observableList(bennes));
    }
}
