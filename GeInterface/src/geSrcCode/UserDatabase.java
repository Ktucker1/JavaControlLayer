package geSrcCode;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class UserDatabase {
	private File file;
	private ArrayList<User> users = new ArrayList<User>();
	
	public UserDatabase(){
		// load login information from file
        file = new File("data.txt");
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String username = scanner.next();
                String password = scanner.next();
                String rights = scanner.next();
                boolean isStudent = true;
                // 0 for teacher, 1 for student
                if (rights.equals("0"))
                	isStudent = false;
                users.add(new User(username, password, isStudent));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e){
        	e.printStackTrace();
        }
	}
	
	//	look for user, check if pass correct
	public int authorize(String user, String pass){
		// go through all users, look for username
		User currentUser = null;
		for (User u : users){
			if (u.username.equals(user)){
				currentUser = u;
				break;
			}
		}
		
		// return 0 = invalid username
		if (currentUser == null)
			return 0;
		
		// determine type of users
		if (currentUser.password.equals(pass)){
			// return 2 = student 
			if (currentUser.isStudent)
				return 2;
			else // return 3 = teacher
				return 3;			
		} else	// return 1 = incorrect password
			return 1;
		
	}
}
