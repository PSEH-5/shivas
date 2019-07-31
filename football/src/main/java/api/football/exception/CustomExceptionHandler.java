package api.football.exception;

import static api.football.constants.Constants.CLIENT_API_RETURNED_BAD_GATEWAY;
import static api.football.constants.Constants.CLIENT_API_RETURNED_BAD_REQUEST;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import api.football.constants.Constants;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ClientAPIException.class)
	protected ResponseEntity<ErrorResponse> handleClientAPIException(ClientAPIException exception) {

		ErrorResponse errorResponse = new ErrorResponse(exception.getErrorCode(), exception.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleAllExceptions(Exception exception) {

		ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage());
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoDataFoundException(NoDataFoundException exception) {
		
		ErrorResponse errorResponse = new ErrorResponse(exception.getMessage());
		return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(Constants.STATUS_CODE_NO_DATA_FOUND));
	}

	@ExceptionHandler(RestClientResponseException.class)
	public ResponseEntity<ErrorResponse> handleRestClientResponseException(RestClientResponseException exception) {

		if (exception.getRawStatusCode() == HttpStatus.BAD_REQUEST.value()) {
			ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), CLIENT_API_RETURNED_BAD_REQUEST);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

		} else if (exception.getRawStatusCode() == HttpStatus.BAD_GATEWAY.value()) {
			ErrorResponse response = new ErrorResponse(HttpStatus.BAD_GATEWAY.value(), CLIENT_API_RETURNED_BAD_GATEWAY);
			return new ResponseEntity<>(response, HttpStatus.BAD_GATEWAY);

		} else {

			ErrorResponse response = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					exception.getMessage());
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
