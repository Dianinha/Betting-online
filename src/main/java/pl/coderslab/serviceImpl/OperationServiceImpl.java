package pl.coderslab.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Operation;
import pl.coderslab.model.OperationType;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.OperationRepository;
import pl.coderslab.service.OperationService;

@Service
public class OperationServiceImpl implements OperationService{

	@Autowired
	OperationRepository operationRepository;
	
	@Override
	public Operation save(Operation operation) {
		return operationRepository.save(operation);
	}


	@Override
	public Operation createAddOperation(Wallet wallet, BigDecimal amount, String lastFourDigits) {
		Operation operation = new Operation();
		operation.setWallet(wallet);
		operation.setAmount(amount);
		operation.setOperationType(OperationType.ADD_FUNDS);
		operation.setTimeOfOperation(LocalDateTime.now());
		operation.setOperationInfo("Added funds. Amount: " + amount + "from credit card: ***" + lastFourDigits);
		return operationRepository.save(operation);
	}

}
