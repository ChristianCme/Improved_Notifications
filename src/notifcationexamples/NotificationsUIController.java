/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notifcationexamples;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import taskers.*;
import static taskers.ThreadState.*;

/**
 * FXML Controller class
 *
 * @author dalemusser
 */
public class NotificationsUIController implements Initializable, Notifiable {

    @FXML
    private TextArea textArea;
    
    @FXML
    private Button button1;
    
    @FXML
    private Button button2;
    
    @FXML
    private Button button3;

    
    private Task1 task1;
    private Task2 task2;
    private Task3 task3;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    public void start(Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                if (task1 != null) task1.end();
                if (task2 != null) task2.end();
                if (task3 != null) task3.end();
            }
        });
    }
    
    @FXML
    public void startTask1(ActionEvent event) {
        System.out.println("start task 1");
        if (task1 == null) {
            task1 = new Task1(2147483647, 1000000);
            task1.setNotificationTarget(this);
            task1.start();
        }
        else {
            task1.end();
            task1 = null;
            button1.setText("Start Thread 1");
        }
    }
    
    @Override
    public void notify(String message) {
        if (message.equals("Task1 done.")) {
            task1 = null;
        }
        textArea.appendText(message + "\n");
        if (checkState(task1.getThreadState())) {
            button1.setText("End Thread 1");
        }
        else {
            button1.setText("Start Thread 1");
        }

    }
    
    @FXML
    public void startTask2(ActionEvent event) {
        System.out.println("start task 2");
        if (task2 == null) {
            task2 = new Task2(2147483647, 1000000);
            task2.setOnNotification((String message) -> {
                textArea.appendText(message + "\n");
                if (checkState(task2.getThreadState())) {
                     button2.setText("End Thread 2");
                }
                else {
                    button2.setText("Start Thread 2");
                }
            });
            
            task2.start();
        }
        else {
            task2.end();
            task2 = null;
            button1.setText("Start Thread 2");
        }
    }
    
    @FXML
    public void startTask3(ActionEvent event) {
        System.out.println("start task 3");
        if (task3 == null) {
            task3 = new Task3(2147483647, 1000000);
            // this uses a property change listener to get messages
            task3.addPropertyChangeListener((PropertyChangeEvent evt) -> {
                textArea.appendText((String)evt.getNewValue() + "\n");
                if (checkState(task3.getThreadState())) {
                     button3.setText("End Thread 3");
                }
                else {
                    button3.setText("Start Thread 3");
                }
            });
            
            task3.start();
        }
        else {
            task3.end();
            task3 = null;
            button1.setText("Start Thread 1");
        }
    }
    
    private boolean checkState(ThreadState state) {
        if(state.equals(STARTED)) {
            return true;
        }
        else {
            return false;
        }
        
    }
}
