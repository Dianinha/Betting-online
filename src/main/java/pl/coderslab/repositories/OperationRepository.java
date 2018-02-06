package pl.coderslab.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pl.coderslab.model.Operation;
import pl.coderslab.model.Wallet;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

	List<Operation> findOperationsByWallet (Wallet wallet);
	
	
}
