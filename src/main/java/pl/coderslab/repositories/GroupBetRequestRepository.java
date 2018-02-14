package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.GroupBetRequest;
import pl.coderslab.model.User;

@Repository
public interface GroupBetRequestRepository extends JpaRepository<GroupBetRequest, Long> {

	@Query("select r from GroupBetRequest r where r.status=true and r.reciever = ?1")
	List<GroupBetRequest> findByUser(User user);

	GroupBetRequest findByBetCode(String betCode);
}
