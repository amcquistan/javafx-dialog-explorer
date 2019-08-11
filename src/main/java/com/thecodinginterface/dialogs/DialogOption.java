
package com.thecodinginterface.dialogs;

import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

class DialogOption extends ToggleButton {

    DialogOption(String optionName, ToggleGroup tg, Runnable callback) {
        super(optionName);
        getStyleClass().add("dialog-option");
        setToggleGroup(tg);
        setMinWidth(App.MENU_OPTIONS_WIDTH);
        setMaxWidth(App.MENU_OPTIONS_WIDTH);
        setOnAction(evt -> {
            callback.run();
        });
    }
}
