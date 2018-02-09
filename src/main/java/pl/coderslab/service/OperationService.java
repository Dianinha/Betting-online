package pl.coderslab.service;

import java.math.BigDecimal;

import pl.coderslab.model.Bet;
import pl.coderslab.model.Operation;
import pl.coderslab.model.Wallet;

public interface OperationService {

	Operation save(Operation operation);
	
	Operation createAddOperation(Wallet wallet, BigDecimal amount, String lastFourDigits);
	
	Operation createPlaceBetOperation(Wallet wallet, BigDecimal amount, Bet bet);
}
