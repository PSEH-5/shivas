package api.football.common;

import static api.football.constants.Constants.CLIENT_API_RESPONSE_NULL;
import static api.football.constants.Constants.NO_DATA_RETURNED_BY_CLIENT_API;
import static api.football.constants.Constants.REQUEST_PARAM_ACTION;
import static api.football.constants.Constants.REQUEST_PARAM_API_KEY;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import api.football.exception.ClientAPIException;
import api.football.exception.NoDataFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public final class CommonUtil {

	private CommonUtil() {

	}

	private static String apiKey;
	private static String baseURL;

	@Value("${API_KEY}")
	public void setApiKey(String configuredAPIKey) {
		apiKey = configuredAPIKey;
	}

	@Value("${BASE_URL}")
	public void setBaseURL(String configuredBaseURL) {
		baseURL = configuredBaseURL;
	}

	public static URI getURI(UriComponentsBuilder uriComponentsBuilder) {

		if (uriComponentsBuilder == null) {
			log.error("Invalid URI details !!");
			return null;
		}
		return uriComponentsBuilder.build().toUri();
	}

	public static boolean isValidURI(UriComponentsBuilder uriComponentsBuilder) {

		if (uriComponentsBuilder == null) {
			log.error("Invalid URI details !!");
			return false;
		}

		return true;
	}

	public static String convertObjectToString(Object json) throws JsonProcessingException {

		if (json == null) {
			return null;
		}

		return new ObjectMapper().writeValueAsString(json);
	}

	public static UriComponentsBuilder getBaseURI() {
		return UriComponentsBuilder.fromHttpUrl(baseURL);
	}

	public static MultiValueMap<String, String> getBaseQueryParameters(String actionName) {

		MultiValueMap<String, String> queryParameters = new LinkedMultiValueMap<>();
		queryParameters.add(REQUEST_PARAM_ACTION, actionName);
		queryParameters.add(REQUEST_PARAM_API_KEY, apiKey);

		return queryParameters;
	}

	public static <T> void validateResponse(ResponseEntity<T> response, String serviceName)
			throws NoDataFoundException, ClientAPIException {

		if (response == null) {
			throw new ClientAPIException(CLIENT_API_RESPONSE_NULL, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		if (response.getStatusCode() == HttpStatus.NO_CONTENT) {
			log.error(NO_DATA_RETURNED_BY_CLIENT_API + " " + serviceName);
			throw new NoDataFoundException(NO_DATA_RETURNED_BY_CLIENT_API + " " + serviceName);
		}
	}
}
