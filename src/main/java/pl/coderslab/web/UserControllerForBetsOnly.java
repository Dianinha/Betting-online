package pl.coderslab.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.repositories.GroupBetRepository;
import pl.coderslab.service.BetService;
import pl.coderslab.service.UserService;

/**
 * This controller controls the bet section in User account
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserControllerForBetsOnly {

	

	@Autowired
	private UserService userService;

	@Autowired
	private BetService betService;

	@Autowired
	GroupBetRepository groupBetRepository;

	/**
	 * List of Users bets. Group Bets are not included.
	 * 
	 * <p>
	 * currentBets : current User's unresolved bets pastBets : past bets placed by
	 * User pastMultiBets : current User's unresolved Multiple Bets pastMultiBets :
	 * past Multiple bets placed by User
	 * </p>
	 * 
	 * @param model
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/myBets", method = RequestMethod.GET)
	public String userBets(Model model, Authentication authentication) {

		User user = userService.getAuthenticatedUser(authentication);

		List<SingleBet> currentBets = betService.findBetsByUserAndStatus(BetStatus.PLACED, user);
		if (!currentBets.isEmpty()) {
			model.addAttribute("bets", currentBets);
		}
		List<SingleBet> pastBets = betService.findBetsByUserAndStatus(BetStatus.FINALIZED, user);
		if (!pastBets.isEmpty()) {
			model.addAttribute("oldbets", pastBets);
		}
		List<MultipleBet> currentMultiBets = betService.findMultipleBetsByUserAndStatus(BetStatus.PLACED, user);
		if (!currentMultiBets.isEmpty()) {
			model.addAttribute("multiBets", currentMultiBets);
		}
		List<MultipleBet> pastMultiBets = betService.findMultipleBetsByUserAndStatus(BetStatus.FINALIZED, user);
		for (MultipleBet multipleBet : pastMultiBets) {
			System.out.println(multipleBet);
		}
		if (!pastMultiBets.isEmpty()) {
			model.addAttribute("oldMultiBets", pastMultiBets);
		}
		return "bet/userBets";
	}

	/**
	 * This method displays Group Bets for User. Id divides Group Bet by its
	 * BetStatus.
	 * 
	 * @param model
	 * @param auth
	 * @return
	 */
	@RequestMapping(value = "/groupBets", method = RequestMethod.GET)
	public String userGroupBets(Model model, Authentication auth) {

		User user = userService.getAuthenticatedUser(auth);
		List<GroupBet> userBets = betService.findGroupBetByUser(user);
		List<GroupBet> currentBets = new ArrayList<>();
		List<GroupBet> pastBets = new ArrayList<>();

		for (GroupBet groupBet : userBets) {
			if (groupBet.getStatus().equals(BetStatus.PLACED)) {
				currentBets.add(groupBet);
			} else if (groupBet.getStatus().equals(BetStatus.FINALIZED)) {
				pastBets.add(groupBet);
			}

		}
		if (!currentBets.isEmpty()) {
			model.addAttribute("bets", currentBets);
		}
		if (!pastBets.isEmpty()) {
			model.addAttribute("oldbets", pastBets);
		}
		return "/bet/userGroupBets";

	}
}
