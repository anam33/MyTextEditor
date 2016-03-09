package editor;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.scene.Group;
/**
 * Created by andrewnam on 3/1/16.
 */
public class WidthListener implements ChangeListener<Number> {

    private final int MARGIN = 5;
    private KeyEventHandler key;
    private EditorScrollBar sc;

    public WidthListener(KeyEventHandler k, EditorScrollBar s) {
        key = k;
        sc = s;
    }

    private int getDimensionInsideMargin(int outsideDimension) {
        return outsideDimension - 2 * MARGIN;
    }

    public void changed(ObservableValue<? extends Number> observableValue, Number oldScreenWidth,
                        Number newScreenWidth) {
        int newWidth = getDimensionInsideMargin(newScreenWidth.intValue());
        sc.setLayoutX(newWidth - sc.getLayoutBounds().getWidth()/2);
        key.disp.setW(newWidth  - sc.getWidth());
        key.characters = key.disp.calcXY(key.characters);
        key.display();
    }

}
