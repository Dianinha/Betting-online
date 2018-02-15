package pl.coderslab.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import pl.coderslab.service.LeagueService;
/**
 * Class represents Leagues. Data is gathered from API through {@link LeagueService}
 * every league has corresponding country.
 * 
 *  * <p>
 * Possible future modifications:
 * <ul>
 * <li> make league a {@link Category}</li>
 * </ul>
 * </p>
 */
@Entity
@Table(name="leagues")
public class League {

	@Id
	private long id;
	
	private String name;
	
	@ManyToOne
	@JoinColumn
	private Country country;
	
	@OneToMany(mappedBy="league")
	private List<Standing> standings;
	
	public League() {
		super();
	}

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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}
	
	

	public List<Standing> getStandings() {
		return standings;
	}

	public void setStandings(List<Standing> standings) {
		this.standings = standings;
	}

	@Override
	public String toString() {
		return "League [id=" + id + ", name=" + name + ", country=" + country + "]";
	}
	
	
	
	
}
