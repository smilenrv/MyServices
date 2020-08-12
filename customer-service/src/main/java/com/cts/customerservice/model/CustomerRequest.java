package com.cts.customerservice.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "All details about the customer.")
public class CustomerRequest {

	@ApiModelProperty(notes = "Name should have alphanumeric of max 20 characters", required = true)
	@NotBlank(message = "name.empty")
	@Pattern(regexp = "^[a-zA-Z0-9]{1,20}$", message = "name.notvalid")
	private String name;

	@ApiModelProperty(notes = "Age should be 1-2 digits", required = true)
	@NotBlank(message = "age.empty")
	@Pattern(regexp = "^[0-9]{1,2}$", message = "age.notvalid")
	private String age;

	@ApiModelProperty(notes = "Email address of customer", required = true)
	@NotBlank(message = "email.empty")
	@Email(message = "email.notvalid")
	private String emailAddress;

	@ApiModelProperty(notes = "Name should have alphanumeric with space of max 100 characters", required = true)
	@NotBlank(message = "address.empty")
	@Pattern(regexp = "^[a-zA-Z0-9 ]{1,100}$", message = "address.notvalid")
	private String address;

	@ApiModelProperty(notes = "Phone should have only numeric of max 10 digits", required = true)
	@NotBlank(message = "phone.empty")
	@Pattern(regexp = "^[0-9]{1,10}$", message = "phone.notvalid")
	private String phoneNumber;

}
