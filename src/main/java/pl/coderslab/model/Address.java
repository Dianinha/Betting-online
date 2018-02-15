package pl.coderslab.model;

import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import org.hibernate.validator.constraints.NotBlank;

/**
 * {@code Address} represents {@link User} address information. It stores
 * information about User (in relation one to one), Street name (stored in
 * {@link String}, street number (stored in int), flat number (stored in int),
 * zipCope (stored in {@link String} without pattern it will be good to add it
 * someday), city (stored in {@link String}) and country ({@link String}). User
 * can also provide additional info in {@link String}, if his or her flat number
 * or street number have a letter <br>
 * <br>
 * Example: For: Main Street 52a <br>
 * streetName : Main <br>
 * streetNumber : 52 <br>
 * additionalLetterToNumber : a <br>
 * 
 * <p>
 * 
 * Possible future modifications:
 * <ul>
 * <li>add {@link Pattern} to zip code</li>
 * <li>provide list of countries, so user cannot type anything</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "addresses")
public class Address {

	// attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private long id;

	@OneToOne
	private User user;

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

	// Constructor
	public Address() {
		super();
	}

	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((additionalLetterToNumber == null) ? 0 : additionalLetterToNumber.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + flatNumber;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((streetName == null) ? 0 : streetName.hashCode());
		result = prime * result + streetNumber;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (additionalLetterToNumber == null) {
			if (other.additionalLetterToNumber != null)
				return false;
		} else if (!additionalLetterToNumber.equals(other.additionalLetterToNumber))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (flatNumber != other.flatNumber)
			return false;
		if (id != other.id)
			return false;
		if (streetName == null) {
			if (other.streetName != null)
				return false;
		} else if (!streetName.equals(other.streetName))
			return false;
		if (streetNumber != other.streetNumber)
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

	// simplified to string
	@Override
	public String toString() {
		return "Address [id=" + id + ", user=" + user.getId() + ", streetName=" + streetName + ", streetNumber="
				+ streetNumber + ", additionalLetterToNumber=" + additionalLetterToNumber + ", flatNumber=" + flatNumber
				+ ", zipCode=" + zipCode + ", city=" + city + ", country=" + country + "]";
	}

}
