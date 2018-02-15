package pl.coderslab.service;

import java.util.List;
import java.util.Set;

import pl.coderslab.model.GroupBet;
import pl.coderslab.model.Message;
import pl.coderslab.model.User;

public interface MessageService {

	/**
	 * Finds all messages recieved by given User
	 * 
	 * @param user
	 * @return {@link Set} of messages
	 */
	Set<Message> findMessagesByReciever(User user);

	/**
	 * Finds all messages send by given User
	 * 
	 * @param user
	 * @return {@link List} of messages
	 */
	List<Message> findMessagesBySender(User user);

	/**
	 * Sends a message. It SAVES given message in the database.
	 * 
	 * @param message
	 * @return saved message
	 */
	Message sendMessage(Message message);

	/**
	 * Add reciever to the message. Can be done multiple times for adding more
	 * recievers. Does not save the message.
	 * 
	 * @param message
	 * @param user
	 * @return message with recievier added
	 */
	Message addReciever(Message message, User user);

	/**
	 * Adds sender to the message. Does NOT save the message.
	 * 
	 * @param message
	 * @param user
	 * @return message with saved user.
	 */
	Message addSender(Message message, User user);

	/**
	 * Creates and sends group bet invitation message.
	 * 
	 * It SAVES the message.
	 * 
	 * @param sender
	 * @param recievers
	 * @param groupBet
	 */
	void createAndSendGroupBetInvitationMessage(User sender, List<User> recievers, GroupBet groupBet);

	/**
	 * Creates and sends friend invitation message.
	 * 
	 * It SAVES the message.
	 * 
	 * @param sender
	 * @param recievers
	 */
	void createUserRequestInvitation(User sender, List<User> recievers);

	/**
	 * Creates and sends friend invitation accepted message.
	 * 
	 * It SAVES the message.
	 * 
	 * @param accepted
	 * @param reciever
	 */
	void createInvitationAcceptedMessage(User sender, User reciever);
}
