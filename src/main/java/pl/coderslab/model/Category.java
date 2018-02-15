package pl.coderslab.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotBlank;

/**
 * This class represents categories for {@link Event} For now, event can have only one category.
 * Category is described by attribute {@link String} name.
 * <p>
 * For now there are only Football games.
 * </p>
 * 
 * <p>
 * Important! Before using the application, please create category "Football" in
 * database.
 * </p>
 * 
 * <p>
 * Possible future modifications:
 * <ul>
 * <li>
 * add subcategory, for example for football it can be league or country
 * </li>
 * <li>change relationship with event to ManyToMany, so event can have more then one category </li>
 * <li> add category description</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "categories")
public class Category {

	// attributes
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@NotBlank
	private String name;

	// Constructor
	public Category() {
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
		Category other = (Category) obj;
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
		return "Category [id=" + id + ", name=" + name + "]";
	}

}
