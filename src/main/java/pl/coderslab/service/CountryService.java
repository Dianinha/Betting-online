package pl.coderslab.service;

public interface CountryService {
	/**
	 * Creates in database list of countries that I have events for. It should be user ONCE befor deploying the application. 
	 * It is first method that should be called BEFORE the leagues creation.
	 */
	void createCountries();
}
