package pl.coderslab.service.implementation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.SingleBet;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.Operation;
import pl.coderslab.model.OperationType;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.OperationRepository;
import pl.coderslab.service.BetService;
import pl.coderslab.service.OperationService;

@Service
public class OperationServiceImpl implements OperationService {

	@Autowired
	OperationRepository operationRepository;
	
	@Autowired
	BetService betService;

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
		operation.setOperationInfo("Added funds. Amount: " + amount + " from credit card: ***" + lastFourDigits);
		return operationRepository.save(operation);
	}

	@Override
	public Operation createPlaceBetOperation(Wallet wallet, BigDecimal amount, SingleBet bet) {
		Operation operation = new Operation();
		operation.setWallet(wallet);
		operation.setAmount(amount);
		operation.setOperationType(OperationType.PLACE_BET);
		operation.setTimeOfOperation(LocalDateTime.now());
		operation.setOperationInfo("Bet placed. Amount: " + amount + " bet rate: " + bet.getRate());
		return operationRepository.save(operation);
	}

	@Override
	public Operation createWithdrawalOperation(Wallet wallet, BigDecimal amount) {
		Operation operation = new Operation();
		operation.setWallet(wallet);
		operation.setAmount(amount);
		operation.setOperationType(OperationType.WITHDRAWN);
		operation.setTimeOfOperation(LocalDateTime.now());
		operation.setOperationInfo("You have withrawn money to Your account. Amount: " + amount + ".");
		return operationRepository.save(operation);
	}

	@Override
	public Operation createPrizeOperation(Wallet wallet, SingleBet bet) {
		Operation operation = new Operation();
		operation.setWallet(wallet);
		BigDecimal amount = bet.getAmount().multiply(bet.getRate());
		operation.setAmount(amount);
		operation.setOperationType(OperationType.BET_PRIZE);
		operation.setTimeOfOperation(LocalDateTime.now());
		operation.setOperationInfo("Funds have been added to Your wallet: " + amount + ". You have won the bet! ");
		return operationRepository.save(operation);
	}

	@Override
	public List<Operation> findAllOperationByWallet(Wallet wallet) {
		List<Operation> operations = operationRepository.findOperationsByWallet(wallet);
		Collections.sort(operations, new Comparator<Operation>() {

			@Override
			public int compare(Operation o1, Operation o2) {
				return o1.getTimeOfOperation().compareTo(o2.getTimeOfOperation());
			}
		});
		return operations;
	}

	@Override
	public Operation createPlaceMultipleBetOperation(Wallet wallet, BigDecimal amount, List<SingleBet> bets) {
		Operation operation = new Operation();
		operation.setWallet(wallet);
		operation.setAmount(amount);
		operation.setOperationType(OperationType.PLACE_BET);
		operation.setTimeOfOperation(LocalDateTime.now());
		BigDecimal joinedRate = new BigDecimal(1);
		for (SingleBet singleBet : bets) {
		joinedRate = joinedRate.multiply(singleBet.getRate());	
		}
		operation.setOperationInfo("Multiple Bet placed. Amount: " + amount + " bet rate: " + joinedRate);
		return operationRepository.save(operation);
	}
	
	@Override
	public Operation createPrizeForMultipleBetOperation(Wallet wallet, MultipleBet multipleBet) {
		Operation operation = new Operation();
		operation.setWallet(wallet);
		BigDecimal amount = multipleBet.getJoinedAmount().multiply(multipleBet.getJoinedRating());
		operation.setAmount(amount);
		operation.setOperationType(OperationType.BET_PRIZE);
		operation.setTimeOfOperation(LocalDateTime.now());
		operation.setOperationInfo("Funds have been added to Your wallet: " + amount + ". You have won the bet! ");
		return operationRepository.save(operation);
	}

	@Override
	public Operation joinGroupBetOperation(Wallet wallet, GroupBet groupBet) {
		Operation operation = new Operation();
		operation.setWallet(wallet);
		operation.setAmount(groupBet.getJoinedAmount());
		operation.setOperationType(OperationType.PLACE_BET);
		operation.setTimeOfOperation(LocalDateTime.now());
		BigDecimal possibleWin = betService.calculatePossilbeWinInGroupBet(groupBet);
		operation.setOperationInfo("You have joined the Group Bet. Possible win for the moment of bet placement: " + possibleWin + ". You were charged: " + groupBet.getJoinedAmount());
		return operationRepository.save(operation);
	}

}
