package com.cts.tanmoy.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@Generated
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuthenticationRequest {
	@Id
	private String username;
	private String password;
}
