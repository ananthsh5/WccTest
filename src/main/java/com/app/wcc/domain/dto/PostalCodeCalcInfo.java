package com.app.wcc.domain.dto;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 * Class to hold the response for postal code info after calculation
 */
@Data
public class PostalCodeCalcInfo {

	private PostalCodeInfo locationInfo1;
	private PostalCodeInfo locationInfo2;
	private Double distance;
	private final String unit = "KM";
}
