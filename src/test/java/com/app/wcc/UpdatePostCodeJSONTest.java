package com.app.wcc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
import com.app.wcc.domain.dto.PostalCodeInfo;
import com.app.wcc.domain.dto.PostalInfoDto;


/**
 * @author Ananth Shanmugam Class to do the update junit test
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@PropertySource("classpath:application.properties ")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UpdatePostCodeJSONTest 
{
	private static final String USERNAME = "admin";
	private static final String PASSWORD = "password";

    @Autowired
    private TestRestTemplate restTemplate;
    
    @LocalServerPort
    int randomServerPort;

    //Test data for update postal code info
    private PostalCodeInfo getPostalCodeInfoTestData() {
    	
    	PostalCodeInfo postalInfoDto = new PostalCodeInfo();
		postalInfoDto.setPostalCode("AB10 1XG");
		postalInfoDto.setLatitude(1);
		postalInfoDto.setLongitude(1);
		
		return postalInfoDto;
    }
    
    //Test data for calculate distance controller
    private PostalInfoDto getPostalInfoDtoTestData() {
    	
		PostalInfoDto postalInfoDto = new PostalInfoDto();
		postalInfoDto.setPostalCode1("AB10 1XG");
		postalInfoDto.setPostalCode2("AB21 7NR");
		return postalInfoDto;
    }

    //Normal flow test with valid test data sent to controller for postal code update    
    @Test
    @Order(value = 0)
    public void testUpdatePostalCode() throws URISyntaxException 
    {
        final String baseUrl = "http://localhost:"+randomServerPort+"/admin/updatepostalcode/";
        URI uri = new URI(baseUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");      

        HttpEntity<PostalCodeInfo> request = new HttpEntity<>(getPostalCodeInfoTestData(), headers);
        
        ResponseEntity<String> result = this.restTemplate.withBasicAuth(USERNAME,PASSWORD)
        		.postForEntity(uri, request, String.class);

        assertEquals("PostalCode updated ",result.getBody());
        assertEquals(200,result.getStatusCodeValue());
    }
    
    //Normal flow test with valid test data sent to controller for postal code verification that update worked!   
    @Test
    @Order(value = 1)
    public void testCalculateDistanceAfterUpdate() throws URISyntaxException 
    {
		String username = "admin";
		String password = "password";
		
        final String baseUrl = "http://localhost:"+randomServerPort+"/admin/distancecalc/";
        URI uri = new URI(baseUrl);
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");      

        HttpEntity<PostalInfoDto> request = new HttpEntity<>(getPostalInfoDtoTestData(), headers);
        
        ResponseEntity<PostalCodeCalcInfo> result = this.restTemplate.withBasicAuth(username, password)
        		.postForEntity(uri, request, PostalCodeCalcInfo.class);

        assertEquals(200,result.getStatusCodeValue());
		PostalCodeCalcInfo postalCodeCalcInfo = result.getBody();
        System.out.println("After Update: postalCodeCalcInfo.getDistance() "+postalCodeCalcInfo.getDistance());

        assertNotEquals(8.853686124741886,postalCodeCalcInfo.getDistance());
        assertEquals("AB10 1XG",postalCodeCalcInfo.getLocationInfo1().getPostalCode());
        assertEquals("AB21 7NR",postalCodeCalcInfo.getLocationInfo2().getPostalCode());
    }

}
