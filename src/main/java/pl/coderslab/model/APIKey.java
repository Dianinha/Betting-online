package pl.coderslab.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class for storing API keys.
 * 
 * <p>
 * Application is tailored with: {@linkplain https://apifootball.com/} <br>
 * If You want to use it without modification You have to register on this page
 * and add to database Your API key with id=1
 * </p>
 * <p>
 * Possible future modification:
 * <ul>
 * <li>Add API key name</li>
 * <li>Add data from more API : create {@link Enum} ApiType and differ parsing
 * methods in Services basing on the ApiType</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
@Entity
@Table(name = "ApiKeys")
public class APIKey {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String keyCode;

	public APIKey() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getKeyCode() {
		return keyCode;
	}

	public void setKeyCode(String keyCode) {
		this.keyCode = keyCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((keyCode == null) ? 0 : keyCode.hashCode());
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
		APIKey other = (APIKey) obj;
		if (id != other.id)
			return false;
		if (keyCode == null) {
			if (other.keyCode != null)
				return false;
		} else if (!keyCode.equals(other.keyCode))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "APIKey [id=" + id + ", key=" + keyCode + "]";
	}

}
