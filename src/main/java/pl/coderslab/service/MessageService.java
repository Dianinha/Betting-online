package pl.coderslab.service;

import java.util.List;
import java.util.Set;

import pl.coderslab.model.GroupBet;
import pl.coderslab.model.Message;
import pl.coderslab.model.User;

public interface MessageService {

	Set<Message> findMessagesByReciever(User user);

	List<Message> findMessagesBySender(User user);

	Message sendMessage(Message message);

	Message addReciever(Message message, User user);
	
	Message addSender(Message message, User user);
	
	void createAndSendGroupBetInvitationMessage(User sender, List<User> recievers, GroupBet groupBet);
}
