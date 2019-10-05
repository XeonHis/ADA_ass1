package client.UI;

import javafx.application.Platform;

/**
 * @author paulalan
 * @create 2019/9/12 14:02
 */
public class ControlledStage {
	/**
	 * stage controller
	 */
	protected StageController myController;
	/**
	 * stage UI ID
	 */
	protected String myStageUIID;

	public void setStageController(StageController stageController){
		this.myController = stageController;
	}

	public void setStageName(String stageUIID){
		this.myStageUIID = stageUIID;
	}

	/**
	 * close original interface and switch to new interface
	 *
	 * @param stage name of new interface
	 */
	public void changeStage(String stage) {
		Platform.runLater(() ->{
			myController.showStage(stage, myStageUIID);
		});
	}
}
