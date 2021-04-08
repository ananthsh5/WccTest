package com.app.wcc.service;

import com.app.wcc.domain.dto.PostalInfoDto;

import javax.validation.Valid;

import com.app.wcc.domain.dto.PostalCodeCalcInfo;
import com.app.wcc.domain.dto.PostalCodeInfo;

/**
 * @author Ananth Shanmugam
 * Service interface for this app
 */
public interface PostalCodeService {
	PostalCodeCalcInfo calculateDistanceBetweenPoints(PostalInfoDto postalInfoDto);

	Boolean updatePostalCode(@Valid PostalCodeInfo postalCodeInfoDto);
}
