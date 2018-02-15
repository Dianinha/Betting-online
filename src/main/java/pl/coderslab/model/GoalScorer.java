package pl.coderslab.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Class with almost no use for now.
 * 
 * <p>
 * Possible future modifications:
 * <ul>
 * <li>show goal scorers in live events</li>
 * <li> let user see the goal scorers in past games</li>
 * <li>calculate statistics for {@link Player} with this data</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
@Entity
public class GoalScorer {

	@Id
	private String id;

	private String time;

	@ManyToOne
	@JoinColumn
	private Player homeScorer;

	@ManyToOne
	@JoinColumn
	private Player awayScorer;

	private String score;

	@ManyToOne
	@JoinColumn
	private Event event;

	public GoalScorer() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Player getHomeScorer() {
		return homeScorer;
	}

	public void setHomeScorer(Player homeScorer) {
		this.homeScorer = homeScorer;
	}

	public Player getAwayScorer() {
		return awayScorer;
	}

	public void setAwayScorer(Player awayScorer) {
		this.awayScorer = awayScorer;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	@Override
	public String toString() {
		return "GoalScorer [id=" + id + ", time=" + time + ", homeScorer=" + homeScorer + ", awayScorer=" + awayScorer
				+ ", score=" + score + ""
		// + ", event=" + event + "]"
		;
	}

}
