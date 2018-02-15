package pl.coderslab.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
/**
 * User role in application.
 * 
 * IMPORTANT! Before using the application please add "ROLE_USER" to database. 
 * 
 * Further possibilities: 
 * <ul>
 * <li>create section for ROLE_ADMIN </li>
 * <li> create special role for API receivers? </li>
 * </ul>
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "roles")
public class Role {

	// attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private long id;

	@NotBlank
	@NotNull
	private String name;

	// Constructor
	public Role() {
		super();
	}

	// getters and setters
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	// hashCode and equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Role other = (Role) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	// simplified to string
	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + "]";
	}
}
