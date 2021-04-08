package com.app.wcc.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.wcc.domain.PostCodeDetail;
import com.app.wcc.domain.PostCodeHistory;
import com.app.wcc.domain.dto.PostalCodeCalcInfo;
import com.app.wcc.domain.dto.PostalCodeInfo;
import com.app.wcc.domain.dto.PostalInfoDto;
import com.app.wcc.repository.PostCodeDetailRepository;
import com.app.wcc.repository.PostCodeHistoryRepository;
import com.app.wcc.service.PostalCodeService;

/**
 * @author Ananth Shanmugam
 * Class to hold primary service methods for distance postal code calculation and postal code update
 */
@Service
public class PostalCodeServiceImpl implements PostalCodeService  {

    @Autowired
    private PostCodeDetailRepository postCodeDetailRepository;
    @Autowired
    private PostCodeHistoryRepository postCodeHistoryRepository;
    
	public PostalCodeCalcInfo calculateDistanceBetweenPoints(PostalInfoDto postalInfoDto) {

		PostalCodeCalcInfo postalCodeCalcInfo = new PostalCodeCalcInfo();

		PostalCodeInfo locationInfo1 = new PostalCodeInfo();
		
		PostCodeDetail postCodeDetail1 = postCodeDetailRepository.findPostCodeDetailByPostcode(postalInfoDto.getPostalCode1());
		PostCodeDetail postCodeDetail2 = postCodeDetailRepository.findPostCodeDetailByPostcode(postalInfoDto.getPostalCode2());
		
		double distance = calculateDistance(Double.parseDouble(postCodeDetail1.getLatitude().toString()), Double.parseDouble(postCodeDetail1.getLongitude().toString()), Double.parseDouble(postCodeDetail2.getLatitude().toString()), Double.parseDouble(postCodeDetail2.getLongitude().toString()));
		locationInfo1.setPostalCode(postalInfoDto.getPostalCode1());
		PostalCodeInfo locationInfo2 = new PostalCodeInfo();
		locationInfo2.setPostalCode(postalInfoDto.getPostalCode2());
		
		postalCodeCalcInfo.setLocationInfo1(locationInfo1);
		postalCodeCalcInfo.setLocationInfo2(locationInfo2);
		postalCodeCalcInfo.setDistance(distance);
		
		auditPostalCode(postalInfoDto);
		
		return postalCodeCalcInfo;
	}

	private PostCodeHistory auditPostalCode(PostalInfoDto postalInfoDto) {
		PostCodeHistory postCodeHistory = new PostCodeHistory();
		postCodeHistory.setPostcode1(postalInfoDto.getPostalCode1());
		postCodeHistory.setPostcode2(postalInfoDto.getPostalCode2());
		return postCodeHistoryRepository.save(postCodeHistory);
	}
	public Boolean updatePostalCode(PostalCodeInfo postalCodeInfoDto) {
		
		PostCodeDetail postCodeDetail = postCodeDetailRepository.findPostCodeDetailByPostcode(postalCodeInfoDto.getPostalCode());
		postCodeDetail.setLatitude(new BigDecimal(new Double(postalCodeInfoDto.getLatitude()).toString()));
		postCodeDetail.setLongitude(new BigDecimal(new Double(postalCodeInfoDto.getLongitude()).toString()));
		postCodeDetailRepository.save(postCodeDetail);
		return true;
	}
	private final static double EARTH_RADIUS = 6371; // radius in kilometers
	private double calculateDistance(double latitude, double longitude, double latitude2, double longitude2) {
		// Using Haversine formula! See Wikipedia;
		double lon1Radians = Math.toRadians(longitude);
		double lon2Radians = Math.toRadians(longitude2);
		double lat1Radians = Math.toRadians(latitude);
		double lat2Radians = Math.toRadians(latitude2);
		double a = haversine(lat1Radians, lat2Radians)
				+ Math.cos(lat1Radians) * Math.cos(lat2Radians) * haversine(lon1Radians, lon2Radians);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return (EARTH_RADIUS * c);
	}

	private double haversine(double deg1, double deg2) {
		return square(Math.sin((deg1 - deg2) / 2.0));
	}

	private double square(double x) {
		return x * x;
	}
}
