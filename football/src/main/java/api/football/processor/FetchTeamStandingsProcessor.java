package api.football.processor;

import static api.football.constants.Constants.COUNTRY_NOT_FOUND;
import static api.football.constants.Constants.EMPTY_OR_NULL_COUNTRY_LIST;
import static api.football.constants.Constants.INVALID_COUNTRY_ID;
import static api.football.constants.Constants.INVALID_LEAGUE_ID;
import static api.football.constants.Constants.LEAGUE_NOT_FOUND_WITH_SPECIFIED_LEAGUE_NAME;
import static api.football.constants.Constants.NO_LEAGUES_FOUND_FOR_COUNTRY_ID;
import static api.football.constants.Constants.NO_STANDINGS_FOUND_FOR_LEAGUE_ID;
import static api.football.constants.Constants.NO_STANDINGS_FOUND_FOR_SPECIFIED_SET_OF_PARAMETERS;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import api.football.constants.Constants;
import api.football.dataobjects.CountryDetails;
import api.football.dataobjects.FetchTeamStandingsResponse;
import api.football.dataobjects.LeagueDetails;
import api.football.dataobjects.StandingDetails;
import api.football.exception.ClientAPIException;
import api.football.exception.NoDataFoundException;
import api.football.proxy.ClientProxy;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class FetchTeamStandingsProcessor {

	@Autowired
	ClientProxy clientProxy;

	public FetchTeamStandingsResponse fetchTeamStandings(String countryName, String teamName, String leagueName)
			throws NoDataFoundException, ClientAPIException {

		String countryId = getCountryId(countryName);
		String leagueId = getLeagueId(countryId, leagueName);
		StandingDetails standing = getStanding(countryName, teamName, leagueName, leagueId);

		return populateResponse(countryId, leagueId, standing);
	}

	private FetchTeamStandingsResponse populateResponse(String countryId, String leagueId, StandingDetails standing) {

		FetchTeamStandingsResponse response = new FetchTeamStandingsResponse();
		
		response.setCountryDetails(getFormattedValue(countryId, standing.getCountryName()));
		response.setLeagueDetails(getFormattedValue(leagueId, standing.getLeagueName()));
		response.setTeamDetails(getFormattedValue(standing.getTeamId(), standing.getTeamName()));
		response.setOverallLeaguePosition(standing.getOverallLeaguePosition());

		return response;
	}

	public String getCountryId(String countryName) throws NoDataFoundException, ClientAPIException {

		List<CountryDetails> countryList = clientProxy.getCountries();

		if (CollectionUtils.isEmpty(countryList)) {
			log.error(EMPTY_OR_NULL_COUNTRY_LIST);
			throw new NoDataFoundException(EMPTY_OR_NULL_COUNTRY_LIST);
		}

		Optional<CountryDetails> optional = countryList.stream()
				.filter(country -> StringUtils.equals(country.getCountryName(), countryName)).findAny();

		if (optional.isPresent()) {

			String countryId = optional.get().getCountryId();

			if (StringUtils.isBlank(countryId)) {
				log.error(INVALID_COUNTRY_ID + " " + countryName);
				throw new NoDataFoundException(INVALID_COUNTRY_ID + " " + countryName);
			}

			return countryId;
		} else {
			log.error(COUNTRY_NOT_FOUND + " " + countryName);
			throw new NoDataFoundException(COUNTRY_NOT_FOUND + " " + countryName);

		}
	}

	public String getLeagueId(String countryId, String leagueName) throws ClientAPIException, NoDataFoundException {
		List<LeagueDetails> leagues = clientProxy.getLeaguesForCountry(countryId);

		if (CollectionUtils.isEmpty(leagues)) {
			log.error(NO_LEAGUES_FOUND_FOR_COUNTRY_ID + " " + countryId);
			throw new NoDataFoundException(NO_LEAGUES_FOUND_FOR_COUNTRY_ID + " " + countryId);
		}

		Optional<LeagueDetails> optional = leagues.stream()
				.filter(league -> StringUtils.equals(league.getCountryId(), countryId)
						&& StringUtils.equals(league.getLeagueName(), leagueName))
				.findFirst();

		if (optional.isPresent()) {

			String leagueId = optional.get().getLeagueId();

			if (StringUtils.isBlank(leagueId)) {
				log.error(INVALID_LEAGUE_ID + " " + leagueName);
				throw new NoDataFoundException(INVALID_LEAGUE_ID + " " + leagueName);
			}

			return leagueId;

		} else {
			log.error(LEAGUE_NOT_FOUND_WITH_SPECIFIED_LEAGUE_NAME + " " +  leagueName);
			throw new NoDataFoundException(LEAGUE_NOT_FOUND_WITH_SPECIFIED_LEAGUE_NAME + " " +  leagueName);

		}
	}

	public StandingDetails getStanding(String countryName, String teamName, String leagueName, String leagueId)
			throws ClientAPIException, NoDataFoundException {

		List<StandingDetails> standingsList = clientProxy.getStandingsForLeagueId(leagueId);

		if (CollectionUtils.isEmpty(standingsList)) {
			log.error(NO_STANDINGS_FOUND_FOR_LEAGUE_ID + " " + leagueId);
			throw new NoDataFoundException(NO_STANDINGS_FOUND_FOR_LEAGUE_ID + " " + leagueId);
		}

		Optional<StandingDetails> optional = standingsList.stream()
				.filter(standing -> StringUtils.equals(standing.getTeamName(), teamName)
						&& StringUtils.equals(standing.getCountryName(), countryName)
						&& StringUtils.equals(standing.getLeagueName(), leagueName))
				.findFirst();

		if (optional.isPresent()) {

			return optional.get();

		} else {

			log.error(NO_STANDINGS_FOUND_FOR_SPECIFIED_SET_OF_PARAMETERS + " " + "countryName = " + countryName + " , teamName = " + teamName + " , leagueName = " + leagueName);
			throw new NoDataFoundException(NO_STANDINGS_FOUND_FOR_SPECIFIED_SET_OF_PARAMETERS + " " + "countryName = " + countryName + " , teamName = " + teamName + " , leagueName = " + leagueName);
		}
	}

	public String getFormattedValue(String... values) {

		if (values == null || values.length == 0) {
			return null;
		}

		if (values.length == 1) {
			return values[0];
		}

		StringBuilder output = new StringBuilder();
		for (int i = 0; i < values.length; i++) {

			if (StringUtils.isBlank(output) && StringUtils.isNotBlank(values[i])) {
				output.append(Constants.OPEN_BRACKET).append(values[i]).append(Constants.CLOSE_BRACKET);
				continue;
			}
			output.append(Constants.DELIMITER_HYPHEN).append(values[i]);
		}

		return output.toString();
	}
}
