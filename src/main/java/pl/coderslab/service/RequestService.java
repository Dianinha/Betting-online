package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.Request;
import pl.coderslab.model.User;

public interface RequestService {

	Request createRequest(Request request);

	void acceptRequest(Request request);

	void discardRequest(Request request);

	List<Request> findByUser(User user);
	
	Request findById(long id);
}
