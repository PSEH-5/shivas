package api.football;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.queryParam;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import api.football.common.CommonUtil;
import api.football.constants.Constants;
import api.football.controller.Controller;
import api.football.dataobjects.FetchTeamStandingsResponse;
import api.football.exception.ClientAPIException;
import api.football.exception.NoDataFoundException;

@SpringBootTest
public class FetchStandingsTest extends BaseJunit {

	@Autowired
	private Controller controller;
	
	@Test
	public void test() throws IOException, ClientAPIException, NoDataFoundException {
		
		mockServer.expect(queryParam(Constants.REQUEST_PARAM_ACTION, Constants.ACTION_GET_COUNTRIES))
		.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
		.andRespond(MockRestResponseCreators.withSuccess(getDataFromResources("mocks/countries.json"), MediaType.APPLICATION_JSON));
		
		mockServer.expect(queryParam(Constants.REQUEST_PARAM_ACTION, Constants.ACTION_GET_LEAGUES))
		.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
		.andRespond(MockRestResponseCreators.withSuccess(getDataFromResources("mocks/leagues.json"), MediaType.APPLICATION_JSON));
		
		mockServer.expect(queryParam(Constants.REQUEST_PARAM_ACTION, Constants.ACTION_GET_STANDINGS))
		.andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
		.andRespond(MockRestResponseCreators.withSuccess(getDataFromResources("mocks/standings.json"), MediaType.APPLICATION_JSON));
		
		ResponseEntity<FetchTeamStandingsResponse> actualResponse = controller.standings("Canada", "Indian Team", "Premiere League");

		mockServer.verify();
		
		String actualResponseString = CommonUtil.convertObjectToString(actualResponse.getBody());
		String expectedResponseString = getDataFromResources("response/fetch_standings_response.json");

		assertEquals(expectedResponseString, actualResponseString);
	}

}
