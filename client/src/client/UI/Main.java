package client.UI;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author paulalan
 * @create 2019/9/9 20:32
 */
public class Main extends Application
{
	public final static String LOGINUIID = "LoginUI";
	public final static String LOGINUIFXML = "client/UI/LoginUI.fxml";

	public final static String CHATUIID = "ChatUI";
	public final static String CHATUIFXML = "client/UI/chatUI.fxml";

	@Override
	public void start(Stage primaryStage)
	{
		StageController stageController = new StageController();
		stageController.addPrimaryStage(primaryStage);

		//No tool bar decoration of operation
		stageController.loadStage(LOGINUIID, LOGINUIFXML, StageStyle.UNDECORATED);
		stageController.loadStage(CHATUIID, CHATUIFXML, StageStyle.UNDECORATED);

		stageController.showStage(LOGINUIID);
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
