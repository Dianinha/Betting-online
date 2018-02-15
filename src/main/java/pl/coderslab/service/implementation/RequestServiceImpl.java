package pl.coderslab.service.implementation;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Request;
import pl.coderslab.model.User;
import pl.coderslab.repositories.RequestRepository;
import pl.coderslab.service.RequestService;
import pl.coderslab.service.UserService;

@Service
public class RequestServiceImpl implements RequestService {

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private UserService userService;

	@Override
	public Request createRequest(User sender, User reciever) {
		Request request = new Request();
		request.setStatus(true);
		request.setSender(sender);
		request.setReciever(reciever);
		return requestRepository.save(request);
	}

	@Override
	public void acceptRequest(Request request) {
		request.setStatus(false);
		User userOne = userService.findByUsername(request.getSender().getUsername());
		User userTwo = userService.findByUsername(request.getReciever().getUsername());
		Set <User> userOneFriends = userOne.getFriends();
		userOneFriends.add(userTwo);
		Set<User> userTwoFriends = userTwo.getFriends();
		userTwoFriends.add(userOne);
		userOne.setFriends(userOneFriends);
		userTwo.setFriends(userTwoFriends);
		userService.saveUser(userOne);
		userService.saveUser(userTwo);
	}

	@Override
	public void discardRequest(Request request) {
		request.setStatus(false);
		requestRepository.save(request);
	}

	@Override
	public List<Request> findByUser(User user) {
		return requestRepository.findByUser(user);
	}

	@Override
	public Request findById(long id) {
		return requestRepository.findOne(id);
	}

}
