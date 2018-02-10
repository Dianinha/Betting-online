package pl.coderslab.serviceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;
import pl.coderslab.model.Standing;
import pl.coderslab.repositories.GameToBetRepository;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.StandingService;

@Service
public class GameToBetServiceImpl implements GameToBetService {

	@Autowired
	EventService eventService;

	@Autowired
	GameToBetRepository gameRepository;

	@Autowired
	StandingService standingService;

	@Override
	public void createGamesToBet() {
		List<Event> futureEvents = eventService.findByDate(LocalDate.now());
		for (Event event : futureEvents) {
			GameToBet game = new GameToBet();
			
			System.out.println(event.getStatus());
			if (event.getStatus().equals("FT")) {
				game.setActive(false);
			} else {
				game.setActive(true);
			}
			game.setId(event.getId());
			game.setEvent(event);
			Standing home = standingService.findStanfingByTeamName(event.getHomeTeamName());
			Standing away = standingService.findStanfingByTeamName(event.getAwayTeamName());
			System.out.println(home);
			System.out.println(away);
			game.setOddsToWinHome(0.5);
			
			game.setOddsToWinAway(0.5);
			
			game.setOddsToWinDraw(0.2);
			
			game.setRateHome(new BigDecimal((1.00-game.getOddsToWinHome())+0.80));
			game.setRateDraw(new BigDecimal((1.00-game.getOddsToWinDraw())+0.80));
			game.setRateAway(new BigDecimal((1-game.getOddsToWinAway())+0.80));
			
			System.out.println(game);
			gameRepository.save(game);
		}

	}

	@Override
	public GameToBet findByEvent(Event event) {
		return gameRepository.findByEvent(event);
	}

	@Override
	public GameToBet findById(long id) {
		return gameRepository.findOne(id);
	}

}
