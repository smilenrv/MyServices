package com.cts.cardservice.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cts.cardservice.validator.DateConstraint;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description="All details about the customer.")
public class CardRequest {
	
	@ApiModelProperty(notes="Date and it should be in format MM/DD/YYYY", required = true)
	@NotBlank(message = "date.empty")
    @DateConstraint
	private String expirationDate;

	@ApiModelProperty(notes="Card type should be VISA or MASTER or AMEX", required = true)
	@NotNull(message = "card.type.notvalid")
	private CardType cardType;
	
	public enum CardType {
		VISA("Visa"), MASTER("Master Card"), AMEX("Amex");
		
		private String value;
		
		CardType(String value){
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}

}
