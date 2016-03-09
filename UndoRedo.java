package editor;

import java.util.LinkedList;
/**
 * Created by andrewnam on 3/1/16.
 */
public class UndoRedo {

    private KeyEventHandler key;
    private LinkedList undo;
    private LinkedList redo;

    public UndoRedo(KeyEventHandler k) {
        key = k;
        undo = new LinkedList<UndoRedoNode>();
        redo = new LinkedList<UndoRedoNode>();
    }

    public void addUndo(UndoRedoNode n) {
        if (undo.size() >= 100) {
            undo.removeLast();
        }
        undo.addFirst(n);
    }
    public void addRedo(UndoRedoNode n) {
        if (redo.size() >= 100) {
            redo.removeLast();
        }
        redo.addFirst(n);
    }

    /* type 1: undo removes the most recently added character
    * 2: undo adds the most recently deleted character
     */
    public void undo(){
        if (!undo.isEmpty()) {
            UndoRedoNode recent = (UndoRedoNode) undo.removeFirst();
            if (recent.getType() == 1) {
                if (key.getIndex() < recent.getIndex()) {
                    key.characters.remove(recent.getIndex() + 1);
                    key.changeIndex(recent.getIndex() + 1);
                }
                else {
                    key.characters.remove(recent.getIndex());
                    key.incIndex(-1);
                    key.changeIndex(recent.getIndex());
                }
                key.disp.backToCursor(key.characters, key.getIndex(), key.startPos, key.sc);
                key.characters = key.disp.calcXY(key.characters);
                key.display();
                addRedo(recent);
            } else if (recent.getType() == 2) {
                if (key.getIndex() < recent.getIndex()) {
                    key.characters.add(recent.getIndex() + 1, recent.getE());
                    key.changeIndex(recent.getIndex() + 2);
                } else {
                    key.characters.add(recent.getIndex(), recent.getE());
                    key.incIndex(1);
                    key.changeIndex(recent.getIndex() + 1);
                }
                key.disp.backToCursor(key.characters, key.getIndex(), key.startPos, key.sc);
                key.characters = key.disp.calcXY(key.characters);
                key.display();
                addRedo(recent);
            }
        }
    }
    /* type 1: redo adds the most recently deleted character
    * 2: redo removes the most recently added character
     */
    public void redo() {
        if (!redo.isEmpty()) {
            UndoRedoNode recent = (UndoRedoNode) redo.removeFirst();
            if (recent.getType() == 1) {
                if (key.getIndex() < recent.getIndex()) {
                    key.characters.add(recent.getIndex() + 1, recent.getE());
                    key.changeIndex(recent.getIndex() + 2);
                } else {
                    key.characters.add(recent.getIndex(), recent.getE());
                    key.incIndex(1);
                    key.changeIndex(recent.getIndex() + 1);
                }
                key.disp.backToCursor(key.characters, key.getIndex(), key.startPos, key.sc);
                key.characters = key.disp.calcXY(key.characters);
                key.display();
                addUndo(recent);
            } else if (recent.getType() == 2) {
                if (key.getIndex() < recent.getIndex()) {
                    key.characters.remove(recent.getIndex() + 1);
                    key.changeIndex(recent.getIndex() + 1);
                }
                else {
                    key.characters.remove(recent.getIndex());
                    key.incIndex(-1);
                    key.changeIndex(recent.getIndex());
                }
                key.disp.backToCursor(key.characters, key.getIndex(), key.startPos, key.sc);
                key.characters = key.disp.calcXY(key.characters);
                key.display();
                addUndo(recent);
            }
        }
    }


}
