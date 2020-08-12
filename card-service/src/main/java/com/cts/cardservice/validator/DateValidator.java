package com.cts.cardservice.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateValidator implements ConstraintValidator<DateConstraint, String> {

	@Value("${card.dateformat}")
	private String dateFormat;

	@Override
	public void initialize(DateConstraint customDate) {
		// do nothing
	}

	@Override
	public boolean isValid(String customDateField, ConstraintValidatorContext cxt) {
		if (null == customDateField) {
			return false;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		sdf.setLenient(false);
		try {
			// if not valid, it will throw ParseException
			Date date = sdf.parse(customDateField);
			log.info("Date - "+date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
