package client.Communication;

import client.UI.Controller;
import client.UI.LoginController;
import client.UI.Main;
import client.messages.Message;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.*;

/**
 * @author paulalan
 * @create 2019/9/9 20:06
 */
public class Comm implements Runnable
{
	/**
	 * UI controller object
	 */
	private LoginController loginController = LoginController.getInstance();
	private Controller controller = Controller.getInstance();

	/**
	 * user name
	 */
	private String userName;

	/**
	 * host name(default:127.0.0.1)
	 */
	private String hostName;
	/**
	 * port(default:11111)
	 */
	private int port;

	/**
	 * socket
	 */
	private Socket s;
	/**
	 * Object output stream, transmit Message Object
	 */
	private ObjectOutputStream oos;
	/**
	 * Object input stream, transmit Message Object
	 */
	private ObjectInputStream ois;
	/**
	 * userList, store online user name
	 */
	private ArrayList<String> userList = new ArrayList<>();

	public Comm(String hostname, int port, String username)
	{
		this.hostName = hostname;
		this.port = port;
		this.userName = username;
		//give thread to Controller
		controller.setComm(this);
	}

	public String getUserName()
	{
		return userName;
	}

	@Override
	public void run()
	{
		try
		{
			s = new Socket(hostName, port);
			System.out.println("Socket creates successfully!");
			oos = new ObjectOutputStream(s.getOutputStream());
			System.out.println("oos creates successfully!");

			// connect instruction
			connect();
			while (s.isConnected())
			{
				ois = new ObjectInputStream(s.getInputStream());
				// read object from server
				Message message = (Message) ois.readObject();
				System.out.println(message);
				if (message != null)
				{

					switch (message.getType())
					{
						case "SUCCESS":
							// switch to chat interface
							loginController.changeStage(Main.CHATUIID);

							break;
						case "FAIL":
							// login failed, show reason
							loginController.setResultText(message.getContent());
							break;
						case "MSG":
							// chat interface, two types -> single and multiple
							controller.addOtherMessges(message);
							break;
						case "USERLIST":
							// update user list and calculate the number of online users
							controller.setUserList(message.getUserlist());
							break;
						case "NOTIFICATION":
							// online & offline notification
							controller.addNotification(message.getContent());
						default:
							break;
					}

				}

			}

		} catch (SocketTimeoutException ex)
		{
			ex.printStackTrace();
			loginController.setResultText(s + " Timeout!");
		} catch (Exception e)
		{
			e.printStackTrace();
			loginController.setResultText(s + " connect error!");

		}
	}


	/**
	 * send message object
	 *
	 * @param message message class
	 */
	public void send(Message message)
	{
		try
		{
			oos.writeObject(message);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * message type of connect by login
	 */
	public void connect()
	{
		//create message class
		Message message = new Message();
		message.setType("CONNECT");
		userList.add(userName);
		message.setUserlist(userList);
		message.setFrom(userName);
		//send
		send(message);
	}

	/**
	 * message type of disconnect
	 */
	public void disconnect()
	{
		//create message class
		Message message = new Message();
		message.setType("DISCONNECT");
		message.setFrom(userName);
		//send
		send(message);
	}

	/**
	 * close stream
	 */
	public void destroy()
	{
		new Thread(() ->
		{
			try
			{
				if (oos != null)
				{
					oos.close();
					oos = null;
				}
				if (ois != null)
				{
					ois.close();
					ois = null;
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}).start();
	}

	/**
	 * @param from    source of message
	 * @param to      destination of message
	 * @param content content of message
	 */
	public void sendMsg(String from, String to, String content)
	{
		//message type of MSG
		Message message = new Message();
		message.setType("MSG");
		message.setFrom(from);
		message.setTo(to);
		message.setContent(content);
		//send
		send(message);
	}
}
