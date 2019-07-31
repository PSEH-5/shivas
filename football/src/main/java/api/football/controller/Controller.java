package api.football.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import api.football.dataobjects.FetchTeamStandingsResponse;
import api.football.exception.ClientAPIException;
import api.football.exception.NoDataFoundException;
import api.football.processor.FetchTeamStandingsProcessor;

@RestController
@RequestMapping("/api/football")
public class Controller {

	@Autowired
	FetchTeamStandingsProcessor fetchTeamStandingProcessor;

	@GetMapping(path = "/standing", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<FetchTeamStandingsResponse> standings(@RequestParam(name = "country_name") String countryName,
			@RequestParam(name = "team_name") String teamName, @RequestParam(name = "league_name") String leagueName

	) throws ClientAPIException, NoDataFoundException {

		FetchTeamStandingsResponse response = fetchTeamStandingProcessor.fetchTeamStandings(countryName, teamName,
				leagueName);

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
	}
}
