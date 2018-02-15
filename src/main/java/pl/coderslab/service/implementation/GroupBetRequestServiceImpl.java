package pl.coderslab.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.BetStatus;
import pl.coderslab.model.GroupBet;
import pl.coderslab.model.GroupBetRequest;
import pl.coderslab.model.User;
import pl.coderslab.repositories.GroupBetRepository;
import pl.coderslab.repositories.GroupBetRequestRepository;
import pl.coderslab.service.BetService;
import pl.coderslab.service.GroupBetRequestService;

@Service
public class GroupBetRequestServiceImpl implements GroupBetRequestService {

	@Autowired
	private GroupBetRequestRepository groupBetRequestRepository;

	@Autowired
	private GroupBetRepository groupBetRepository;

	@Override
	public void sendGroupBetRequests(GroupBet groupBet, List<User> recievers, User sender) {
		GroupBet bet = groupBetRepository.findOne(groupBet.getId());
		for (User reciever : recievers) {
			GroupBetRequest request = new GroupBetRequest();
			request.setStatus(true);
			request.setSender(sender);
			request.setReciever(reciever);
			request.setBetCode(groupBet.getBetCode());
			request.setGroupBet(bet);
			groupBetRequestRepository.save(request);
		}
	}

	@Override
	public List<GroupBetRequest> getRequestsByUser(User user) {
		return groupBetRequestRepository.findByUser(user);
	}

	@Override
	public boolean checkIfYouCanJoinTheGroupBet(GroupBetRequest groupBetRequest) {
		GroupBet groupBet = groupBetRequest.getGroupBet();
		if (groupBet.getStatus().equals(BetStatus.FINALIZED)) {
			groupBetRequest.setStatus(false);
			groupBetRequestRepository.save(groupBetRequest);
			return false;
		}
		if (groupBet.getUsers().size() > 19) {
			groupBetRequest.setStatus(false);
			groupBetRequestRepository.save(groupBetRequest);
			return false;
		}

		return true;
	}

	@Override
	public void discardRequest(GroupBetRequest groupBetRequest) {

		groupBetRequest.setStatus(false);
		groupBetRequestRepository.save(groupBetRequest);
	}

	@Override
	public GroupBetRequest findById(long id) {
		return groupBetRequestRepository.findOne(id);
	}
}
