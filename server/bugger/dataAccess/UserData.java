package bugger.dataAccess;

import java.sql.*;
import java.util.ArrayList;
import bugger.dataModel.User;
import bugger.dataModel.Permission;

public class UserData
	{
	//Creates a new entry into the database
	public static User CreateNewUser(String username, String email, String password, String alias, String firstName, String lastName)
		{
		if(username == null || email == null || password == null || firstName == null || lastName == null)
			{
			return null;
			}

		String userID = User.GenerateUserID(firstName,lastName);
		String hashedPassword = User.HashPassword(password);

		try
			{
			Connection connect = DriverManager.getConnection(DataAccess.databaseConnection);
			Statement statement = connect.createStatement();
			statement.executeUpdate("INSERT INTO User(userID,username,email,password,alias,firstName,lastName) VALUES ('"
									+ userID + "','"
									+ username + "','"
									+ email + "','"
									+ hashedPassword + "','"
									+ alias + "','"
									+ firstName + "','"
									+ lastName + "')");
			connect.close();
			}
		catch (Exception e)
			{
			System.out.println(e.getMessage()); 
        	}

		return(new User(userID,username,email,hashedPassword,alias,firstName,lastName));
		}

	//Gets a new user, DOES NOT get the user's permissions~
	public static User GetUserByUsername(String username)
		{
		User returnValue = null;

		if(username == null)
			{
			return null;
			}

		try
			{
			Connection connect = DriverManager.getConnection(DataAccess.databaseConnection);
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT userID,email,password,alias,firstName,lastName FROM User WHERE username = '"+ username +"'" );

			result.next();

			String userID = result.getString("userID");
			String email = result.getString("email");
			String password = result.getString("password");
			String alias = result.getString("alias");
			String firstName = result.getString("firstName");
			String lastName = result.getString("lastName");

			if(username != null && email != null && password != null && firstName != null && lastName != null)
				{
				returnValue = new User(userID,username,email,password,alias,firstName,lastName);
				}
			connect.close();
			}
		catch (Exception e)
			{
			System.out.println("Cannot find user! Exception: " + e.getMessage()); 
        	}

		return(returnValue);
		}

	public static boolean CheckForUserByUsername(String username)
		{
		boolean returnValue = false;

		if(username == null)
			{
			return false;
			}

		try
			{
			Connection connect = DriverManager.getConnection(DataAccess.databaseConnection);
			Statement statement = connect.createStatement();
			ResultSet result = statement.executeQuery("SELECT userID,email,password,alias,firstName,lastName FROM User WHERE username = '"+ username +"'" );

			if(result.next())
				{
				returnValue = true;
				}
			connect.close();
			}
		catch (Exception e)
			{
			System.out.println("Cannot find user! Exception: " + e.getMessage()); 
	    	}

		return(returnValue);
		}


	public static boolean AddUserPermission(String username,String permissionName)
		{
		boolean returnValue = false;
		//Load the user and permission
		User targetUser = GetUserByUsername(username);
		Permission targetPermission = PermissionData.GetByName(permissionName);

		if(targetUser != null || targetPermission != null)
			{
			try
				{
				Connection connect = DriverManager.getConnection(DataAccess.databaseConnection);
				Statement statement = connect.createStatement();
				statement.executeUpdate("INSERT IGNORE INTO UserPermission(permissionID,userID) VALUES ('"
										+ targetPermission.permissionID + "','"
										+ targetUser.userID + "')");
				connect.close();

				returnValue = true;
				}
			catch (Exception e)
				{
				System.out.println(e.getMessage()); 
				}
			}

		return(returnValue);
		}

	public static Permission[] GetUserPermissions(String username)
		{
		ArrayList<Permission> permissionList = new ArrayList<Permission>();
		boolean targetUser = CheckForUserByUsername(username);

		if(targetUser)
			{
			try
				{
				Connection connect = DriverManager.getConnection(DataAccess.databaseConnection);
				Statement statement = connect.createStatement();

				//Get USERID
				ResultSet result = statement.executeQuery("SELECT USERID FROM User WHERE USERNAME = '" + username + "'");
				result.next();
				String userID = result.getString("USERID");
				
				//Get the permissions
				result = statement.executeQuery("SELECT * FROM Permission JOIN UserPermission on UserPermission.permissionID = Permission.permissionID WHERE userID ='"+ userID +"'" );

				while(result.next())
					{
					String permissionID = result.getString("permissionID");
					String permissionName = result.getString("permissionName");
					String discription = result.getString("discription");
					permissionList.add(new Permission(permissionID, permissionName,discription));
					}

				connect.close();
				}
			catch (Exception e)
				{
				System.out.println(e.getMessage()); 
		    	}
			}

		return(permissionList.toArray(new Permission[permissionList.size()]));
		}

	public static boolean ValidateUserID()
		{
		boolean returnValue = false;

		return(returnValue);
		}
	}
