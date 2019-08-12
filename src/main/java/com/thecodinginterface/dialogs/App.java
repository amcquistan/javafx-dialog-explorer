
package com.thecodinginterface.dialogs;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;

public class App extends Application {
    private static final double APP_WIDTH = 1200;
    private static final double APP_HEIGTH = 900;
    public static final double MENU_OPTIONS_WIDTH = 250;

    private final String BLOCKING_NONE_ALERT = "None Alert";
    private final String BLOCKING_INFO_ALERT = "Info Alert";
    private final String BLOCKING_WARNING_ALERT = "Warning Alert";
    private final String BLOCKING_CONFIRM_ALERT = "Confirmation Alert";
    private final String BLOCKING_ERR_ALERT = "Error Alert";
    private final String NONBLOCKING_ERR_ALERT = "Error Alert (Non-Blocking)";
    private final String NONMODAL_ERR_ALERT = "Error Alert (Non-Modal)";
    private final String ALERT_NO_HEADER = "Alert (No Header)";
    private final String TEXT_DIALOG = "Text Input Dialog";
    private final String CHOICE_DIALOG = "Choice Box Dialog";
    private final String CUSTOM_DLG = "Custom Dialog";

    private final Map<String, Pair<String, String>> webResourceMap = new HashMap<>();

    private BorderPane rootPane;
    private VBox menuVBox;
    private WebView webView;
    private Label feedbackLbl;
    private String currentTCIUrl;
    private String currentJavaFXUrl;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        buildWebResouceMap();
        rootPane = new BorderPane();
        
        // heading
        rootPane.setTop(makeHeader());

        // content
        feedbackLbl = new Label("Select a dialog option from the menu on the left");
        feedbackLbl.getStyleClass().add("feedback-label");
        feedbackLbl.setAlignment(Pos.CENTER_LEFT);
        feedbackLbl.prefWidth(APP_WIDTH * 0.6);
        feedbackLbl.setTextAlignment(TextAlignment.LEFT);
        
        var sourceBtns = makeSourceButtons();
        var spacer = new Region();
        var contentSpace = sourceBtns.widthProperty().add(feedbackLbl.widthProperty()).add(MENU_OPTIONS_WIDTH + 60);
        spacer.prefWidthProperty().bind(rootPane.widthProperty().subtract(contentSpace));
        var contentHBox = new HBox(10, feedbackLbl, spacer, sourceBtns);
        contentHBox.setPadding(new Insets(5));
        contentHBox.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(feedbackLbl, Priority.ALWAYS);

        webView = new WebView();
        var contentVBox = new VBox(contentHBox, webView);
        VBox.setVgrow(webView, Priority.ALWAYS);
        
        rootPane.setCenter(contentVBox);

        // dialog menu options
        var menuScrollPane = new ScrollPane();
        menuScrollPane.setFitToHeight(true);
        // menuScrollPane.setPrefWidth(300);

        rootPane.setLeft(menuScrollPane);

        menuVBox = new VBox(5);
        menuVBox.setFillWidth(true);

