package finalproject;

public class Username {
	private String username;
	private String date;
	
	Username(String username,String date){
		this.date = date;
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	
}
