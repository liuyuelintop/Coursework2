/**
 * projectName: SpriteEditor
 * fileName: Main.java 
 * packageName: SpriteEditor
 * Author: Liu Yuelin
 * Student ID: 6522048
 */

package SpriteEditor;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.scene.control.ColorPicker;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public final class Main extends Application {
	private final Desktop desktop = Desktop.getDesktop();
    ColorPicker colorPicker = new ColorPicker();
	private Image image;
	private ImageView imageView;
	private boolean loadfile = false;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {

        FileChooser fileChooser = new FileChooser();

        // Enable UI controls such as buttons and color selectors
        // to be adaptive based on the size of the window

		colorPicker.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		colorPicker.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);

		Button openButton = new Button("Open a picture..");
		openButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		openButton.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);

		Button newButton = new Button("New");
		newButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		newButton.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);

		Button saveButton = new Button("Save");
		saveButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		saveButton.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);

		Button resetButton = new Button("Reset");
		resetButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		resetButton.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);

        HBox topbuttons = new HBox(20);
        topbuttons.setStyle("-fx-background-color:#B3E5FF");
        topbuttons.setAlignment(Pos.CENTER);
        topbuttons.getChildren().addAll(openButton, newButton, colorPicker, resetButton, saveButton);
        topbuttons.setHgrow(openButton,Priority.ALWAYS);
        topbuttons.setHgrow(newButton,Priority.ALWAYS);
        topbuttons.setHgrow(colorPicker,Priority.ALWAYS);
        topbuttons.setHgrow(resetButton, Priority.ALWAYS);
        topbuttons.setHgrow(saveButton,Priority.ALWAYS);

		BorderPane borderPane = new BorderPane();
		GridPane gridPane = new GridPane();

        // Initialize the buttons according to 32 x 32 specifications,
        InitializePixelButtons(gridPane);

		// Load the "*.png" file into the Sprite Editor.
		openButton.setOnAction(e ->{
			configureFileChooser(fileChooser);
			File file = fileChooser.showOpenDialog(primaryStage);
			if(file != null){
				image = ReturnImage(file);
				imageView = new ImageView();
				imageView.setImage(image);
                gridPane.getChildren().clear();
                //Write the color of the pixel of the loaded image to the button
                WritePixelToButton(gridPane);
			}
			loadfile = true;
		});

		// To Rebuild buttons which are used to represent the pixel.
		newButton.setOnAction((final ActionEvent e) ->{
			gridPane.getChildren().clear();
            InitializePixelButtons(gridPane);
		});

        // if we have already loaded the png file
        // Return buttons to the state it was in when the image was just loaded
        // Otherwise ReInitialize the buttons
		resetButton.setOnAction((final ActionEvent event) ->{
			gridPane.getChildren().clear();
			if(loadfile){
                // Write the color of the pixel of the last loaded image to the button
                WritePixelToButton(gridPane);
            }else {
			    InitializePixelButtons(gridPane);
            }
		});

		// Export the image represented by the button to a file in PNG format when click "Save" Button
		saveButton.setOnAction((final ActionEvent t) ->{
			File file = fileChooser.showSaveDialog(primaryStage);
			// Get the image composed of buttons by using snapshot() method.
			WritableImage image = gridPane.snapshot(new SnapshotParameters(), null);
			if (file != null){
				try{
					ImageIO.write(SwingFXUtils.fromFXImage(image, null),"png", file);
				}catch (IOException e){
					e.printStackTrace();
				}
			}
		});

		// Show the stage
        borderPane.setTop(topbuttons);
		borderPane.setCenter(gridPane);
		Scene scene = new Scene(borderPane,640, 640);
		primaryStage.setTitle("Sprite Editor");
		primaryStage.setScene(scene);
		primaryStage.show();

	}

    // Initialize the buttons according to 32 x 32 specifications,
    // Each button represents one pixel and the initial color will be set as white.
    private void InitializePixelButtons(GridPane gridpane){
        for( int y  = 0; y<32; y++){
            for(int x = 0; x<32; x++){
                Button button = new Button();
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);
                button.setBackground(new Background(new BackgroundFill(Color.WHITE,null,null)));
                button.setOnAction((ActionEvent t) ->{
                    button.setBackground(new Background(new BackgroundFill(colorPicker.getValue(), null, null)));
                });
                gridpane.add(button,x,y);
            }
        }
    }

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("View Pictures");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG", "*.png")
		);
	}

    // Return the image generated from the chosen PNG file
	private Image ReturnImage(File file) {
		String imagePath = file.getAbsoluteFile().toURI().toString();
		System.out.println(imagePath);
		Image temp = new Image(imagePath);
		return temp;
	}


    // Write the color of the pixel of the last loaded image to the button
    private void WritePixelToButton(GridPane gridpane){

        int width = (int) image.getWidth();
        int height = (int) image.getHeight();

        for(int y = 0; y< height; y++){
            for (int x = 0; x<width; x++){
                PixelReader pixelReader = image.getPixelReader();
                Button button = new Button();
                button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                button.setMinSize(Double.MIN_VALUE, Double.MIN_VALUE);
                Color color =pixelReader.getColor(x,y);
                button.setBackground(new Background(new BackgroundFill(color,null,null)));
                button.setOnAction((ActionEvent t) ->{
                    button.setBackground(new Background(new BackgroundFill(colorPicker.getValue(), null, null)));
                });
                gridpane.setHgrow(button,Priority.ALWAYS);
                gridpane.setVgrow(button, Priority.ALWAYS);
                gridpane.add(button,x,y);
            }
        }
    }
}

