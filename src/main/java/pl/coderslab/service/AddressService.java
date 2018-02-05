package pl.coderslab.service;

import pl.coderslab.model.Address;
import pl.coderslab.model.User;

public interface AddressService {

	Address create(Address address, User user);
	
	Address save (Address address);
}
