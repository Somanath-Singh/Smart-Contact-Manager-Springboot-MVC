package com.smart.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotBlank(message = "Name field must be required ! ! ")
	@Size(min = 2, max = 20, message = "min 2 and max 20 characters are allowed ! ! ")
	private String name;

	@Column(unique = true)
	@NotBlank(message = "Email field must be required ! ! ")
	@Email
	private String email;

	@Column(length = 300)
	@NotNull
	@Size(min = 8, max = 200)
	private String password;

	private String role;
	private boolean enabled;
	
	private boolean verifiedEmail;
	
	private String verificationCode;
	
	private String imageurl;

	@NotBlank(message = "About field must be required ! ! ")
	@Size(min = 20, max = 300, message = "min 20 and max 300 characters are allowed ! ! ")
	private String about;
	

}
