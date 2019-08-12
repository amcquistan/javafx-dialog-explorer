# How To Build JavaFX Dialogs and Alerts

## Introduction

In this How To article I demonstrate some of the different ways that JavaFX dialogs and alerts are built and controlled. As a learning and exploration aid the code samples in this article have been integrated into a larger JavaFX application, JavaFX Dialog Explorer, that demonstrates usability of the various dialogs and alerts discussed as well as sources the various parts of this article's code snippets along with the official [JavaFX 11 documentation](https://openjfx.io/javadoc/11/) using the WebView component.

On The Coding Interace, "How To" articles are intended to be more of a collection of code samples relying on code to convey the majority of the semantics of a topic with textual descriptions being on the lighter side. 

How To Contents

## Running the JavaFX Dialog Explorer App

To run the JavaFX Dialog Explorer app you will need to have Java JDK 11 or higher installed and the JAVA_HOME variable set. See [Installing OpenJDK 11](https://thecodinginterface.com/blog/intro-to-java-for-devs/#install-openjdk) from my earlier article "High Level Introduction to Java for Developers" for instructions if needed. After than simply clone the GitHub repo and run with gradle as follows. 

```
git clone https://github.com/amcquistan/javafx-dialog-explorer.git
cd javafx-dialog-explorer
```

_Linux / Mac_

```
./gradlew run
```

_Windows_

```
gradlew.bat run
```

Then click on one of the Alert / Dialog options from the left side bar and the associated dialog will appear. Additionally, in the main area of the UI the corresponding section of this blog post will be shown. You can also toggle back and forth between the blog post and the offical JavaFX docs by clicking the to Logo'd buttons on the top right of the UI.

## The Alert class

The javafx.scene.control.Alert class is an abstraction of the javafx.scene.control.Dialog class and serves as a convience class for a set of common use cases seen in a JavaFX application. That being said working with the Alert class is incredibly easy and is generally using in the following way:

1) instantiate a Alert class passing it the type of alert you desire from the Alert.AlertType set of enums CONFIRMATION, ERROR, INFORMATION, NONE, and WARNING

```
var alert = new Alert(Alert.AlertType.NONE);
```

2) set informational message characteristics to be displayed to the user

```
alert.setTitle("I'm an alert title");
alert.setHeaderText("I'm an alert header");
alert.setContentText("I'm the main alert context (body)");
```

3) show the alert window and watch for a response indicating the action the user took with the alert window

```
// showAndWait is a blocking call so, the code execution path pauses in 
// the executing JavaFX thread until the user interacts with the dialog
// in a way that causes it to close
Optional<ButtonType> result = alert.showAndWait();
result.ifPresent(btnType -> {
    var msg = String.format(
    		"You clicked %s in Alert %s",
    		btnType.getButtonData(),
    		BLOCKING_NONE_ALERT
    );
    feedbackLbl.setText(msg);
});
```

In this example I have specified the AlertType of NONE which is the least convenient way of using the Alert class because now I have to add javafx.scene.control.ButtonType values manually. For this example I do so by adding the following before the alert.showAndWait() method call. Normally this is unnecessary when using the Alert class because it abstracts away the need to specify buttons for the other common AlertType enums.

```
alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
```

#### Informational Alert

A common use case is to simply display an informational message to a user when some event happens which requires nothing more than an acknowledgement from the user. In JavaFX this is crazy simple. You can accomplish this by passing the enum constant AlertType.INFORMATION to the constructor of the Alert class and giving some textual messages for display. This results in a dialog being shown that dislays an info graphic and an OK button.

```
var alert = new Alert(AlertType.INFORMATION);
alert.setTitle(BLOCKING_INFO_ALERT);
alert.setHeaderText(BLOCKING_INFO_ALERT);
alert.setContentText(BLOCKING_INFO_ALERT);

alert.showAndWait().ifPresent((btnType) -> {
    feedbackLbl.setText("Thats all from " + BLOCKING_INFO_ALERT);
    clearDialogOptionSelections();
});
``` 

#### Warning Alert

A similar warning alert is available also which only differs in that a warn info graphic is shown.

```
var alert = new Alert(AlertType.WARNING);
alert.setTitle(BLOCKING_WARNING_ALERT);
alert.setHeaderText(BLOCKING_WARNING_ALERT);
alert.setContentText(BLOCKING_WARNING_ALERT);
alert.showAndWait().ifPresent((btnType) -> {
    feedbackLbl.setText("Thats all from " + BLOCKING_WARNING_ALERT);
    clearDialogOptionSelections();
});
```

#### Confirmation Alert

