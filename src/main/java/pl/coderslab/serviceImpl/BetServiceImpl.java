package pl.coderslab.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Bet;
import pl.coderslab.repositories.BetRepository;
import pl.coderslab.service.BetService;

@Service
public class BetServiceImpl implements BetService {

	@Autowired
	BetRepository betRepository;

	@Override
	public void placeBet(Bet bet) {

		betRepository.save(bet);

	}

}
