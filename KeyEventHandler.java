package editor;

/**
 * Created by andrewnam on 2/27/16.
 */

import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.io.*;

/**
 * An EventHandler to handle keys that get pressed.
 */


public class KeyEventHandler implements EventHandler<KeyEvent> {

    /**
     * The Text to display on the screen.
     */
    private static final int FONTSTART = 12;
    private int fontSize = FONTSTART;
    private int index = 0;
    private String filename;
    private boolean selected;
    double startPos;
    EditorLinkedList characters;
    EditorDisplay disp;
    Group rt, r;
    UndoRedo ur;
    EditorScrollBar sc;

    private String fontName = "Verdana";

    public KeyEventHandler(final Group root, EditorLinkedList ed, EditorDisplay e, String f) {
        startPos = 0.0;
        disp = e;
        rt = root;
        r = new Group();
        rt.getChildren().add(r);
        characters = ed;
        Text sentText = new Text(5,0,"");
        Rectangle sentRekt = new Rectangle(1,sentText.getLayoutBounds().getHeight());
        sentRekt.setX(5);
        sentRekt.setY(0);
        EditorNode sentinel = new EditorNode(sentText, sentRekt);
        Cursor cursorBox = new Cursor(sentRekt);
        r.getChildren().add(sentRekt);
        cursorBox.makeRectangleColorChange();
        characters.addFirst(sentinel);
        characters = disp.calcXY(characters);
        filename = f;
        ur = new UndoRedo(this);
        display();
        selected = false;
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        if (keyEvent.isShortcutDown()) {
            KeyCode code = keyEvent.getCode();
            if (code == KeyCode.PLUS || code == KeyCode.EQUALS) {
                fontSize += 4;
                disp.fontChange(characters, fontName, fontSize);
                characters = disp.calcXY(characters);
                display();
            } else if (code == KeyCode.MINUS) {
                fontSize = Math.max(0, fontSize - 4);
                disp.fontChange(characters, fontName, fontSize);
                characters = disp.calcXY(characters);
                display();
            } else if (code == KeyCode.P) {
                Text temp = characters.get(index).getTxt();
                System.out.println((int)temp.getX() + ", " + (int)temp.getY());
            } else if (code == KeyCode.Z) {
                ur.undo();
            } else if (code == KeyCode.Y) {
                ur.redo();
            } else if (code == KeyCode.S) {
                File file = new File(filename);
                try {
                    FileWriter writer = new FileWriter(file);
                    for(int i = 0; i < characters.size(); i++) {
                        String charRead = characters.get(i).getTxt().getText();
                        writer.write(charRead);
                    }
                    writer.close();
                } catch (IOException ioException) {
                    System.out.println("Error when saving; exception was: " + ioException);
                }
            }
        } else {
            if (keyEvent.getEventType() == KeyEvent.KEY_TYPED) {
                // Use the KEY_TYPED event rather than KEY_PRESSED for letter keys, because with
                // the KEY_TYPED event, javafx handles the "Shift" key and associated
                // capitalization.
                String characterTyped = keyEvent.getCharacter();
                if (characterTyped.length() > 0 && characterTyped.charAt(0) != 8) {
                    Text temp = new Text(characterTyped);
                    temp.setFont(Font.font(fontName, fontSize));
                    EditorNode temp1 = new EditorNode(temp, disp.makeRekt(temp));
                    characters.add(index, temp1);
                    ur.addUndo(new UndoRedoNode(1, index, temp1));
                    index++;
                    characters = disp.calcXY(characters);
                    disp.backToCursor(characters, index, startPos, sc);
                    display();
                    keyEvent.consume();
                }
            } else if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                KeyCode code = keyEvent.getCode();
                if ((code == KeyCode.DELETE || code == KeyCode.BACK_SPACE) && index > 0) {
                    EditorNode temp = characters.remove(index);
                    index = Math.max(0, index - 1);
                    EditorNode temp1 = characters.remove(index);
                    characters.add(index, temp);
                    ur.addUndo(new UndoRedoNode(2, index, temp1));
                    characters = disp.calcXY(characters);
                    disp.backToCursor(characters, index, startPos, sc);
                    display();
                } else if (code == KeyCode.LEFT && index > 0) {
                    EditorNode temp = characters.remove(index);
                    index = Math.max(0, index - 1);
                    characters.add(index, temp);
                    int ind = disp.nextTxt(characters, startPos, index);
                    if (ind != index) {
                        changeIndex(ind);
                    }
                    characters = disp.calcXY(characters);
                    disp.backToCursor(characters, index, startPos, sc);
                    display();
                }
                else if (code == KeyCode.RIGHT && index < characters.size() - 1) {
                    EditorNode temp = characters.remove(index);
                    index = Math.min(index + 1, characters.size());
                    characters.add(index, temp);
                    int ind = disp.nextTxt(characters, startPos, index);
                    if (ind != index) {
                        changeIndex(ind);
                    }
                    characters = disp.calcXY(characters);
                    disp.backToCursor(characters, index, startPos, sc);
                    display();
                }
                else if (code == KeyCode.UP) {
                    if (characters.get(index).getTxt().getY() != 0) {
                        EditorNode temp = characters.remove(index);
                        index = disp.nearestNode(temp.getTxt().getX(),
                                        temp.getTxt().getY() - temp.getRekt().getHeight(),
                                        characters);
                        characters.add(index, temp);
                    }
                    int ind = disp.nextTxt(characters, startPos, index);
                    if (ind != 0) {
                        changeIndex(ind);
                    }
                    characters = disp.calcXY(characters);
                    disp.backToCursor(characters, index, startPos, sc);
                    display();
                }
                else if (code == KeyCode.DOWN) {
                    if (characters.get(index).getTxt().getY() !=
                            characters.getLast().getTxt().getY()) {
                        EditorNode temp = characters.remove(index);
                        index = disp.nearestNode(temp.getTxt().getX(),
                                        temp.getTxt().getY() + temp.getRekt().getHeight(),
                                        characters);
                        characters.add(index, temp);
                    }
                    int ind = disp.nextTxt(characters, startPos, index);
                    if (ind != index) {
                        changeIndex(ind);
                    }
                    characters = disp.calcXY(characters);
                    disp.backToCursor(characters, index, startPos, sc);
                    display();
                }
            }
        }
    }

    public void changeIndex(int i) {
        EditorNode temp = characters.remove(index);
        index = Math.min(i, characters.size());
        if (i == characters.size()) {
            characters.addLast(temp);
        } else {
            characters.add(index, temp);
        }
        characters = disp.calcXY(characters);
        display();
    }

    public void display() {
        r.getChildren().clear();
        for (int i = 0; i < characters.size(); i++) {
            if (!(i == index)) {
                Rectangle displayRekt = characters.get(i).getRekt();
                r.getChildren().add(displayRekt);
                Text displayText = characters.get(i).getTxt();
                displayText.setTextOrigin(VPos.TOP);
                r.getChildren().add(displayText);
            }
        }
        Rectangle temp = characters.get(index).getRekt();
        temp.setX(characters.get(index).getTxt().getX());
        temp.setY(characters.get(index).getTxt().getY());
        Cursor cursorBox = new Cursor(temp);
        r.getChildren().add(temp);
        temp.toFront();
        cursorBox.makeRectangleColorChange();
        r.setLayoutY(startPos);
        rt.getChildren().get(0).toFront();
    }

    public void setStart(double d) {
        startPos += d;
    }

    public void setScroll(EditorScrollBar s) {
        sc = s;
    }

    public void incIndex(int i) {
        index = index + i;
    }

    public int getIndex() {
        return index;
    }

    public void setSelected(boolean b) {
        selected = b;
    }
}