package pl.coderslab.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pl.coderslab.model.Address;
import pl.coderslab.model.User;
import pl.coderslab.repositories.AddressRepository;
import pl.coderslab.service.AddressService;

@Service
public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepository;

	@Override
	public Address create(Address address, User user) {
		address.setUser(user);
		return addressRepository.save(address);
	}

	@Override
	public Address save(Address address) {
		return addressRepository.save(address);
	}

	@Override
	public Address findByUser(User user) {
		return addressRepository.findByUser(user);
	}

}
