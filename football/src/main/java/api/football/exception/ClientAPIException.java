package api.football.exception;

import lombok.Getter;

public class ClientAPIException extends Exception {

	@Getter
	int errorCode;
	
	private static final long serialVersionUID = 1L;

	public ClientAPIException(String message, int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
}
