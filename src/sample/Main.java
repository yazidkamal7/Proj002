package sample;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeSet;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


public class Main extends Application {


    static int[] rep = new int[256];
    static String fileName;
    static String fileEnd;
    static String filePath;

    static String fileName2;
    static String fileEnd2;
    static String filePath2;

    static File in;
    static TextField pathField;
    static Button compress;


    static TextField pathField2;
    static Button decompress;
    static Stage stage;
    static BorderPane root;

    static Button head;
    static Button statistics;
    static WriteData write1;

    static VBox rightBox;


    @Override
    public void start(Stage primaryStage) {
        try {
            stage = primaryStage;
            root = new BorderPane();
            Scene scene = new Scene(root,800,600);

            VBox topBox = new VBox(20);
            topBox.setId("TOP");
            topBox.setAlignment(Pos.CENTER);

            pathField = new TextField();
            pathField.setMinWidth(300);
            pathField.setMinHeight(30);
            Label pathLabel = new Label("File Path");
            HBox box = new HBox(30);
            topBox.setAlignment(Pos.CENTER);
            topBox.setPadding(new Insets(20,0,0,50));
            Button browse = new Button("Browse");
            browse.setMinWidth(100);
            box.getChildren().addAll(pathLabel,pathField,browse);
            topBox.getChildren().add(box);

            compress = new Button("Compress");
            compress.setDisable(true);
            topBox.getChildren().add(compress);

            VBox bottomBox = new VBox(20);
            bottomBox.setId("BOTTOM");
            Label pathLabel2 = new Label("File Path");
            pathField2 = new TextField();
            pathField2.setMinWidth(300);
            pathField2.setMinHeight(30);
            HBox box2 = new HBox(30);
            bottomBox.setAlignment(Pos.CENTER);
            bottomBox.setPadding(new Insets(20,0,0,50));
            Button browse2 = new Button("Browse");
            browse2.setMinWidth(100);
            box2.getChildren().addAll(pathLabel2,pathField2,browse2);
            bottomBox.getChildren().add(box2);

            decompress = new Button("Decompress");
            decompress.setDisable(true);
            bottomBox.getChildren().add(decompress);


            browse.setOnAction(e -> readOriginFileName());
            compress.setOnAction(e -> startCompress());

            browse2.setOnAction(e -> readCompressedFile());
            decompress.setOnAction(e -> startDecompress());


            head = new Button("Header");
            statistics = new Button("Statistics");

            VBox leftBox = new VBox(30);
            leftBox.setId("LEFT");
            leftBox.setMinWidth(150);
            leftBox.setAlignment(Pos.CENTER);
            leftBox.getChildren().addAll(head,statistics);
            head.setMinWidth(150);
            statistics.setMinWidth(150);
            root.setLeft(leftBox);

            head.setDisable(true);
            statistics.setDisable(true);

            head.setOnAction(e -> {
                root.setCenter(write1.area);
                root.setRight(null);
            });


            rightBox = new VBox();

            statistics.setOnAction(e -> {
                rightBox = write1.rightBox;
                rightBox.setId("RIGHT");
                root.setRight(rightBox);
                root.setCenter(write1.statTable);
                System.out.println("testttt");
            });

            root.setTop(topBox);
            root.setBottom(bottomBox);

//			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Huffman Compresser");
            primaryStage.show();


        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    static void readOriginFileName() {
        compress.setDisable(false);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        in = fileChooser.showOpenDialog(null);
        fileName = in.getName();
        int ind = in.getPath().lastIndexOf(".");
        filePath = in.getPath().substring(0,ind) + ".huff";
        int index = fileName.lastIndexOf(".");
        fileEnd = fileName.substring(index+1);
        pathField.setText(in.getPath());
        System.out.println(fileName);
        System.out.println(in.getPath());
        System.out.println(fileEnd);
    }



    static void  startCompress()  {
        try {
            head.setDisable(false);
            statistics.setDisable(false);
            ReadFile read = new ReadFile();
            read.readFile(rep,in);
            HuffmanCode encode = new HuffmanCode();
            encode.generateCodes(rep);
            System.out.println(10);
            System.out.println(encode.codes);
            System.out.println(11);
            write1 = new WriteData();
            write1.compress(encode.codes, rep,in,filePath,fileEnd);
        } catch (Exception e) {
            System.out.println("error  " + e.getMessage());
        }
    }

    static void readCompressedFile() {
        decompress.setDisable(false);
        FileChooser fileChooser = new  FileChooser();
        fileChooser.setTitle("Open Resource File");
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("Huffman Code Compressd(*.huff)", "*.huff");
        fileChooser.getExtensionFilters().add(extFilter);
        in = fileChooser.showOpenDialog(null);
        fileName = in.getName();
        int ind = fileName.lastIndexOf(".");
        fileName = fileName.substring(0, ind);
        int index = in.getPath().lastIndexOf(".");
        filePath = in.getPath().substring(0,index) + "-.";
        pathField2.setText(in.getPath());


    }

    static void  startDecompress()  {
        try {
            WriteData write = new WriteData();
            write.decompress(in,filePath,fileName);

        } catch (Exception e) {
            System.out.println("error  " + e.getMessage());
        }
    }

    public static void main(String[] args) throws IOException {


        launch(args);
    }
}
