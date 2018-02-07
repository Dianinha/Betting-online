package pl.coderslab.model;

import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
