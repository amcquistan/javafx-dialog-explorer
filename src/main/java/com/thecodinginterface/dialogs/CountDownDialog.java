
package com.thecodinginterface.dialogs;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

class CountDownDialog extends Dialog<Integer> {
    private BorderPane root;
    private int startFrom;
    private IntegerProperty current;
    private Timer timer;

    private static final String PAUSE_TXT = "Pause";
    private static final String START_TXT = "Start";


    CountDownDialog(Stage primaryStage, int startFrom, String header, boolean decorated) {
        super();
        initOwner(primaryStage);
        if (!decorated) {
            initStyle(StageStyle.UNDECORATED);
        }
        
        this.startFrom = startFrom;
        current = new SimpleIntegerProperty(startFrom);

        root = new BorderPane();
        root.setTop(new Label(header));

        var countDownLbl = new Label();
        countDownLbl.textProperty().bind(current.asString());
        countDownLbl.setFont(Font.font("Cambria", FontWeight.BOLD, 60));
        var vbox = new VBox(countDownLbl);
        vbox.setAlignment(Pos.CENTER);
        root.setCenter(vbox);

        getDialogPane().setContent(root);

        var startStopBtnType = new ButtonType(PAUSE_TXT, ButtonBar.ButtonData.APPLY);
        var resetBtnType = new ButtonType("Reset", ButtonBar.ButtonData.BACK_PREVIOUS);
        var closeBtnType = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);

        getDialogPane().getButtonTypes().addAll(startStopBtnType, resetBtnType, closeBtnType);

        var startStopBtn = (Button) getDialogPane().lookupButton(startStopBtnType);
        var resetBtn = (Button) getDialogPane().lookupButton(resetBtnType);
        
        startStopBtn.addEventFilter(ActionEvent.ACTION, (evt) -> {
            evt.consume();
            var startCounting = timer == null;
            if (startCounting) {
              startStopBtn.setText(PAUSE_TXT);
              startTimer();
            } else {
              startStopBtn.setText(START_TXT);
              stopTimer();
            }
        });

        resetBtn.addEventFilter(ActionEvent.ACTION, (evt) -> {
            evt.consume();
            resetTimer();
            startStopBtn.setText(START_TXT);
        });

        setResultConverter((btnType) -> {
            return Integer.valueOf(current.get());
        });

        startTimer();
    }

    private void startTimer() {
        if (timer == null) {
            timer = new Timer("Custom Dialog Countdown", true);
            timer.schedule(new CountDownTask(), 1000, 1000);
        }
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void resetTimer() {
        current.set(startFrom);
        if (timer != null) {
            stopTimer();
        }
    }

    private final class CountDownTask extends TimerTask {
        @Override
        public void run() {
            Platform.runLater(() -> {
                int currentCnt = current.intValue();
                if (currentCnt > 0) {
                    current.set(currentCnt - 1);
                }
            });
        }
    }
}

