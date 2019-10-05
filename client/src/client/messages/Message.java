package client.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author paulalan
 * @create 2019/9/12 13:51
 */
public class Message implements Serializable
{
	public static final String ALL = "@ALL*";
	private ArrayList<String> userlist;
	private HashMap<String, Object> map = new HashMap<>();

	public String getType()
	{
		return (String) map.get("MessageType");
	}

	public void setType(String type)
	{
		map.put("MessageType", type);
	}


	public ArrayList<String> getUserlist()
	{
		return userlist;
	}


	public void setUserlist(ArrayList<String> temp)
	{
		this.userlist = temp;
	}

	public String getFrom()
	{
		return (String) map.get("From");
	}

	public void setFrom(String from)
	{
		map.put("From", from);
	}

	public String getTo()
	{
		return (String) map.get("To");
	}

	public void setTo(String to)
	{
		map.put("To", to);
	}

	public String getContent()
	{
		return (String) map.get("Content");
	}

	public void setContent(String content)
	{
		map.put("Content", content);
	}

	public HashMap<String, Object> getMap()
	{
		return map;
	}

	@Override
	public String toString()
	{
		return "(M)UserList=" + String.valueOf(userlist) + " Type=" + getType() +
				" From=" + getFrom() + " =To" + getTo() + " Content=" + getContent();
	}
}
