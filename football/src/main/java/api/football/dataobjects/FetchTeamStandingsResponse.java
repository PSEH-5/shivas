package api.football.dataobjects;

import com.fasterxml.jackson.annotation.JsonProperty;

import api.football.constants.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FetchTeamStandingsResponse {

	@JsonProperty(Constants.COUNTRY_ID_AND_NAME)
	private String countryDetails;
	
	@JsonProperty(Constants.TEAM_ID_AND_NAME)
	private String teamDetails;

	@JsonProperty(Constants.LEAGUE_ID_AND_NAME)
	private String leagueDetails;

	@JsonProperty(Constants.OVERALL_LEAGUE_POSITION)
	private String overallLeaguePosition;
}
