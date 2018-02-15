package pl.coderslab.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.coderslab.model.GameToBet;
import pl.coderslab.service.GameToBetService;

@RestController
@RequestMapping("/api")
public class APIController {

	@Autowired
	GameToBetService gameService;
	
	@GetMapping(value = "/activeGamesToBet")
	public ResponseEntity<List<GameToBet>> getActiveGamesToBet() {
		return ResponseEntity.ok(gameService.findActiveGames());
	}

}
