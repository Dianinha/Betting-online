package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Request;
import pl.coderslab.model.User;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long>{
	
	@Query("select r from Request r where r.status=true and r.reciever = ?1")
	List<Request> findByUser(User user);
	

}
