package com.app.wcc.domain.dto;

import lombok.Data;

/**
 * @author Ananth Shanmugam
 * Class to hold the postal code info for update
 */
@Data
public class PostalCodeInfo {

	private double latitude;
	
	private double longitude;

	private String postalCode;

}
