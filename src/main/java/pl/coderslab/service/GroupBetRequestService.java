package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.GroupBet;
import pl.coderslab.model.GroupBetRequest;
import pl.coderslab.model.User;

public interface GroupBetRequestService {

	/**Saves in database requests to join a {@link GroupBet}
	 * 
	 * @param groupBet
	 * @param recievers
	 * @param sender
	 */
	void sendGroupBetRequests(GroupBet groupBet, List<User> recievers, User sender);

	/**Finds a list of {@link GroupBetRequest} that where send to the {@link User}
	 * 
	 * @param user
	 * @return
	 */
	List<GroupBetRequest> getRequestsByUser(User user);

	/**
	 * Checks if {@link GroupBet} is still available to join in.
	 * @param groupBetRequest
	 * @return true if given {@link GroupBetRequest} is still valid, false otherwise
	 */
	boolean checkIfYouCanJoinTheGroupBet(GroupBetRequest groupBetRequest);

	/**
	 * Sets status of given {@link GroupBetRequest} to false and saves it.
	 * @param groupBetRequest
	 */
	void discardRequest(GroupBetRequest groupBetRequest);
	
	/**
	 * Finds {@link GroupBetRequest} by given id
	 * @param id
	 * @return 
	 */
	GroupBetRequest findById(long id);

}
