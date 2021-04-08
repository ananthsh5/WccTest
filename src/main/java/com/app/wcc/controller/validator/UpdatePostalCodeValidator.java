package com.app.wcc.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.wcc.controller.PostalCodeController;
import com.app.wcc.domain.dto.PostalCodeInfo;

/**
 * @author Ananth Shanmugam Class to validate update postal code data
 */
@Component
public class UpdatePostalCodeValidator implements Validator {

	private final static Logger LOGGER = LoggerFactory.getLogger(PostalCodeController.class);

	private static final String POSTALCODE_IS_INVALID = "POSTALCODE IS INVALID";

	private static final String LATITUDE_IS_INVALID = "LATITUDE IS INVALID";

	private static final String LONGITUDE_IS_INVALID = "LONGITUDE IS INVALID";

	@Override
	public boolean supports(Class<?> clazz) {
		return PostalCodeInfo.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PostalCodeInfo postalCodeInfo = (PostalCodeInfo) target;
		if(postalCodeInfo ==null) {
			errors.rejectValue("postalCode", "POSTCODE", POSTALCODE_IS_INVALID);
		}
		if (!validatePostalCode(postalCodeInfo.getPostalCode())) {
			errors.rejectValue("postalCode", "POSTCODE", POSTALCODE_IS_INVALID);
		}
		if(!validatePointField(postalCodeInfo.getLatitude())) {
			errors.rejectValue("latitude", "LATITUDE", LATITUDE_IS_INVALID);
		}
		if(!validatePointField(postalCodeInfo.getLongitude())) {
			errors.rejectValue("longitude", "LONGITUDE", LONGITUDE_IS_INVALID);
		}

	}

	private boolean validatePostalCode(String postalCode) {
		if (postalCode == null || postalCode.trim().length() == 0) {
			return false;
		}
		return true;
	}

	private boolean validatePointField(double pointField) {
		if (pointField == 0) {
			return false;
		}
		return true;
	}

}
