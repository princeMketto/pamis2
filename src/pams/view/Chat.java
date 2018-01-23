package pams.view;

import javafx.beans.property.SimpleStringProperty;

public class Chat {
	private SimpleStringProperty message;
	private SimpleStringProperty sender;
	private SimpleStringProperty receiver;
	public Chat(String message, String sender) {
		super();
		this.message = new SimpleStringProperty(message);
		this.sender = new SimpleStringProperty(sender);
		//this.receiver = new SimpleStringProperty(receiver);
	}
	public String getMessage() {
		return message.get();
	}

	public String getSender() {
		return sender.get();
	}

	public String getReceiver() {
		return receiver.get();
	}

	

}
