package pl.coderslab.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.GroupBet;
import pl.coderslab.model.Message;
import pl.coderslab.model.User;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.service.BetService;
import pl.coderslab.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Autowired
	private BetService betService;

	@Override
	public Set<Message> findMessagesByReciever(User user) {
		List<Message> listMessage = messageRepository.findAllByRecieversIs(user);
		Collections.sort(listMessage, new Comparator<Message>() {

			@Override
			public int compare(Message o1, Message o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});
		Collections.reverse(listMessage);
		return new HashSet<>(listMessage);
	}

	@Override
	public List<Message> findMessagesBySender(User user) {
		List<Message> listMessage = messageRepository.findAllBySender(user);
		Collections.sort(listMessage, new Comparator<Message>() {

			@Override
			public int compare(Message o1, Message o2) {
				return o1.getTime().compareTo(o2.getTime());
			}
		});
		Collections.reverse(listMessage);
		return listMessage;
	}

	@Override
	public Message sendMessage(Message message) {
		message.setTime(LocalDateTime.now());
		return messageRepository.save(message);
	}

	@Override
	public Message addReciever(Message message, User user) {
		List<User> recievers = null;
		if (message.getRecievers() == null) {
			recievers = new ArrayList<>();
		} else {
			recievers = message.getRecievers();
		}
		recievers.add(user);
		message.setRecievers(recievers);
		return message;
	}

	@Override
	public Message addSender(Message message, User user) {
		message.setSender(user);
		return message;
	}

	@Override
	public void createAndSendGroupBetInvitationMessage(User sender, List<User> recievers, GroupBet groupBet) {

		Message message = new Message();
		message.setSender(sender);
		message.setRecievers(recievers);
		message.setTitle("You have group bet request from: " + sender.getUsername());
		message.setContent("User " + sender.getUsername() + " wants You to join in group betting! Possible win: "
				+ betService.calculatePossilbeWinInGroupBet(groupBet) + " with the amount: "
				+ groupBet.getJoinedAmount());
		sendMessage(message);

	}

	@Override
	public void createUserRequestInvitation(User sender, List<User> recievers) {
		Message message = new Message();
		message.setSender(sender);
		message.setRecievers(recievers);
		message.setTitle("Friend request from: " + sender.getUsername());
		message.setContent("User " + sender.getUsername()
				+ " wants to be on Your friends list. Would You accept? Check Your invites to accept or discard the invite");
		sendMessage(message);
	}

	@Override
	public void createInvitationAcceptedMessage(User sender, User reciever) {
		Message message = new Message();
		message.setTitle("Invitation accepted");
		message.setContent("Your invite has been accepted: " + sender.getUsername()
				+ " is Your new friends. You can now send messages to each other and invite to group bets.");
		message.setSender(sender);
		message = addReciever(message, reciever);
		sendMessage(message);
	}

}
