package api.football;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

import api.football.common.CommonUtil;

public class BaseJunit {

	@Autowired
	private RestTemplate restTemplate;

	protected MockRestServiceServer mockServer;
	 
    @BeforeEach
    public void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }
    
	private static String RESOURCES_PATH = "src/test/resources/";

	protected String getDataFromResources(String filePath) throws IOException {

		byte[] bytes = getBytes(filePath);

		if (bytes == null || bytes.length == 0) {
			return null;
		}

		return new String(bytes);
	}

	private byte[] getBytes(String filePath) throws IOException {

		File file = new File(RESOURCES_PATH + filePath);
		return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
	}
}
