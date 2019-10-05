package client.UI;


import client.Communication.Comm;
import client.messages.Message;
import client.messages.User;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author paulalan
 * @create 2019/9/11 10:32
 */

public class Controller extends ControlledStage implements Initializable
{

	/**
	 * Controller object
	 */
	private static Controller instance;

	/**
	 * Comm object
	 */
	private Comm comm;

	/**
	 * interface root container
	 */
	@FXML
	private BorderPane borderPane;
	/**
	 * Default user profile photo
	 */
	@FXML
	private ImageView userImageView;
	/**
	 * user name
	 */
	@FXML
	private Label usernameLabel;
	/**
	 * online users
	 */
	@FXML
	private ListView<String> userListView;
	/**
	 * the number of online users
	 */
	@FXML
	private Label userCountLabel;
	/**
	 * message show list
	 */
	@FXML
	private ListView<HBox> chatPaneListView;
	/**
	 * message input area
	 */
	@FXML
	private TextArea messageBoxTextArea;
	/**
	 * message send button
	 */
	@FXML
	private Button sendButton;
	/**
	 * whom to talk with
	 */
	@FXML
	private Label otherUserNameLabel;

	/**
	 * offset
	 */
	private double xOffset;
	private double yOffset;

	/**
	 * user info list
	 */
	ArrayList<String> userInfoList = new ArrayList<>();
	/**
	 * current user name
	 */
	String userName = null;
	/**
	 * current user profile photo
	 */
	String userPic = null;

	/**
	 * (talk to all) sign
	 */
	String otherUserName = Message.ALL;

	public Controller()
	{
		instance = this;
	}

	/**
	 * acquire the object of chat UI
	 *
	 * @return Controller.class
	 */
	public static Controller getInstance()
	{
		return instance;
	}

	public void setComm(Comm comm)
	{
		this.comm = comm;
		userName = comm.getUserName();
		init();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		borderPane.setOnMousePressed(event ->
		{
			xOffset = myController.getStage(myStageUIID).getX() - event.getScreenX();
			yOffset = myController.getStage(myStageUIID).getY() - event.getScreenY();
			borderPane.setCursor(Cursor.CLOSED_HAND);
		});
		borderPane.setOnMouseDragged(event ->
		{
			myController.getStage(myStageUIID).setX(event.getScreenX() + xOffset);
			myController.getStage(myStageUIID).setY(event.getScreenY() + yOffset);
		});
		borderPane.setOnMouseReleased(event ->
		{
			borderPane.setCursor(Cursor.DEFAULT);
		});
	}

	/**
	 * send message
	 */
	@FXML
	public void sendBtnAction()
	{
		String content = messageBoxTextArea.getText();
		if (!content.isEmpty())
		{
			comm.sendMsg(userName, otherUserName, content);
			messageBoxTextArea.clear();
			addYourMessages(content);
		}
	}

	/**
	 * send keyboard shortcut
	 *
	 * @param event Enter press
	 */
	@FXML
	public void sendMethod(KeyEvent event)
	{
		if (event.getCode() == KeyCode.ENTER)
		{
			sendBtnAction();
		}
	}

	/**
	 * close interface
	 */
	@FXML
	public void closeImgViewPressedAction()
	{
		//login out in server
		comm.disconnect();
		//exit
		Platform.exit();
		System.exit(0);
	}

	/**
	 * login out
	 */
	@FXML
	public void logoutImgViewPressedAction()
	{
		//login out in server
		comm.disconnect();
		//clear the message dialog
		chatPaneListView.getItems().clear();
		//switch into login interface
		changeStage(Main.LOGINUIID);
	}

	/**
	 * initialize the Chat interface
	 */
	private void init()
	{
		userImageView.setImage(new Image("asset/images/default.png"));
		usernameLabel.setText(userName);
	}

