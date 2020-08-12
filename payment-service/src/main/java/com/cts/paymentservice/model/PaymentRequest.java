package com.cts.paymentservice.model;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.cts.paymentservice.validator.DateConstraint;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "All details about the customer.")
public class PaymentRequest {

	@ApiModelProperty(notes = "Date and it should be in format MM/DD/YYYY", required = true)
	@NotBlank(message = "date.empty")
	@DateConstraint
	private String expirationDate;

	@ApiModelProperty(notes = "Card type should be VISA or MASTER or AMEX", required = true)
	@NotNull(message = "card.type.notvalid")
	private CardType cardType;
	
	@ApiModelProperty(notes = "Card number should be 16 digits", required = true)
	@NotNull(message = "card.number.empty")
	@Pattern(regexp = "^[0-9]{16}$", message = "card.number.notvalid")
	private String cardNumber;
	
	@ApiModelProperty(notes = "Status can be OPEN OR CLOSED OR FAILURE", required = true)
	@NotNull(message = "status.notvalid")
	private Status status;
	
	@ApiModelProperty(notes = "Amount can be 5 digit", required = true)
	@NotNull(message = "amount.empty")
	@Digits(integer=5, fraction=0)
	private BigDecimal amount;

	public enum CardType {
		VISA("Visa"), MASTER("Master Card"), AMEX("Amex");

		private String value;

		CardType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum Status {
		OPEN("Open"), CLOSED("Closed"), FAILURE("Failure");

		private String value;

		Status(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

}
