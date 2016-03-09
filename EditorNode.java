package editor;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

/**
 * Created by andrewnam on 2/26/16.
 */
public class EditorNode {
    public Text txt;
    public Rectangle box;

    public EditorNode(Text it, Rectangle rect) {
        txt = it;
        box = rect;
        box.setFill(Color.WHITE);
    }
    public EditorNode() {
        txt = null;
        box = null;
    }

    public Text getTxt() {
        return txt;
    }
    public Rectangle getRekt() {
        return box;
    }

    public void setTxt(Text t) {
        txt = t;
    }

    public void setBox(Rectangle b) {
        box = b;
    }
}
