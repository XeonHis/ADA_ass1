package client.UI;


import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


import client.Communication.Comm;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

/**
 * @author paulalan
 * @create 2019/9/12 14:02
 */

public class LoginController extends ControlledStage implements Initializable
{
	/**
	 * LoginController object
	 */
	private static LoginController instance;

	@FXML
	private BorderPane borderPane;
	@FXML
	private ImageView defaultImgView;
	@FXML
	private TextField hostnameTextfield;
	@FXML
	private TextField portTextfield;
	@FXML
	private TextField usernameTextfield;
	@FXML
	private Text resultText;

	private double horizontalOffset;
	private double verticalOffset;

	public final static String[] NameList =
			{"Alex", "Brenda", "Connie", "Donny",
					"Echo", "Fuse", "Grace", "Henry",
					"Paul", "Tony", "Williams"};


	public LoginController()
	{
		instance = this;
	}

	/**
	 * get object of LoginController
	 *
	 * @return LoginController.class
	 */
	public static LoginController getInstance()
	{
		return instance;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		borderPane.setOnMousePressed(event ->
		{
			horizontalOffset = myController.getStage(myStageUIID).getX() - event.getScreenX();
			verticalOffset = myController.getStage(myStageUIID).getY() - event.getScreenY();
			borderPane.setCursor(Cursor.CLOSED_HAND);
		});
		borderPane.setOnMouseDragged(event ->
		{
			myController.getStage(myStageUIID).setX(event.getScreenX() + horizontalOffset);
			myController.getStage(myStageUIID).setY(event.getScreenY() + verticalOffset);
		});
		borderPane.setOnMouseReleased(event ->
		{
			borderPane.setCursor(Cursor.DEFAULT);
		});
	}

	/**
	 * random button -> generate random user name
	 *
	 * @param event mouse click
	 */
	@FXML
	public void randomBtnAction(ActionEvent event)
	{
		resultText.setText("");

		int num = new Random().nextInt(11);
		usernameTextfield.setText(NameList[num]);

		defaultImgView.setVisible(true);
		Image image = new Image("asset/images/default.png");
		defaultImgView.setImage(image);

	}

	/**
	 * connect keyboard shortcut
	 *
	 * @param event Enter press
	 */
	@FXML
	public void connectMethod(KeyEvent event)
	{
		if (event.getCode() == KeyCode.ENTER)
		{
			connectBtnAction();
		}
	}

	@FXML
	public void connectBtnAction()
	{
		String username = usernameTextfield.getText().trim();
		String hostname = hostnameTextfield.getText().trim();
		int port = Integer.parseInt(portTextfield.getText().trim());

		if ("".equals(username))
		{
			setResultText("Username cannot be empty !");
			return;
		}
		Comm comm = new Comm(hostname, port, username);
		new Thread(comm).start();
	}

	/**
	 * minimize the window
	 */
	@FXML
	public void minBtnAction()
	{
		myController.getStage(myStageUIID).setIconified(true);

	}

	/**
	 * close window and application
	 */
	@FXML
	public void closeBtnAction()
	{
		Platform.exit();
		System.exit(0);

	}

	/**
	 * show login result in loginUI
	 *
	 * @param result login result
	 */
	public void setResultText(String result)
	{
		resultText.setText(result);
	}

}