	/**
	 * set online user list and show the number of online users except user self
	 *
	 * @param userInfolist online user list
	 */
	public void setUserList(ArrayList<String> userInfolist)
	{

		this.userInfoList = userInfolist;
		System.out.println("Online user: " + userInfoList);

		//add ALL selection into ListView
		if (!userInfolist.get(0).equals(Message.ALL))
		{
			User allUser = new User(Message.ALL);
			userInfoList.add(0, allUser.getUsername());
			System.out.println(userInfoList);
		}
		//online user amount
		int userCount = userInfoList.size() - 1;

		//remove user self
		for (String user : userInfoList)
		{
			if (user.equals(userName))
			{
				userInfoList.remove(user);
				break;
			}
		}
		//set online user list
		Platform.runLater(() ->
		{
			ObservableList<String> users = FXCollections.observableList(userInfoList);
			userListView.setItems(users);
			userListView.setCellFactory((ListView<String> L) -> new UsersCell());

			//set online user amount
			userCountLabel.setText(userCount + "");
		});

		//userListView -> click event listener
		userListView.getSelectionModel().selectedItemProperty().addListener(
				(ObservableValue<? extends String> ov, String old_val,
				 String new_val) ->
				{
					otherUserName = new_val;
					if (otherUserName.equals(Message.ALL))
					{
						otherUserNameLabel.setText("Chat with everyone..");
					} else
					{
						otherUserNameLabel.setText("Chat with " + otherUserName + ":");
					}
				});

	}


	/**
	 * custom the ListView
	 */
	static class UsersCell extends ListCell<String>
	{
		@Override
		protected void updateItem(String item, boolean empty)
		{
			super.updateItem(item, empty);
			setGraphic(null);
			setText(null);
			if (item != null)
			{
				HBox hBox = new HBox();
				//gap
				hBox.setSpacing(5);

				Text name = new Text(item);
				name.setFont(new Font(20));
				ImageView statusImageView = new ImageView();
				Image statusImage = new Image("asset/images/online.png", 16, 16, true, true);
				statusImageView.setImage(statusImage);

				if (item.equals(Message.ALL))
				{
					name.setText("group chat >");
					statusImageView.setImage(null);
				}
				ImageView pictureImageView = new ImageView();

				hBox.getChildren().addAll(statusImageView, pictureImageView, name);
				hBox.setAlignment(Pos.CENTER_LEFT);
				setGraphic(hBox);
			}
		}
	}

	/**
	 * add message of other users into dialog box
	 *
	 * @param message message.class -> contain From, To, Content, etc.
	 */
	public void addOtherMessges(Message message)
	{
		Task<HBox> msgHander = new Task<HBox>()
		{

			@Override
			protected HBox call() throws Exception
			{
				//set default image
				Image image = new Image("asset/images/default.png");
				ImageView profileImage = new ImageView(image);
				profileImage.setFitHeight(30);
				profileImage.setFitWidth(30);
				Label label = new Label();
				label.setFont(Font.font(15));
				// determine message type
				if (message.getTo().equals(Message.ALL))
				{
					label.setText("[public message]" + message.getFrom() + ": " + message.getContent());
					label.setBackground(new Background(new BackgroundFill(Color.LIGHTYELLOW, null, null)));
				} else
				{
					label.setText("[private message] " + message.getFrom() + ": " + message.getContent());
					label.setBackground(new Background(new BackgroundFill(Color.LIGHTBLUE, null, null)));
				}

				HBox x = new HBox();
				label.setPadding(new Insets(10, 5, 5, 5));
				x.getChildren().addAll(profileImage, label);
				return x;
			}
		};
		msgHander.setOnSucceeded(event ->
		{
			chatPaneListView.getItems().add(msgHander.getValue());
		});
		Thread t2 = new Thread(msgHander);
		t2.setDaemon(true);
		t2.start();
	}

	/**
	 * add own message into ListView
	 *
	 * @param content message content
	 */
	public void addYourMessages(String content)
	{

		Platform.runLater(() ->
		{
			//default image
			Image image = new Image("asset/images/default.png");
			ImageView profileImage = new ImageView(image);
			profileImage.setFitHeight(32);
			profileImage.setFitWidth(32);
			Label label = new Label();
			label.setFont(Font.font(15));
			label.setText(content);
			label.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, null, null)));
			HBox x = new HBox();
			x.setMaxWidth(chatPaneListView.getWidth() - 20);
			x.setAlignment(Pos.TOP_RIGHT);
			label.setPadding(new Insets(5, 5, 5, 5));
			x.getChildren().addAll(label, profileImage);
			chatPaneListView.getItems().add(x);
		});
	}

	/**
	 * show online & offline notification
	 *
	 * @param notice online & offline
	 */
	public void addNotification(String notice)
	{
		Platform.runLater(() ->
		{
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			String timer = df.format(new Date());
			Label label = new Label();
			label.setFont(Font.font(15));
			label.setTextFill(Color.BLACK);
			label.setText(timer + ": " + notice);
			label.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
			HBox x = new HBox();
			x.setAlignment(Pos.TOP_CENTER);
			label.setPadding(new Insets(5, 5, 5, 5));
			x.getChildren().addAll(label);
			chatPaneListView.getItems().add(x);
		});

	}

}
