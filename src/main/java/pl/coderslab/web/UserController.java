package pl.coderslab.web;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.model.Address;
import pl.coderslab.model.BetStatus;
import pl.coderslab.model.CreditCardInfo;
import pl.coderslab.model.Event;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.Message;
import pl.coderslab.model.MultipleBet;
import pl.coderslab.model.Operation;
import pl.coderslab.model.Request;
import pl.coderslab.model.SingleBet;
import pl.coderslab.model.User;
import pl.coderslab.model.UserSimple;
import pl.coderslab.model.Wallet;
import pl.coderslab.repositories.GroupBetRepository;
import pl.coderslab.repositories.MultipleBetRepository;
import pl.coderslab.service.AddressService;
import pl.coderslab.service.BetService;
import pl.coderslab.service.CreditCardService;
import pl.coderslab.service.EventService;
import pl.coderslab.service.GameToBetService;
import pl.coderslab.service.MessageService;
import pl.coderslab.service.OperationService;
import pl.coderslab.service.RequestService;
import pl.coderslab.service.UserService;
import pl.coderslab.service.WalletService;
/**This is controller for basic {@link User}
 * 
 * @author dianinha
 *
 */
@Controller
@RequestMapping(value = "/user")
public class UserController {

	@Autowired
	private CreditCardService creditService;

	@Autowired
	private UserService userService;

	@Autowired
	private WalletService walletService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private MessageService messageService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private EventService eventService;

	@Autowired
	private GameToBetService gameService;

	@Autowired
	private BetService betService;

	@Autowired
	private MultipleBetRepository multiBetRepository;

	@Autowired
	AddressService addressService;

	@Autowired
	GroupBetRepository groupBetRepository;

	@RequestMapping(path = "/menu")
	public String menu() {
		return "/user/menu";
	}

	@RequestMapping(path = "", method = RequestMethod.GET)
	public String maimUserPageGet(HttpSession session, Authentication authentication, Model model) {
		User user = userService.getAuthenticatedUser(authentication);
		if (user == null) {
			return "redirect:/login";
		}
		System.out.println(user.getWallet().getAmount());
		session.setAttribute("currentFunds", user.getWallet().getAmount());
		List<Event> liveEvents = eventService.findByDate(LocalDate.now());
		Collections.sort(liveEvents, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});
		for (Event event : liveEvents) {
			System.out.println(event);
		}
		model.addAttribute("liveNow", liveEvents);
		model.addAttribute("gameToBetNow", gameService.findByListOfEvents(liveEvents));

		return "/user/menu";
	}

	@RequestMapping(path = "", method = RequestMethod.POST)
	public String maimUserPage(HttpSession session, Authentication authentication, Model model) {
		User user = userService.getAuthenticatedUser(authentication);
		session.setAttribute("currentFunds", user.getWallet().getAmount());
		List<Event> liveEvents = eventService.findByDate(LocalDate.now());
		Collections.sort(liveEvents, new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});
		model.addAttribute("liveNow", liveEvents);
		model.addAttribute("gameToBetNow", gameService.findByListOfEvents(liveEvents));
		return "/user/menu";
	}

}
