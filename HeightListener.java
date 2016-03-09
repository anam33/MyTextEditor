package editor;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.control.ScrollBar;

/**
 * Created by andrewnam on 3/1/16.
 */
public class HeightListener implements ChangeListener<Number> {
    private final int MARGIN = 5;
    private KeyEventHandler key;
    private EditorScrollBar sc;

    public HeightListener(KeyEventHandler k, EditorScrollBar s) {
        key = k;
        sc = s;
    }

    private int getDimensionInsideMargin(int outsideDimension) {
        return outsideDimension - 2 * MARGIN;
    }

    public void changed(
            ObservableValue<? extends Number> observableValue,
            Number oldScreenHeight,
            Number newScreenHeight) {
        int newHeight = getDimensionInsideMargin(newScreenHeight.intValue());
        sc.setPrefHeight(newHeight);
        key.disp.setH(newHeight);
        key.characters = key.disp.calcXY(key.characters);
        key.display();
    }

}
