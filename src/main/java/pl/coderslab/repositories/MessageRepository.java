package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Message;
import pl.coderslab.model.User;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>{
	List<Message> findAllBySender(User sender);
	
//	@Query("SELECT m FROM Message m INNER JOIN m.recievers r WHERE r IN (?1)")
//	Set<Message> findMessagesByUser(User user);
	
	List<Message> findAllByRecieversIs(User reciever);
}
