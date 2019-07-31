package api.football.proxy;

import static api.football.common.CommonUtil.getBaseQueryParameters;
import static api.football.common.CommonUtil.getBaseURI;
import static api.football.common.CommonUtil.validateResponse;
import static api.football.constants.Constants.ACTION_GET_COUNTRIES;
import static api.football.constants.Constants.ACTION_GET_LEAGUES;
import static api.football.constants.Constants.ACTION_GET_STANDINGS;
import static api.football.constants.Constants.REQUEST_PARAM_COUNTRY_ID;
import static api.football.constants.Constants.REQUEST_PARAM_LEAGUE_ID;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import api.football.common.CommonUtil;
import api.football.dataobjects.CountryDetails;
import api.football.dataobjects.LeagueDetails;
import api.football.dataobjects.StandingDetails;
import api.football.exception.APIErrorResponse;
import api.football.exception.ClientAPIException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ClientProxy {

	@Autowired
	private RestTemplate restTemplate;
	
	public List<CountryDetails> getCountries() throws ClientAPIException {

		UriComponentsBuilder uriComponentsBuilder = getBaseURI()
				.queryParams(getBaseQueryParameters(ACTION_GET_COUNTRIES));

		return invokeClientAPI(uriComponentsBuilder, ACTION_GET_COUNTRIES,
				new ParameterizedTypeReference<List<CountryDetails>>() {
				});
	}

	public List<StandingDetails> getStandingsForLeagueId(@NonNull String leagueId) throws ClientAPIException {

		UriComponentsBuilder uriComponentsBuilder = getBaseURI()
				.queryParams(getBaseQueryParameters(ACTION_GET_STANDINGS));

		if (CommonUtil.isValidURI(uriComponentsBuilder)) {
			uriComponentsBuilder.queryParam(REQUEST_PARAM_LEAGUE_ID, leagueId);
		}

		return invokeClientAPI(uriComponentsBuilder, ACTION_GET_STANDINGS,
				new ParameterizedTypeReference<List<StandingDetails>>() {
				});
	}

	public List<LeagueDetails> getLeaguesForCountry(String countryId) throws ClientAPIException {

		UriComponentsBuilder uriComponentsBuilder = getBaseURI()
				.queryParams(getBaseQueryParameters(ACTION_GET_LEAGUES));

		if (CommonUtil.isValidURI(uriComponentsBuilder)) {
			uriComponentsBuilder.queryParam(REQUEST_PARAM_COUNTRY_ID, countryId);
		}

		return invokeClientAPI(uriComponentsBuilder, ACTION_GET_LEAGUES,
				new ParameterizedTypeReference<List<LeagueDetails>>() {
				});
	}

	private <T> T invokeClientAPI(UriComponentsBuilder uriComponentsBuilder, String serviceName,
			ParameterizedTypeReference<T> classType) throws ClientAPIException {

		URI uri = CommonUtil.getURI(uriComponentsBuilder);

		if (uri == null) {
			log.error("{} service not invoked, hence returning reponse as null !!", serviceName);

		} else {

			log.info("Starting call to REST API : " + uri.toString());

			try {

				ResponseEntity<T> response = restTemplate.exchange(uri, HttpMethod.GET, null, classType);

				log.info("Completed call to REST API : " + uri.toString());

				validateResponse(response, serviceName);

				return response.getBody();

			} catch (Exception exception) {

				log.error("Exception Occured while invoking REST API {} : " + exception.getMessage(), serviceName);

				checkforAPIErrorResponse(uri, serviceName);
			}

		}
		return null;

	}

	private void checkforAPIErrorResponse(URI uri, String serviceName) throws ClientAPIException {

		try {

			log.info("Invoking API {} for validating API Error", serviceName);
			APIErrorResponse apiErrorResponse = restTemplate.getForObject(uri, APIErrorResponse.class);

			log.info("Completed call to API {} for validating API Error", serviceName);
			throw new ClientAPIException(apiErrorResponse.getMessage(), apiErrorResponse.getError());

		} catch (Exception exception) {
			throw exception;
		}
	}
}
