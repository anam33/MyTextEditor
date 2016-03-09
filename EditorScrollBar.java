package editor;

import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.control.ScrollBar;
import javafx.beans.value.ObservableValue;

/**
 * Created by andrewnam on 2/29/16.
 */
public class EditorScrollBar extends ScrollBar {
    //scroll bar
    private double windowWidth;
    private double windowHeight;
    private KeyEventHandler key;

    public EditorScrollBar(double x, double y, KeyEventHandler k) {
        windowWidth = x;
        windowHeight = y;
        key = k;
        setOrientation(Orientation.VERTICAL);
        // Set the height of the scroll bar so that it fills the whole window.
        setPrefHeight(windowHeight);
        setLayoutX(x - getLayoutBounds().getWidth());
        // Set the range of the scroll bar.
        setMin(0);
        setMax(1000);

        double usableScreenWidth = windowWidth - getLayoutBounds().getWidth();
        setLayoutX(usableScreenWidth);
        valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(
                    ObservableValue<? extends Number> observableValue,
                    Number oldValue,
                    Number newValue) {
                // newValue describes the value of the new position of the scroll bar. The numerical
                // value of the position is based on the position of the scroll bar, and on the min
                // and max we set above. For example, if the scroll bar is exactly in the middle of
                // the scroll area, the position will be:
                //      scroll minimum + (scroll maximum - scroll minimum) / 2
                // Here, we can directly use the value of the scroll bar to set the height of Josh,
                // because of how we set the minimum and maximum above.
                key.setStart(oldValue.doubleValue() - newValue.doubleValue());
                key.display();
            }
        });
    }
}