Displaying a message to the user that requires them to either confirm or deny is another common use case for a JavaFX application. This is easily accomplished via the AlertType.CONFIRMATION enum which displays a question info graphic along with OK and Cancel buttons similar to what I showed with the NONE example at first. You can check to see what the user did by checking the ButtonType from the Optional that is returned from calling Dialog#showAndWait (the showAndWait method is inherited from the Dialog parent class).

```
var alert = new Alert(AlertType.CONFIRMATION);
alert.setTitle(BLOCKING_CONFIRM_ALERT);
alert.setHeaderText(BLOCKING_CONFIRM_ALERT);
alert.setContentText(BLOCKING_CONFIRM_ALERT);
alert.showAndWait().ifPresent((btnType) -> {
  if (btnType == ButtonType.OK) {
    feedbackLbl.setText("Confirmed " + BLOCKING_CONFIRM_ALERT);
  } else if (btnType == ButtonType.CANCEL) {
    feedbackLbl.setText("Cancelled " + BLOCKING_CONFIRM_ALERT);
  }
  clearDialogOptionSelections();
});
```

#### Error Alert

Again, very similar to the INFORMATION and WARN AlertType enums you can use the ERROR enum to display an error info graphic containing alert.

```
var alert = new Alert(AlertType.ERROR);
alert.setTitle(BLOCKING_ERR_ALERT);
alert.setHeaderText(BLOCKING_ERR_ALERT);
alert.setContentText(BLOCKING_ERR_ALERT);
alert.showAndWait().ifPresent((btnType) -> {
    if (btnType == ButtonType.OK) {
      feedbackLbl.setText("Thats all from " + BLOCKING_ERR_ALERT);
    }
    clearDialogOptionSelections();
});
```

#### Error Alert (Non-Blocking)

Up to this point I have been using the Dialog#showAndWait method to display the alert dialog making the path of code execution pause until the dialog is closed.  This is the most common way I have come to use dialogs but, the JavaFX framework provides another method, Dialog#show, which shows the dialog without blocking the executing code path.  In this example I have chosen to add an event handler to hook into the dialog window closing event to know when the user closed the dialog window.

```
var alert = new Alert(AlertType.ERROR);
alert.setTitle(NONBLOCKING_ERR_ALERT);
alert.setHeaderText(NONBLOCKING_ERR_ALERT);
alert.setContentText(NONBLOCKING_ERR_ALERT);

// Alert#show contrasts Alert#showAndWait where Alert#show is non-blocking 
// meaning that code within the execution path that contains the Alert 
// instance will continue to execute after Alert#show is called.
alert.show();

// the code below the show() method call will get exectued immediately
// after show() is called rather than wait on a user to close the dialog
alert.setOnHiding((evt) -> {
    feedbackLbl.setText("Thats all from " + NONBLOCKING_ERR_ALERT);
    clearDialogOptionSelections();
});

feedbackLbl.setText(NONBLOCKING_ERR_ALERT + 
    " execution path kept running after the dialog was displayed");
``` 

#### Error Alert (Non-Modal)

The default behavior of dialogs are for them to be modal which disallows the user from interacting with the window(s) behind the dialog. This is easily overriden via the Dialog#initModality(Modality) method. For example, to make a non-modal dialog pass javafx.stage.Modality.NONE as the argument.

```
var alert = new Alert(AlertType.ERROR);
alert.setTitle(NONMODAL_ERR_ALERT);
alert.setHeaderText(NONMODAL_ERR_ALERT);
alert.setContentText(NONMODAL_ERR_ALERT);

// Alerts exhibit Modality.APPLICATION_MODAL by default but,
// you can specify WINDOW_MODAL to block the window or NONE
// to be non-modal at all.
alert.initModality(Modality.NONE);

// you can also hook into when Dialog's are initially shown using the
// inherited Dialog#setOnShowing event handler
alert.setOnShowing((evt) -> {
    feedbackLbl.setText("Go ahead, click another option. I won't stop you.");
});

alert.showAndWait().ifPresent((btnType) -> {
    if (btnType == ButtonType.OK) {
        feedbackLbl.setText("Thats all from " + NONMODAL_ERR_ALERT);
    }
    clearDialogOptionSelections();
});
```

#### Alert (No Header)

You can omit title, header or body content like so.

```
var alert = new Alert(AlertType.INFORMATION);
alert.setTitle(ALERT_NO_HEADER);
// null as a value will cause the section to not be displayed
alert.setHeaderText(null);
alert.setContentText(ALERT_NO_HEADER);

alert.showAndWait().ifPresent((btnType) -> {
    if (btnType == ButtonType.OK) {
        feedbackLbl.setText("Thats all from " + ALERT_NO_HEADER);
    }
    clearDialogOptionSelections();
});
```

## Convience Input Dialogs

