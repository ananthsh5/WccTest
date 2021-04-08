package com.app.wcc.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ananth Shanmugam
 * Class to hold the postal code info for distance calculation
 */
@Data
@NoArgsConstructor
public class PostalInfoDto {

	private String postalCode1;
	
	private String postalCode2;

}
