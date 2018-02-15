package pl.coderslab.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
/**
 * This class corresponds to User of my application
 * 
 * Further corrections:
 * <ul>
 * <li> attribute passwordConfirmed should be deleted </li>
 * <li> validation should be corrected</li>
 * </ul>
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private long id;

	@Size(min = 3, max = 26, message = "Username should be at least 3 characters long. Maximum size is 26.")
	@Column(unique = true)
	private String username;

	@Email(message = "Please write down proper e-mail address, for example: simpleExample@gmail.com")
	@NotBlank(message = "Email address cannot be empty.")
	@Column(unique = true)
	private String email;

	@NotBlank(message = "Password cannot be empty")
	private String password;

	@Transient
	@org.springframework.data.annotation.Transient
	private transient String passwordConfirmed;

	@NotBlank(message = "Name cannot be empty")
	private String name;

	@NotBlank(message = "Surname cannot be empty")
	private String surname;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn
	private Wallet wallet;

	@OneToMany(mappedBy = "user")
	private List<SingleBet> userBets;

	@ManyToMany
	@JoinTable(name = "user_event", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "event_id"))
	private Set<Event> userObservedGames;

	private boolean generalSubscription;

	@OneToOne(cascade = { CascadeType.ALL })
	@JoinColumn
	private Address address;

	@ManyToMany
	@JoinTable(name = "user_category", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
	private Set<Category> categories;

	@OneToMany(mappedBy = "sender")
	private List<Message> sendMessages;

	@ManyToMany
	@JoinTable(name = "user_recievedMessages", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "message_id"))
	private List<Message> recievedMessages;

	@ManyToMany
	@JoinTable(name = "user_friends", joinColumns = @JoinColumn(name = "friendOwner_id"), inverseJoinColumns = @JoinColumn(name = "userFriend_id"))
	private Set<User> friends;

	// Constructor
	public User() {
		super();
	}

	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getPasswordConfirmed() {
		return passwordConfirmed;
	}

	public void setPasswordConfirmed(String passwordConfirmed) {
		this.passwordConfirmed = passwordConfirmed;
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

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public List<SingleBet> getUserBets() {
		return userBets;
	}

	public void setUserBets(List<SingleBet> userBets) {
		this.userBets = userBets;
	}

	public Set<Event> getUserObservedGames() {
		return userObservedGames;
	}

	public void setUserObservedGames(Set<Event> userObservedGames) {
		this.userObservedGames = userObservedGames;
	}

	public boolean isGeneralSubscription() {
		return generalSubscription;
	}

	public void setGeneralSubscription(boolean generalSubscription) {
		this.generalSubscription = generalSubscription;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public List<Message> getSendMessages() {
		return sendMessages;
	}

	public void setSendMessages(List<Message> sendMessages) {
		this.sendMessages = sendMessages;
	}

	public List<Message> getRecievedMessages() {
		return recievedMessages;
	}

	public void setRecievedMessages(List<Message> recievedMessages) {
		this.recievedMessages = recievedMessages;
	}

	
	public Set<User> getFriends() {
		return friends;
	}

	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categories == null) ? 0 : categories.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (generalSubscription ? 1231 : 1237);
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((userBets == null) ? 0 : userBets.hashCode());
		result = prime * result + ((userObservedGames == null) ? 0 : userObservedGames.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((wallet == null) ? 0 : wallet.hashCode());
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
		User other = (User) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (generalSubscription != other.generalSubscription)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (userBets == null) {
			if (other.userBets != null)
				return false;
		} else if (!userBets.equals(other.userBets))
			return false;
		if (userObservedGames == null) {
			if (other.userObservedGames != null)
				return false;
		} else if (!userObservedGames.equals(other.userObservedGames))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		if (wallet == null) {
			if (other.wallet != null)
				return false;
		} else if (!wallet.equals(other.wallet))
			return false;
		return true;
	}

	// simplified to string
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password + ", name="
				+ name + ", surname=" + surname + ", roles=" + roles + ", wallet=" + wallet + ", address=" + address
				+ "]";
	}

}
