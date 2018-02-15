package pl.coderslab.service;

import java.util.List;

import pl.coderslab.model.Request;
import pl.coderslab.model.User;

public interface RequestService {

	/**
	 * Creates and saves the friend request.
	 * @param sender
	 * @param reciever
	 * @return saved request
	 */
	Request createRequest(User sender, User reciever);

	/**
	 * Set status of request to false and saves it. It also updates sender and
	 * reciever friends list.
	 * 
	 * @param request
	 */
	void acceptRequest(Request request);

	/**
	 * Sets request status to false and saves it.
	 * 
	 * @param request
	 */
	void discardRequest(Request request);

	/**
	 * Finds all request that were SEND to the given user
	 * 
	 * @param user
	 * @return
	 */
	List<Request> findByUser(User user);

	/**
	 * Finds invitation by id
	 * 
	 * @param id
	 * @return
	 */
	Request findById(long id);
}
