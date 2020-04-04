package exception;

import com.apress.ravi.dto.UsersDTO;

public class CustomErrorType extends UsersDTO {

	private String errorMessage;

	public CustomErrorType(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	public String getErrorMessage() {
		return errorMessage;
	}
	
}
