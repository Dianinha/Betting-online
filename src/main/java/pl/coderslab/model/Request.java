package pl.coderslab.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * This class corresponds to invite to {@link User} friends list.
 * 
 * Possible change : name change to "FriendRequest". When I added
 * {@link GroupBetRequest} the name of this class is very misleading.
 * 
 * @author dianinha
 *
 */
@Entity
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	protected long id;

	@ManyToOne
	@JoinColumn
	protected User sender;

	@ManyToOne
	@JoinColumn
	protected User reciever;

	protected boolean status;

	public Request() {
		super();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReciever() {
		return reciever;
	}

	public void setReciever(User reciever) {
		this.reciever = reciever;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Request [id=" + id + ", sender=" + sender + ", reciever=" + reciever + ", status=" + status + "]";
	}

}
