package pl.coderslab.service;

import pl.coderslab.model.Operation;
import pl.coderslab.model.OperationType;

public interface OparationService {

	Operation save(Operation operation);
	
	Operation create(OperationType type);
}
