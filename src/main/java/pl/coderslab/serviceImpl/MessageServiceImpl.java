package pl.coderslab.serviceImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Message;
import pl.coderslab.model.User;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.service.MessageService;

@Service
public class MessageServiceImpl implements MessageService {

	@Autowired
	private MessageRepository messageRepository;

	@Override
	public Set<Message> findMessagesByReciever(User user) {
		List<Message> listMessage = messageRepository.findAllByRecieversIs(user);
		return new HashSet<>(listMessage);
	}

	@Override
	public List<Message> findMessagesBySender(User user) {
		return messageRepository.findAllBySender(user);
	}

	@Override
	public Message sendMessage(Message message) {
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

}
