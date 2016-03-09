package editor;
import java.io.*;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.text.Text;

public class Editor extends Application {

    private static final int FHUNDO = 500;
    private static String filename;
    private static EditorLinkedList characters;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        int windowWidth = FHUNDO;
        int windowHeight = FHUNDO;
        Scene scene = new Scene(root, windowWidth, windowHeight, Color.WHITE);
        EditorDisplay disp = new EditorDisplay(scene);
        EventHandler<KeyEvent> keyEventHandler =
                new KeyEventHandler(root,characters, disp, filename);
        scene.setOnKeyTyped(keyEventHandler);
        scene.setOnKeyPressed(keyEventHandler);
        EditorScrollBar scroll =
                new EditorScrollBar(windowWidth, windowHeight, (KeyEventHandler) keyEventHandler);
        root.getChildren().add(0,scroll);
        disp.setW(windowWidth - scroll.getWidth());
        ((KeyEventHandler) keyEventHandler).setScroll(scroll);
        scene.setOnMousePressed(new Selection((KeyEventHandler) keyEventHandler, disp));
        scene.setOnMouseClicked(new Mouse((KeyEventHandler) keyEventHandler));
        WidthListener wL = new WidthListener((KeyEventHandler) keyEventHandler, scroll);
        HeightListener hL = new HeightListener((KeyEventHandler) keyEventHandler, scroll);
        scene.widthProperty().addListener(wL);
        scene.heightProperty().addListener(hL);
        primaryStage.setTitle(filename);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Expected usage: Editor <source filename> ");
            System.exit(1);
        }
        filename= args[0];
        if (args.length == 2) {
            String debug = args[1];
            if (debug == "debug") {
                System.out.println("debug test");
            }
        }
        characters = new EditorLinkedList();
        try {
            File file = new File(filename);
            if (file.exists()) {
                FileReader reader = new FileReader(filename);
                BufferedReader bufferedReader = new BufferedReader(reader);
                int intRead = -1;
                int i = 0;
                while ((intRead = bufferedReader.read()) != -1) {
                    char charRead = (char) intRead;
                    Text temp = new Text(Character.toString(charRead));
                    EditorNode temp1 = new EditorNode(temp, EditorDisplay.makeRekt(temp));
                    characters.add(i,temp1);
                    i++;
                }
                bufferedReader.close();
            }
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("File not found! Exception was: " + fileNotFoundException);
        } catch (IOException ioException) {
            System.out.println("Error when copying; exception was: " + ioException);
        }
        launch(args);
    }
}
