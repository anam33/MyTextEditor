package editor;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Created by andrewnam on 3/4/16.
 */
public class Selection implements EventHandler<MouseEvent>{
    private KeyEventHandler key;
    double firstPositionX;
    double firstPositionY;
    double lastPositionX;
    double lastPositionY;
    EditorDisplay disp;

    public Selection(KeyEventHandler k, EditorDisplay e) {
        key = k;
        disp = e;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        double mousePressedX = mouseEvent.getX();
        double mousePressedY = mouseEvent.getY();
        EventType eventType = mouseEvent.getEventType();
        if (eventType == MouseEvent.MOUSE_PRESSED) {
            firstPositionX = mousePressedX;
            firstPositionY = mousePressedY;
        } else if (eventType == MouseEvent.MOUSE_DRAGGED) {
            lastPositionX = mousePressedX;
            lastPositionY = mousePressedY;
            highlight();
            key.display();
        } else if (eventType == MouseEvent.MOUSE_RELEASED) {
            lastPositionX = mousePressedX;
            lastPositionY = mousePressedY;
            highlight();
            key.display();
            key.setSelected(true);
        }
    }

    public void highlight() {
        int start = disp.nearestNode(firstPositionX,firstPositionY,key.characters);
        int end = disp.nearestNode(lastPositionX,lastPositionY,key.characters);
        key.changeIndex(start);
        if (start > end) {
            int temp = start;
            start = end;
            end = temp;
        }
        for (int i = start; i <= end; i++) {
            key.characters.get(i).getRekt().setFill(Color.LIGHTBLUE);
        }
    }
}
