package pl.coderslab.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Address;
import pl.coderslab.model.User;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long>{
	
	Address findByUser(User user);

}
