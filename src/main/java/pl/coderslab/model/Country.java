package pl.coderslab.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import pl.coderslab.service.CountryService;
/**
 * This class represents country. Country data is created from API through {@link CountryService}
 * Every country has name, related leagues and related standings.
 * 
 * <p>
 * Possible future modifications:
 * <ul>
 * <li> use this data in {@link Address} country field</li>
 * <li> make country a {@link Category}</li>
 * </ul>
 * </p>
 * @author dianinha
 *
 */
@Entity
@Table(name = "countries")
public class Country {

	@Id
	@Column(name = "country_id")
	private Long id;

	@Column(name = "country_name")
	private String name;

	@OneToMany(mappedBy = "country")
	private Set<League> leagues;
	
	@OneToMany(mappedBy="country")
	private List<Standing> standings;

	public Country() {
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

	public Set<League> getLeagues() {
		return leagues;
	}

	public void setLeagues(Set<League> leagues) {
		this.leagues = leagues;
	}
	
	

	public List<Standing> getStandings() {
		return standings;
	}

	public void setStandings(List<Standing> standings) {
		this.standings = standings;
	}

	@Override
	public String toString() {
		return "Country [id=" + id + ", name=" + name + "]";
	}

}
