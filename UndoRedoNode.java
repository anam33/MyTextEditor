package editor;

/**
 * Created by andrewnam on 3/1/16.
 */
public class UndoRedoNode {
    private int type;
    private int index;
    private EditorNode ed;
    public UndoRedoNode(int t, int i, EditorNode e) {
        type = t;
        index = i;
        ed = e;
    }

    public int getType(){
        return type;
    }
    public EditorNode getE(){
        return ed;
    }
    public int getIndex() {
        return index;
    }
}
