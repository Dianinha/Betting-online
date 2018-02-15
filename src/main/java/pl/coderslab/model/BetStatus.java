package pl.coderslab.model;

/**
 * Simple {@link Enum} for possible bets statuses.
 * 
 * <p>
 * Some of the statuses are not for every type of bet. For example:
 * <ul>
 * <li>{@code NOT_APPROVED} is only for {@link SingleBet}</li>
 * <li>{@code PLACED} is for all bets: {@link SingleBet}, {@link MultipleBet},
 * {@link GroupBet}</li>
 * <li>{@code FINALIZED} is for all bets: {@link SingleBet},
 * {@link MultipleBet}, {@link GroupBet}</li>
 * <li> {@code ENDED_IN_MULTIBET} is only for @{@link SingleBet} that is placed in {@link MultipleBet}</li>
 * <li> {@code ENDED_IN_GROUP_BET} is only for @{@link SingleBet} that is placed in {@link GROUP_BET}</li>
 * </ul>
 * </p>
 * 
 * @author dianinha
 *
 */
public enum BetStatus {

	NOT_APPROVED, PLACED, FINALIZED, ENDED_IN_MULTIBET, ENDED_IN_GROUP_BET
}
