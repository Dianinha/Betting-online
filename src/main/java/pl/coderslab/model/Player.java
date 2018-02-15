package pl.coderslab.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * This class has no use in my application right now.
 * 
 * Still, there are many possibilities with it.
 * <ul>
 * <li> create ranking of players </li>
 * <li> create players statistics </li>
 * <li> and many more, sky is the limit</li>
 * </ul>
 * 
 * @author dianinha
 *
 */
@Entity
public class Player {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String name;

	public Player() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
