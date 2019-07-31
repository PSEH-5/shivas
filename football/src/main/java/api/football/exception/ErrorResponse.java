package api.football.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {

	@JsonInclude(value = Include.NON_NULL)
	private Integer errorCode;
	private String message;

	public ErrorResponse(String message) {
		this.message = message;
	}
}
