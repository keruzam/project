package pl.keruzam.service;

import jakarta.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class ServiceExample {

	@Inject
	Service2Example service2Example;

	public String getTextFromService2() {
		return service2Example.getText();
	}

}
