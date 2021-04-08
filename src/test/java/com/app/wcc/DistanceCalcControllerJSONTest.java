package com.app.wcc;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.app.wcc.domain.dto.PostalCodeCalcInfo;
import com.app.wcc.domain.dto.PostalInfoDto;

/**
 * @author Ananth Shanmugam Class to do the primary junit test
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@PropertySource("classpath:application.properties ")
public class DistanceCalcControllerJSONTest 
{
    @Autowired
    private TestRestTemplate restTemplate;
    

    @LocalServerPort
    int randomServerPort;
    
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "password";


    private PostalInfoDto getPostalInfoDtoTestData() {
    	
		PostalInfoDto postalInfoDto = new PostalInfoDto();
		postalInfoDto.setPostalCode1("AB10 1XG");
		postalInfoDto.setPostalCode2("AB21 7NR");
		return postalInfoDto;
    }

    private PostalInfoDto getInvalidPostalInfoDtoTestData() {
    	
		PostalInfoDto postalInfoDto = new PostalInfoDto();
		postalInfoDto.setPostalCode1("AB10 1XG");
		//postalInfoDto.setPostalCode2("AB21 7NR");
		return postalInfoDto;
    }
    
    //Normal flow test
    @Test
    public void testCalculateDistance() throws URISyntaxException 
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/admin/distancecalc/";
        URI uri = new URI(baseUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");      

        HttpEntity<PostalInfoDto> request = new HttpEntity<>(getPostalInfoDtoTestData(), headers);
        
        ResponseEntity<PostalCodeCalcInfo> result = this.restTemplate.withBasicAuth(USERNAME, PASSWORD)
        		.postForEntity(uri, request, PostalCodeCalcInfo.class);

        System.out.println("result.getStatusCodeValue() "+result.getStatusCodeValue());
        assertEquals(200,result.getStatusCodeValue());
		PostalCodeCalcInfo postalCodeCalcInfo = result.getBody();
        assertEquals(8.853686124741886,postalCodeCalcInfo.getDistance());
        assertEquals("KM",postalCodeCalcInfo.getUnit());
        assertEquals("AB10 1XG",postalCodeCalcInfo.getLocationInfo1().getPostalCode());
        assertEquals("AB21 7NR",postalCodeCalcInfo.getLocationInfo2().getPostalCode());
    }
    
   //Exceptional flow test with invalid test data sent to controller
    @Test
    public void testInvalidData() throws URISyntaxException 
    {
		
        final String baseUrl = "http://localhost:"+randomServerPort+"/admin/distancecalc/";
        URI uri = new URI(baseUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");      

        HttpEntity<PostalInfoDto> request = new HttpEntity<>(getInvalidPostalInfoDtoTestData(), headers);
        
        ResponseEntity<PostalCodeCalcInfo> result = this.restTemplate.withBasicAuth(USERNAME, PASSWORD)
        		.postForEntity(uri, request, PostalCodeCalcInfo.class);

        System.out.println("testInvalidData result.getStatusCodeValue() "+result.getStatusCodeValue());
        assertEquals(400,result.getStatusCodeValue());
    }

}
