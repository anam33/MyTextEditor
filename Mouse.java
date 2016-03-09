package editor;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;


/**
 * Created by andrewnam on 2/29/16.
 */
public class Mouse implements EventHandler<MouseEvent> {

    KeyEventHandler key;

    public Mouse(KeyEventHandler e) {
        key = e;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        // Because we registered this EventHandler using setOnMouseClicked, it will only called
        // with mouse events of type MouseEvent.MOUSE_CLICKED.  A mouse clicked event is
        // generated anytime the mouse is pressed and released on the same JavaFX node.
        double mousePressedX = mouseEvent.getX();
        double mousePressedY = mouseEvent.getY();
        EditorNode temp = key.characters.remove(key.getIndex());
        int newInd = key.disp.nearestNode(mousePressedX, mousePressedY, key.characters);
        key.characters.add(key.getIndex(), temp);
        if (key.getIndex() < newInd) {
            key.changeIndex(newInd + 1);
        } else {
            key.changeIndex(newInd);
        }
    }
}
