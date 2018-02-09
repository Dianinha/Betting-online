package pl.coderslab.service;

import pl.coderslab.model.Event;
import pl.coderslab.model.GameToBet;

public interface GameToBetService {

	public void createGamesToBet();
	
	GameToBet findByEvent(Event event);
	
	GameToBet findById(long id);
}
