package bugger.handlers;

import java.util.*;
import com.sun.net.httpserver.*;
import bugger.dataAccess.UserData;
import bugger.dataAccess.CookieData;
import bugger.dataModel.User;

public class HandlerUtilites
	{
	public static String GetCookieIDFromCookie(Headers headers)
		{
		String returnValue = null;
		for (Map.Entry<String,List<String>> entry : headers.entrySet())
			{
    		//System.out.println("Key: " + entry.getKey() + ": \n -" + entry.getValue());
			if(entry.getKey().toLowerCase().equals("cookie"))
				{
				//System.out.println(" -- Found a sweet cookie! (::) -> Contents -- ");
				Iterator<String> strings = entry.getValue().iterator();
				while(strings.hasNext())
					{
					String cookieChip = strings.next();
					//System.out.println("   -> " + cookieChip);
					returnValue = PullUserID(cookieChip);
					}
				}
			}
		return(returnValue);
		}

	private static String PullUserID(String inputString)
		{
		String returnString = null;
		//System.out.println("Substring: " + inputString.substring(0,6));
		if(inputString.substring(0,6).equals("UserID"))
			{
			returnString = inputString.substring(7);
			}
		return(returnString);
		}

	public static User GetUserFromCookie(String cookieContents)
		{
		String userID = CookieData.GetUserFromCookie(cookieContents);
		User user = UserData.GetUserByParameter(userID,"userID");
		user.GetPermissions();
		return(user);
		}
	}
