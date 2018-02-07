package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Country;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long>{
Country findByName(String name);
}
