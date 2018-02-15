package pl.coderslab.model;

import javax.persistence.Column;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

/**
 * This class is not stored in database. Its only use is to simplify the process of editing and saving {@link User} data.
 * SimpleUser can and SHOULD be created from {@link User}.
 * 
 * @author dianinha
 *
 */
public class UserSimple {

	@Size(min = 3, max = 26, message = "Username should be at least 3 characters long. Maximum size is 26.")
	@Column(unique = true)
	private String username;

	@Email(message = "Please write down proper e-mail address, for example: simpleExample@gmail.com")
	@NotBlank(message = "Email address cannot be empty.")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotBlank(message = "Surname cannot be empty")
	private String surname;

	private boolean generalSubscription;

	public UserSimple() {
		super();
	}

	public UserSimple(User user) {
		super();
		this.username = user.getSurname();
		this.surname = user.getSurname();
		this.name = user.getName();
		this.email = user.getEmail();
		this.generalSubscription = user.isGeneralSubscription();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isGeneralSubscription() {
		return generalSubscription;
	}

	public void setGeneralSubscription(boolean generalSubscription) {
		this.generalSubscription = generalSubscription;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

}
