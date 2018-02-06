package pl.coderslab.service;

import java.math.BigDecimal;

import pl.coderslab.model.Operation;
import pl.coderslab.model.OperationType;
import pl.coderslab.model.Wallet;

public interface OperationService {

	Operation save(Operation operation);
	
	
	Operation createAddOperation(Wallet wallet, BigDecimal amount, String lastFourDigits);
}
