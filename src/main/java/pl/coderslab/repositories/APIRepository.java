package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.APIKey;

@Repository
public interface APIRepository extends JpaRepository<APIKey, Long>{

	
}
