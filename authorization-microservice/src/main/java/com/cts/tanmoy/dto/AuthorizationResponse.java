package com.cts.tanmoy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationResponse {
	
	@JsonProperty
    private boolean validStatus;
}
