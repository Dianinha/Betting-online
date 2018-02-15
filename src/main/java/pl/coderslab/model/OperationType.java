package pl.coderslab.model;

/**
 * Represents types of financial operations that can be done on {@link User}
 * {@link Wallet}
 * 
 * There are four basics operation types:
 * <ul>
 * <li>add funds to wallet</li>
 * <li>withdraw money</li>
 * <li>bet placing</li>
 * <li>finalizing bet price</li>
 * </ul>
 * 
 * 
 * 
 * @author dianinha
 *
 */
public enum OperationType {

	ADD_FUNDS, WITHDRAWN, PLACE_BET, BET_PRIZE
}
