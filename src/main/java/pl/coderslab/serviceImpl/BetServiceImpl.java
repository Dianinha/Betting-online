package pl.coderslab.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.BetRepository;
import pl.coderslab.repositories.MultipleBetRepository;
import pl.coderslab.service.BetService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.WalletService;

@Service
public class BetServiceImpl implements BetService {

	@Autowired
	BetRepository betRepository;

	@Autowired
	MultipleBetRepository multibetRepository;

	@Autowired
	GameToBetService gameService;

	@Autowired
	EventService eventService;

	@Autowired
	WalletService walletService;
	
	@Autowired
	OperationService operationService;

	@Override
	public void placeBet(SingleBet bet) {

		betRepository.save(bet);

	}

	@Override
	public boolean isBetRateAccurate(SingleBet bet) {
		boolean result = false;
		GameToBet game = gameService.findById(bet.getGame().getId());
		System.out.println(bet);
		if (bet.getBetOn().equals("home")) {
			System.out.println("home");
			result = bet.getRate().equals(game.getRateHome());
		} else if (bet.getBetOn().equals("away")) {
			System.out.println("away");
			result = bet.getRate().equals(game.getRateAway());
		} else {
			System.out.println("draw");
			result = bet.getRate().equals(game.getRateDraw());
		}
		return result;
	}

	@Override
	public boolean isBetAlreadyPlaced(SingleBet bet, List<SingleBet> bets) {
		boolean flag = false;

		for (SingleBet singleBet : bets) {
			if (singleBet.getGame().getId() == bet.getGame().getId()) {
				flag = true;
			}
		}

		return flag;
	}

	@Override
	public List<SingleBet> findBetsByUserAndStatus(BetStatus status, User user) {
		return betRepository.findByUser(status, user);
	}

	@Override
	public void checkBetsForTodayGames() {

		List<Event> liveEvents = eventService.findByDate(LocalDate.now());
		List<GameToBet> games = gameService.findByListOfEvents(liveEvents);
		for (GameToBet gameToBet : games) {
			if (!gameToBet.isActive()) {
				List<SingleBet> singleBets = betRepository.findByGameAndStatus(gameToBet, BetStatus.PLACED);
				for (SingleBet singleBet : singleBets) {

					try {

						String eventResult = getEventResult(gameToBet.getEvent());

						if (eventResult.equals(singleBet.getBetOn())) {
							User user = singleBet.getUser();
							Wallet wallet = walletService.findByUser(user);
							BigDecimal prize = singleBet.getAmount().multiply(singleBet.getRate());
							walletService.addFunds(wallet, prize);
							operationService.createPrizeOperation(wallet, singleBet);
							singleBet.setBetResult("WON");
						}
						
						else {
							singleBet.setBetResult("LOST");
						}
						
						singleBet.setStatus(BetStatus.FINALIZED);
						betRepository.save(singleBet);

					} catch (Exception e) {
					}
				}
			}
		}

	}

	private boolean betResult(SingleBet bet) {

		String betOn = bet.getBetOn();

		return false;

	}

	private String getEventResult(Event event) {
		String result = "";
		int homeScore = event.getHomeTeamScore();
		int awayScore = event.getAwayTeamScore();

		if (homeScore > awayScore) {
			result = "home";
		}

		else if (homeScore < awayScore) {
			result = "away";
		} else {
			result = "draw";
		}

		return result;

	}

}
