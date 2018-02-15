package pl.coderslab.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Request that is send to other users to join the {@link GroupBet}
 * 
 * It is related with {@link GroupBet} with many to one relationship. This
 * request can have only one sender and one receiver. Request status corresponds
 * to whether is active (user has not accept or discard it yet) or not.
 * 
 * @author dianinha
 *
 */
@Entity
public class GroupBetRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "request_id")
	protected long id;

	@ManyToOne
	@JoinColumn
	protected User sender;

	/**
	 * Sorry I misspelled that
	 */
	@ManyToOne
	@JoinColumn
	protected User reciever;

	protected boolean status;

	/**
	 * Again, useless for now.
	 */
	@Column
	private String betCode;

	@ManyToOne
	@JoinColumn
	private GroupBet groupBet;

	public GroupBetRequest() {
		super();
	}

	public String getBetCode() {
		return betCode;
	}

	public void setBetCode(String betCode) {
		this.betCode = betCode;
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

	public GroupBet getGroupBet() {
		return groupBet;
	}

	public void setGroupBet(GroupBet groupBet) {
		this.groupBet = groupBet;
	}

}
