package pl.coderslab.service;

import pl.coderslab.model.Address;
import pl.coderslab.model.User;

public interface AddressService {

	/**Creates and SAVES given Address. Also connects it to given User
	 * 
	 * @param address
	 * @param user
	 * 
	 * @return saved address
	 */
	Address create(Address address, User user);

	/**
	 * Only SAVES the given address.
	 * 
	 * @param address
	 * @return saved address
	 */
	Address save(Address address);

	/**
	 * Finds address by corresponding {@link User}
	 * @param user
	 * @return address from database
	 */
	Address findByUser(User user);
}