The JavaFX framework also provides two convience implementation classes of Dialogs that accept textual as well as dropdown like selection inputs.

#### Text Input Dialog

To easily accept text input via a dialog use the javafx.scene.control.TextInputDialog class. This class differs in that a Optional<String> is returned if the user clicks Ok. In this example I as the user to enter the section name of another dialog from this article which I then display as the next dialog. If the user enters text that doesn't match the title of one of the sections then I inform them with a warning dialog that no match as found.

```
// Two constructors: TextInputDialog() and TextInputDialog(String)
// where the String is the default value
var txtDlg = new TextInputDialog(TEXT_DIALOG);
txtDlg.setTitle(TEXT_DIALOG);
txtDlg.setHeaderText(TEXT_DIALOG);
txtDlg.setContentText("Enter name of another dialog to open.");

// returns String optional
Optional<String> result = txtDlg.showAndWait();

// true => String (ie, user entered value and clicked ok)
// false => user clicked cancel
result.ifPresent(input -> {

    // find the VBox child node that is a DialogOption
    // and matches the user's input
    Optional<Node> matchedDlg = menuVBox.getChildren().stream()
        .filter(node -> {
            return !input.equalsIgnoreCase(txtDlg.getDefaultValue())
              && ((DialogOption) node).getText().equalsIgnoreCase(input);
        }).findFirst();

    if (matchedDlg.isPresent()) {
        // if present fire the action event of the underlying ToggleButton
        var dlgOption = (DialogOption) matchedDlg.get();
        dlgOption.fire();
        dlgOption.setSelected(true);
    } else {
        // otherwise let the user know they didn't type in something
        // that matched on of the titles of the available OptionDialog(s)
        dialogsToggleGrp.selectToggle(null);
        var notFoundAlert = new Alert(AlertType.WARNING);
        notFoundAlert.setTitle("Input Not Recognized");
        notFoundAlert.setHeaderText(null);
        notFoundAlert.setContentText(String.format("%s does not match a dialog from the menu", input));
        notFoundAlert.show();
    }
});
```

#### Choice Box Dialog

To limit input options a [javafx.scene.control.ChoiceDialog](https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/ChoiceDialog.html) can be used. To do this I supply a predifined list of String values for the user to select from along with a default value to the ChoiceDialog constructor. In this example I display all other dialog sections from this article for the user to choose from then display that dialog.

```
var titles = webResourceMap.keySet().stream()
    .filter(dlgOption -> !dlgOption.equals(CHOICE_DIALOG))
    .collect(Collectors.toList());

var defaultTitle = titles.get(0);
var choiceDlg = new ChoiceDialog<String>(defaultTitle, titles);

// returns Optional<String> on clicking ok
// or false if cancel is clicked
choiceDlg.showAndWait().ifPresent(selection -> {
  Optional<Node> matchedDlg = menuVBox.getChildren().stream()
      .filter(node -> ((DialogOption) node).getText().equals(selection))
      .findFirst();

  matchedDlg.ifPresent(node -> {
      var dlgOption = (DialogOption) matchedDlg.get();
      dlgOption.fire();
      dlgOption.setSelected(true);
  });
});
```

## The Dialog Class

Of course you can still implement your own version of a [javafx.scene.control.Dialog](https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Dialog.html#initModality(javafx.stage.Modality)) and have complete control of what is displayed and now things are handled.

#### Custom Dialog

In this example I have implemented a class named CountDownDialog which displays a dialog that counts down to zero allowing the user to start and stop (pause) the countdown as well as reset and cancel.  This interactivity is provided by implementing javafx.scene.control.ButtonType instances with meaningful text to display to the user and paired with a javafx.scene.control.ButtonBar.ButtonData enum that fits the semantics of the action being handled. Then the ButtonType instances are added to the ObservableList of ButtonType instances via DialogPane#getButtonTypes of the DialogPane which is part of the Dialog class.

An important distinction to make is that the ButtonType instances being added to the Dialog are different from the Button control. However, you can gain access to the actual Button instances that get added to the Dialog via DialogPane#lookupButton(ButtonType) method which I demonstrate below in order to disable buttons and add action event handlers. 

The other thing worth mentioning is that the Dialog class is generically typed with regards to the Object that is returned when the Dialog is closed. In this example I have implemented the CountDownDialog class to return an Integer value represeting the countdown value the dialog was closed at. 

```
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
```

## Conclusion

In this How To article I have demonstrated a number of ways that I have learned to display dialogs in JavaFX applications. To aid in the discussion I wrote a simple learning aid app named JavaFX Dialog Explorer that was a fun little experiment in which I hope gives a sufficient number of working examples along with an easy way to find useful information on dialog API details.