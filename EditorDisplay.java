package editor;

import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.Group;
import javafx.scene.control.ScrollBar;

import java.util.HashMap;

/**
 * Created by andrewnam on 2/26/16.
 */
public class EditorDisplay {
    private double screenW;
    private double screenH;
    private static HashMap rows;
    private String fontName;
    private int fontSize;

    public EditorDisplay(Scene sc) {
        screenH = sc.getHeight();
        screenW = sc.getWidth();
        rows = new HashMap();
        rows.put(0, 0);
        fontName = "Verdana";
        fontSize = 12;
    }

    public void setW(double w) {
        screenW = w;
    }

    public void setH(double h) {
        screenH = h;
    }

    public void fontChange(EditorLinkedList e, String name, int size) {
        fontName = name;
        fontSize = size;
    }

    public static Rectangle makeRekt(Text t) {
        Rectangle rekt =
                new Rectangle(t.getX(), t.getY(), t.getLayoutBounds().getWidth(), t.getLayoutBounds().getHeight());
        return rekt;
    }

    public EditorLinkedList calcXY(EditorLinkedList e) {
        EditorLinkedList newList = new EditorLinkedList();
        Text measure = new Text("a");
        measure.setFont(Font.font(fontName, fontSize));
        double height = measure.getLayoutBounds().getHeight();
        rows.clear();
        rows.put(0, 0);
        Integer row = 0;
        Integer rowI = 0;
        double wIndex;
        double hIndex;
        while (!(e.isEmpty())) {
            hIndex = 0;
            while (!(e.isEmpty())) {
                wIndex = 5.0;
                boolean newLine = false;
                while (wIndex < screenW - 5.0 && !(e.isEmpty())) {
                    EditorNode temp = e.removeFirst();
                    Text tempTxt = new Text(wIndex, hIndex, temp.getTxt().getText());
                    tempTxt.setFont(Font.font(fontName, fontSize));
                    if (tempTxt.getText().equals("")) {
                        Rectangle tempRekt =
                                new Rectangle(tempTxt.getX(), tempTxt.getY(), 1, height);
                        newList.add(new EditorNode(tempTxt, tempRekt));
                    } else if (tempTxt.getText().equals("\r")) {
                        newList.add(new EditorNode(tempTxt, makeRekt(tempTxt)));
                        newLine = true;
                        rowI++;
                        break;
                    } else {
                        double newW = wIndex + Math.round(tempTxt.getLayoutBounds().getWidth());
                        if (newW < screenW - 5.0) {
                            newList.add(new EditorNode(tempTxt, makeRekt(tempTxt)));
                            wIndex = newW;
                            rowI++;
                        } else {
                            e.addFirst(temp);
                            break;
                        }
                    }
                }
                if (e.isEmpty()) {
                    break;
                } else if (!(e.get(0).getTxt().getText().equals(" ")) && !newLine) {
                    int index = newList.size() - 1;
                    int counter = 0;
                    boolean isSpace = false;
                    while (index >= 0 && newList.get(index).getTxt().getY() == hIndex) {
                        if (newList.get(index).getTxt().getText().equals(" ")) {
                            isSpace = true;
                            break;
                        }
                        index--;
                        counter++;
                        rowI--;
                    }
                    if (isSpace) {
                        while (counter > 0) {
                            EditorNode temp2 = newList.remove(newList.size() - 1);
                            e.addFirst(temp2);
                            counter--;
                        }
                    }
                }
                row++;
                rows.put(row, rowI);
                hIndex += height;
            }
        }
        return newList;
    }

    public int nearestNode(double x, double y, EditorLinkedList e) {
        int row;
        Text measure = new Text("a");
        measure.setFont(Font.font(fontName, fontSize));
        double height = measure.getLayoutBounds().getHeight();
        if (rows.size() > 0 && e.size() > 0) {
            row = Math.min((int) Math.round(y / height),
                    rows.size() - 1);
        } else {
            row = 0;
        }
        int i = (Integer) rows.get(row);
        int closestI = i;
        double rightY = e.get(i).getTxt().getY();
        double closest = Math.abs(x - (e.get(i).getTxt().getX()));
        if (rows.size() - 1 > row && e.get((Integer) rows.get(row + 1) - 1).getTxt().getText().equals("\r")) {
            while (i < e.size() - 1 && e.get(i).getTxt().getY() == rightY) {
                double centerX = e.get(i).getTxt().getX();
                if (Math.abs(x - centerX) < closest) {
                    closestI = i;
                    closest = Math.abs(x - centerX);
                }
                i++;
            }
        } else {
            while (i < e.size() && e.get(i).getTxt().getY() == rightY) {
                double centerX = e.get(i).getTxt().getX();
                if (Math.abs(x - centerX) < closest) {
                    closestI = i;
                    closest = Math.abs(x - centerX);
                }
                i++;
            }
            if (x > e.get(i - 1).getTxt().getX()) {
                return closestI + 1;
            }
        }
        return closestI;
    }

    public void backToCursor(EditorLinkedList e, int index, double start, ScrollBar sc) {
        double cursorY = e.get(index).getTxt().getY() + start;
        if (cursorY < 0) {
            sc.setValue(sc.getValue() + cursorY);
        } else if (cursorY > screenH) {
            sc.setValue(sc.getValue() + (cursorY + e.get(index).getTxt().getLayoutBounds().getHeight() - screenH));
        }
    }

    public int nextTxt(EditorLinkedList e, double start, int index) {
        double cursorY = e.get(index).getTxt().getY() + start;
        if (cursorY < 0) {
            int windowTop = nearestNode(0, -start, e);
            while (windowTop >= 0) {
                if (!(e.get(windowTop).getTxt().getText().equals("\r") ||
                        e.get(windowTop).getTxt().getText().equals(" ") ||
                        e.get(windowTop).getTxt().getText().equals(""))) {
                    return windowTop + 1;
                }
                windowTop--;
            }
        } else if (cursorY > screenH) {
            int windowBot = nearestNode(screenW,screenH,e) + 1;
            while (windowBot < e.size()) {
                if (!(e.get(windowBot).getTxt().getText().equals("\r")
                        || e.get(windowBot).getTxt().getText().equals(" ") ||
                        e.get(windowBot).getTxt().getText().equals(""))) {
                    return windowBot - 1;
                }
                windowBot++;
            }
        }
        return index;
    }
}

