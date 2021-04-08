package com.app.wcc.controller;

import java.net.URISyntaxException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.wcc.controller.validator.PostalCodeValidator;
import com.app.wcc.controller.validator.UpdatePostalCodeValidator;
import com.app.wcc.domain.dto.PostalCodeCalcInfo;
import com.app.wcc.domain.dto.PostalCodeInfo;
import com.app.wcc.domain.dto.PostalInfoDto;
import com.app.wcc.service.PostalCodeService;

/**
 * @author Ananth Shanmugam
 * Primary controller to calculate the distance between 2 postalcode and to update existing postalcode
 */
@RestController
@RequestMapping("/admin")
public class PostalCodeController {

	private final static Logger LOGGER = LoggerFactory.getLogger(PostalCodeController.class);

	@Autowired
	private PostalCodeService postalCodeService;

	@Autowired
	private PostalCodeValidator postalCodeValidator;

	@Autowired
	private UpdatePostalCodeValidator updatePostalCodeValidator;

	@PostMapping("/distancecalc")
	@ResponseBody /* Return the response for distance between 2 postal codes */
	public ResponseEntity<PostalCodeCalcInfo> calculateDistanceBetweenPostalCodes(
			@Valid @RequestBody PostalInfoDto postalInfoDto, BindingResult bindingresult) {
		LOGGER.info("REST request to calculate distance between 2 PostalCode : {}", postalInfoDto);
		postalCodeValidator.validate(postalInfoDto, bindingresult);

		if (bindingresult.hasErrors()) {
			LOGGER.error("postaljson data is incorrect");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		} else {
			return ResponseEntity.ok(postalCodeService.calculateDistanceBetweenPoints(postalInfoDto));
		}
	}

	@PostMapping("/updatepostalcode")
	@ResponseBody
	public ResponseEntity<String> updatePostalCode(@Valid @RequestBody PostalCodeInfo postalCodeInfoDto,
			BindingResult bindingresult) throws URISyntaxException {
		LOGGER.info("REST request to update PostalCode : {}", postalCodeInfoDto);

		updatePostalCodeValidator.validate(postalCodeInfoDto, bindingresult);
		if (bindingresult.hasErrors()) {
			LOGGER.error("postaljson2 data is incorrect");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

		} else {
			Boolean ok = postalCodeService.updatePostalCode(postalCodeInfoDto);
			return new ResponseEntity("PostalCode updated ", ok ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
		}
	}
}
