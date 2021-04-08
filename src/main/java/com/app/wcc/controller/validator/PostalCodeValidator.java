package com.app.wcc.controller.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.app.wcc.controller.PostalCodeController;
import com.app.wcc.domain.dto.PostalInfoDto;

/**
 * @author Ananth Shanmugam Class to validate distance postal code data for calculation
 */
@Component
public class PostalCodeValidator implements Validator {

	private final static Logger LOGGER = LoggerFactory.getLogger(PostalCodeController.class);

	private static final String POSTALCODE1_IS_INVALID = "POSTALCODE 1 IS INVALID";
	private static final String POSTALCODE2_IS_INVALID = "POSTALCODE 2 IS INVALID";
	

	@Override
	public boolean supports(Class<?> clazz) {
		return PostalInfoDto.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		PostalInfoDto postalInfo = (PostalInfoDto) target;
		if (!validatePostalCode(postalInfo.getPostalCode1())) {
			errors.rejectValue("postalCode1", "POSTCODE001", POSTALCODE1_IS_INVALID);
		}
		if (!validatePostalCode(postalInfo.getPostalCode2())) {
			errors.rejectValue("postalCode2", "POSTCODE002", POSTALCODE2_IS_INVALID);
		}
	}
	
	private boolean validatePostalCode(String postalCode) {
		if(postalCode == null || postalCode.trim().length()==0)
		{
			return false;
		}
		return true;
	}

}
