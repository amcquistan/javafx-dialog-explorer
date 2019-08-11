# How To Build JavaFX Dialogs and Alerts

## Introduction

In this How To article I demonstrate some of the different ways that JavaFX dialogs and alerts are built and controlled. As a learning and exploration aid the code samples in this article have been integrated into a larger JavaFX application that demonstrates usability of the various dialogs and alerts discussed as well as sources the various parts of the article's code snippets along with the official JavaFX 11 documentation.

At The Coding Interace "How To" articles are intended to be more of a collection of code samples relying on code to convey the majority of the semantics of a topic with textual descriptions being on the lighter side. 

How To Contents

## Running the JavaFX Dialog Explorer App

To run the JavaFX Dialog Explorer app you will need to have Java JDK 11 or higher installed and the JAVA_HOME variable set. See [Installing OpenJDK 11](https://thecodinginterface.com/blog/intro-to-java-for-devs/#install-openjdk) from my earlier article "High Level Introduction to Java for Developers" for instructions if needed. After than simply clone the GitHub repo and run with gradle as follows. 



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
// showAndWait is a blocking call (you pause in the executing JavaFX thread)
Optional<ButtonType> result = alert.showAndWait();
result.ifPresent(btnType -> {
                    var msg = String.format("You clicked %s in Alert %s", btnType.getButtonData(), BLOCKING_NONE_ALERT);
                    feedbackLbl.setText(msg);
                });
```

Now in this example I have specified the AlertType of NONE which is the least helpful, least convient way of using the Alert class so, I have to add javafx.scene.control.ButtonType values manually. For this example I would do so by added the following before the alert.showAndWait() method call which is really just removing the nice abstractions of the Alert class as you are interacting with the Dialog super class.

```
alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
```



####  
