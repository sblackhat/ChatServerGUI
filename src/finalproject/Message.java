package finalproject;

public class Message {
	private String username;
	private String date;
	private String text;
	
	Message(String username,String date, String text){
		this.username = username;
		this.date = date;
		this.text = text;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
