package pl.coderslab.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserAddressDto {

	@Size(min = 3, max = 26, message = "Username should be at least 3 characters long. Maximum size is 26.")
	private String username;

	@Email(message = "Please write down proper e-mail address, for example: simpleExample@gmail.com")
	@NotBlank(message = "Email address cannot be empty.")
	private String email;

	@NotBlank(message = "Password cannot be empty")
	private String password;

	private boolean generalSubscription;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotBlank(message = "Surname cannot be empty")
	private String surname;

	@NotBlank(message = "Street name cannot be empty.")
	private String streetName;

	@Min(value = 0, message = "Please pick number greater than 0.")
	private int streetNumber;

	private String additionalLetterToNumber;

	private int flatNumber;

	private String zipCode;

	@NotBlank(message = "City cannot be empty.")
	private String city;

	@NotBlank(message = "Country cannot be empty.")
	private String country;

	public UserAddressDto(String username, String email, String password, boolean generalSubscription, String name,
			String surname, String streetName, int streetNumber, String additionalLetterToNumber, int flatNumber,
			String zipCode, String city, String country) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.generalSubscription = generalSubscription;
		this.name = name;
		this.surname = surname;
		this.streetName = streetName;
		this.streetNumber = streetNumber;
		this.additionalLetterToNumber = additionalLetterToNumber;
		this.flatNumber = flatNumber;
		this.zipCode = zipCode;
		this.city = city;
		this.country = country;
	}

	public UserAddressDto() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isGeneralSubscription() {
		return generalSubscription;
	}

	public void setGeneralSubscription(boolean generalSubscription) {
		this.generalSubscription = generalSubscription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public int getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getAdditionalLetterToNumber() {
		return additionalLetterToNumber;
	}

	public void setAdditionalLetterToNumber(String additionalLetterToNumber) {
		this.additionalLetterToNumber = additionalLetterToNumber;
	}

	public int getFlatNumber() {
		return flatNumber;
	}

	public void setFlatNumber(int flatNumber) {
		this.flatNumber = flatNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
