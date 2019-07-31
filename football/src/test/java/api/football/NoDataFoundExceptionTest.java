package api.football;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import api.football.constants.Constants;
import api.football.constants.Constants;
import api.football.exception.ClientAPIException;
import api.football.exception.NoDataFoundException;
import api.football.processor.FetchTeamStandingsProcessor;

@SpringBootTest
public class NoDataFoundExceptionTest extends BaseJunit {

	@Autowired
	private FetchTeamStandingsProcessor fetchTeamStandingsProcessor;

	@Test
	public void noCountriesTest() throws NoDataFoundException, ClientAPIException {

		mockServer.expect(queryParam(Constants.REQUEST_PARAM_ACTION, Constants.ACTION_GET_COUNTRIES))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).andRespond(MockRestResponseCreators
						.withSuccess());

		
		NoDataFoundException exception = Assertions.assertThrows(NoDataFoundException.class, () -> {
			fetchTeamStandingsProcessor.getCountryId("test");
		}, "test");

		mockServer.verify();
		assertEquals(Constants.EMPTY_OR_NULL_COUNTRY_LIST, exception.getMessage());
	}

	@Test
	public void noLeaguesFoundTest() {

		mockServer.expect(queryParam(Constants.REQUEST_PARAM_ACTION, Constants.ACTION_GET_LEAGUES))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).andRespond(MockRestResponseCreators
						.withSuccess());

		Assertions.assertThrows(NoDataFoundException.class, () -> {
			fetchTeamStandingsProcessor.getLeagueId("test1", "test2");
		}, "test");
		
		mockServer.verify();

	}

	@Test
	public void noStandingsFoundTest() {

		mockServer.expect(queryParam(Constants.REQUEST_PARAM_ACTION, Constants.ACTION_GET_STANDINGS))
				.andExpect(MockRestRequestMatchers.method(HttpMethod.GET)).andRespond(MockRestResponseCreators
						.withSuccess());

		
		Assertions.assertThrows(NoDataFoundException.class, () -> {
			fetchTeamStandingsProcessor.getStanding("test_country","test_team", "test_league", "test_league_id");
		}, "test");

		mockServer.verify();
	}

}
