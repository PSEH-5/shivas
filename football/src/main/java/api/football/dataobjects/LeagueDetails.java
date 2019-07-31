package api.football.dataobjects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class LeagueDetails {

	@JsonProperty("country_id")
	private String countryId;
	
	@JsonProperty("league_id")
	private String leagueId;
	
	@JsonProperty("league_name")
	private String leagueName;
}
