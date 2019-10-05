package client.UI;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author paulalan
 * @create 2019/9/12 13:58
 */
public class StageController
{
	/**
	 * Store stage objects
	 */
	private HashMap<String, Stage> stages = new HashMap<>();

	/**
	 * Add stage to Map
	 *
	 * @param name  stage name
	 * @param stage stage object
	 */
	private void addStage(String name, Stage stage)
	{
		stages.put(name, stage);
	}

	/**
	 * Save Main stage
	 *
	 * @param primaryStage Main stage object
	 */
	void addPrimaryStage(Stage primaryStage)
	{
		this.addStage("primaryStage", primaryStage);
	}

	/**
	 * @return primaryStage object
	 */
	public Stage getPrimaryStage()
	{
		return stages.get("primaryStage");
	}

	/**
	 *
	 * @param name find stage by name
	 * @return stage object
	 */
	Stage getStage(String name)
	{
		return stages.get(name);
	}

	/**
	 * fxml file which loads stage will store stage object into Map
	 *
	 * @param name     registered fxml file
	 * @param resource fxml url
	 * @param styles   stage styles
	 */
	void loadStage(String name, String resource, StageStyle... styles)
	{
		try
		{
			// load xml files
			FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(resource));
			Parent root;
			root = loader.load();

			// construct the stage
			Scene scene = new Scene(root);
			Stage stage = new Stage();
			stage.setScene(scene);

			// Acquire ViewControl of FXML by Loader and inject StageController into ViewControl
			ControlledStage controlledStage = loader.getController();
			controlledStage.setStageController(this);
			controlledStage.setStageName(name);
			// config initstyle
			for (StageStyle style : styles)
			{
				stage.initStyle(style);
			}
			// Add into Map
			this.addStage(name, stage);

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * remove loaded Stage Object
	 *
	 * @param name name of fxml files
	 * @return whether the file is deleted
	 */
	public boolean unloadStage(String name)
	{
		return stages.remove(name) != null;
	}

	/**
	 * show Stage
	 *
	 * @param name name of window which is needed
	 */
	void showStage(String name)
	{
		this.getStage(name).show();
	}


	/**
	 * show Stage and hide specified window
	 *
	 * @param show  window which needs to show
	 * @param close window which needs to close
	 */
	void showStage(String show, String close)
	{
		getStage(close).close();
		this.getStage(show).show();
	}

}