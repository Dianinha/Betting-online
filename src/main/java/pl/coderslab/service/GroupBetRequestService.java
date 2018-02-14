package pl.coderslab.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import pl.coderslab.model.GroupBet;
import pl.coderslab.model.GroupBetRequest;
import pl.coderslab.model.User;

public interface GroupBetRequestService {

	void sendGroupBetRequests(GroupBet groupBet, List<User> recievers, User sender);

	List<GroupBetRequest> getRequestsByUser(User user);

	boolean checkIfYouCanJoinTheGroupBet(GroupBetRequest groupBetRequest);

	void discardRequest(GroupBetRequest groupBetRequest);
	
	GroupBetRequest findById(long id);

}
