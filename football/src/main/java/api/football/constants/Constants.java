package api.football.constants;

public final class Constants {

	private Constants() {
	}
	
	// Symbols
	public static final String DELIMITER_HYPHEN = "-";
	public static final String OPEN_BRACKET = "(";
	public static final String CLOSE_BRACKET = ")";
	public static final String SPACE = " ";

	// Error Constants
	public static final String INVALID_URI_DETAILS = "Invalid URI details !!";
	public static final String CLIENT_API_RESPONSE_NULL = "Null response returned by client API";
	public static final String EMPTY_OR_NULL_COUNTRY_LIST = "Country list empty or null !!";
	public static final String COUNTRY_NOT_FOUND = "No country found with specified name";
	public static final String INVALID_COUNTRY_ID = "Country Id found as null or empty for specified country name";
	public static final String NO_LEAGUES_FOUND_FOR_COUNTRY_ID = "No leagues found for specified country Id";
	public static final String LEAGUE_NOT_FOUND_WITH_SPECIFIED_LEAGUE_NAME = "No League found with specified league name";
	public static final String INVALID_LEAGUE_ID = "League id found as null or empty for specified league name";
	public static final String NO_STANDINGS_FOUND_FOR_LEAGUE_ID = "No standings found for specified league Id";
	public static final String NO_STANDINGS_FOUND_FOR_SPECIFIED_SET_OF_PARAMETERS = "No standings found for requested set of parameters";
	public static final String CLIENT_API_RETURNED_BAD_REQUEST = "400 Error code returned by client API";
	public static final String CLIENT_API_RETURNED_BAD_GATEWAY = "502 Error code returned by client API";
	public static final String CLIENT_API_RETURNED_NO_CONTENT = "204 status code returned by client API";
	public static final String NO_DATA_RETURNED_BY_CLIENT_API = "No data returned by client API";

	// Client Constants
	public static final String REQUEST_PARAM_API_KEY = "APIkey";
	public static final String REQUEST_PARAM_ACTION = "action";
	public static final String REQUEST_PARAM_COUNTRY_ID = "country_id";
	public static final String REQUEST_PARAM_LEAGUE_ID = "league_id";
	public static final String ACTION_GET_COUNTRIES = "get_countries";
	public static final String ACTION_GET_LEAGUES = "get_leagues";
	public static final String ACTION_GET_STANDINGS = "get_standings";

	// API Constants
	public static final String COUNTRY_ID_AND_NAME = "Country ID & Name";
	public static final String LEAGUE_ID_AND_NAME = "League ID & Name";
	public static final String TEAM_ID_AND_NAME = "Team ID & Name";
	public static final String OVERALL_LEAGUE_POSITION = "Overall League Position";
	public static final int STATUS_CODE_NO_DATA_FOUND = 207;

}