        menuScrollPane.setContent(menuVBox);
        menuVBox.getStyleClass().add("dialog-options");
        var dialogsToggleGrp = new ToggleGroup();
        menuVBox.getChildren().addAll(
            new DialogOption(BLOCKING_NONE_ALERT, dialogsToggleGrp, () -> {
                updateWebResouceUrls(BLOCKING_NONE_ALERT);
                var alert = new Alert(AlertType.NONE);

                alert.setTitle("I'm an alert title");
                alert.setHeaderText("I'm an alert header");
                alert.setContentText("I'm the main alert context (body)");

                alert.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

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
                    clearDialogOptionSelections();
                });
            }),
            new DialogOption(BLOCKING_INFO_ALERT, dialogsToggleGrp, () -> {
                updateWebResouceUrls(BLOCKING_INFO_ALERT);

                var alert = new Alert(AlertType.INFORMATION);
                alert.setTitle(BLOCKING_INFO_ALERT);
                alert.setHeaderText(BLOCKING_INFO_ALERT);
                alert.setContentText(BLOCKING_INFO_ALERT);

                alert.showAndWait().ifPresent((btnType) -> {
                    feedbackLbl.setText("Thats all from " + BLOCKING_INFO_ALERT);
                    clearDialogOptionSelections();
                });
            }),
            new DialogOption(BLOCKING_WARNING_ALERT, dialogsToggleGrp, () -> {
                updateWebResouceUrls(BLOCKING_WARNING_ALERT);

                var alert = new Alert(AlertType.WARNING);
                alert.setTitle(BLOCKING_WARNING_ALERT);
                alert.setHeaderText(BLOCKING_WARNING_ALERT);
                alert.setContentText(BLOCKING_WARNING_ALERT);
                alert.showAndWait().ifPresent((btnType) -> {
                    feedbackLbl.setText("Thats all from " + BLOCKING_WARNING_ALERT);
                    clearDialogOptionSelections();
                });
            }),
            new DialogOption(BLOCKING_CONFIRM_ALERT, dialogsToggleGrp, () -> {
              updateWebResouceUrls(BLOCKING_CONFIRM_ALERT);

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
          }),
          new DialogOption(BLOCKING_ERR_ALERT, dialogsToggleGrp, () -> {
            updateWebResouceUrls(BLOCKING_ERR_ALERT);

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
          }),
          new DialogOption(NONBLOCKING_ERR_ALERT, dialogsToggleGrp, () -> {
            updateWebResouceUrls(NONBLOCKING_ERR_ALERT);

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
          }),
          new DialogOption(NONMODAL_ERR_ALERT, dialogsToggleGrp, () -> {
            updateWebResouceUrls(NONMODAL_ERR_ALERT);

            var alert = new Alert(AlertType.ERROR);
            alert.setTitle(NONMODAL_ERR_ALERT);
            alert.setHeaderText(NONMODAL_ERR_ALERT);
            alert.setContentText(NONMODAL_ERR_ALERT);

            // Alerts exhibit Modality.APPLICATION_MODAL by default but,
            // you can specify WINDOW_MODAL to block the window or NONE
            // to be non-modal at all.
            alert.initModality(Modality.NONE);

            // you can also hook into when Modal's are initially shown using the
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
          }),
          new DialogOption(ALERT_NO_HEADER, dialogsToggleGrp, () -> {
            updateWebResouceUrls(ALERT_NO_HEADER);

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
          }),
          new DialogOption(TEXT_DIALOG, dialogsToggleGrp, () -> {
            updateWebResouceUrls(TEXT_DIALOG);

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
            clearDialogOptionSelections();
          }),
          new DialogOption(CHOICE_DIALOG, dialogsToggleGrp, () -> {
            updateWebResouceUrls(CHOICE_DIALOG);

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
            clearDialogOptionSelections();
          }),
          new DialogOption(CUSTOM_DLG, dialogsToggleGrp, () -> {
            updateWebResouceUrls(CUSTOM_DLG);
            
            var countDownDlg = new CountDownDialog(primaryStage, 60, CUSTOM_DLG, true);

            countDownDlg.showAndWait().ifPresent((endingValue) -> {
                feedbackLbl.setText(String.format("%s was closed at %d", CUSTOM_DLG, endingValue));
                clearDialogOptionSelections();
            });
          })
        );
        
        var scene = new Scene(rootPane, APP_WIDTH, APP_HEIGTH);
        var url = getClass().getResource("styles.css");
        scene.getStylesheets().add(url.toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    HBox makeHeader() {
        var headerLabel = new Label("JavaFX Dialog Explorer");
        headerLabel.setFont(Font.font("Cambria", FontWeight.BOLD, 32));
        var headerHBox = new HBox(headerLabel);
        headerHBox.setAlignment(Pos.CENTER);
        headerHBox.getStyleClass().add("heading");
        return headerHBox;
    }

    HBox makeSourceButtons() {
        var sourceBtnTG = new ToggleGroup();
        var tciImageView = new ImageView(new Image(
            getClass().getResourceAsStream("tci-logo.png")
        ));
        tciImageView.setPreserveRatio(true);
        tciImageView.setFitWidth(71);

        var javafxImageView = new ImageView(new Image(
            getClass().getResourceAsStream("javafx-logo.png")
        ));
        javafxImageView.setPreserveRatio(true);
        javafxImageView.setFitWidth(69);
        
        var tciBtn = new ToggleButton(null, tciImageView);
        var javafxBtn = new ToggleButton(null, javafxImageView);

        tciBtn.setToggleGroup(sourceBtnTG);
        javafxBtn.setToggleGroup(sourceBtnTG);

        tciBtn.getStyleClass().add("source-btn");
        javafxBtn.getStyleClass().add("source-btn");

        tciBtn.setOnAction(evt -> {
            webView.getEngine().load(currentTCIUrl);
        });
        javafxBtn.setOnAction(evt -> {
            webView.getEngine().load(currentJavaFXUrl);
        });

        return new HBox(3, tciBtn, javafxBtn);
    }

    void clearDialogOptionSelections() {
        menuVBox.getChildren().forEach((node) -> {
          if (node instanceof DialogOption) {
              var dlgOption = (DialogOption) node;
              dlgOption.setSelected(false);
          }
        });
    }

    void updateWebResouceUrls(String resourceKey) {
        Pair<String, String> resourcePair = webResourceMap.get(resourceKey);
        currentTCIUrl = resourcePair.getKey();
        currentJavaFXUrl = resourcePair.getValue();
        webView.getEngine().load(currentTCIUrl);
    }

    void buildWebResouceMap() {
        webResourceMap.putAll(Map.ofEntries(
          Map.entry(BLOCKING_NONE_ALERT, 
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#non-alert",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(BLOCKING_INFO_ALERT, 
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#informational-alert",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(BLOCKING_WARNING_ALERT,
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#warning-alert",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(BLOCKING_CONFIRM_ALERT,
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#confirmation-alert",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(BLOCKING_ERR_ALERT,
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#error-alert",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(NONBLOCKING_ERR_ALERT,  
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#error-alert-non-blocking",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(NONMODAL_ERR_ALERT, 
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#error-alert-non-modal",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(ALERT_NO_HEADER, 
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#alert-no-header",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Alert.html")),
          Map.entry(TEXT_DIALOG, 
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#text-input-dialog",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/TextInputDialog.html")),
          Map.entry(CHOICE_DIALOG, 
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#choice-box-dialog",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/ChoiceDialog.html")),
          Map.entry(CUSTOM_DLG, 
            new Pair<String, String>(
                "https://thecodinginterface.com/blog/javafx-alerts-and-dialogs/#custom-dialog",
                "https://openjfx.io/javadoc/11/javafx.controls/javafx/scene/control/Dialog.html"))
        ));
    }
}
