/**
 * 
 */
package exception;

import java.awt.TrayIcon.MessageType;

/**
 * @author alibi
 *
 */
public class FieldValidationError {

	private String filed;
	private String message;
	private MessageType type; // SUCCESS, INFO, WARNING, ERROR
	
	public String getFiled() {
		return filed;
	}
	public void setFiled(String filed) {
		this.filed = filed;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public MessageType getType() {
		return type;
	}
	public void setType(MessageType type) {
		this.type = type;
	}
	
	
}
