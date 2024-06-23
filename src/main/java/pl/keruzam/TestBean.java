package pl.keruzam;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

@ViewScoped
@Named
public class TestBean {

	private String message = "Hello from Spring!";
	private String inputText;
	private String output;

	public void submit() {
		output = "You entered: " + inputText;
	}

	// Getters and setters
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInputText() {
		return inputText;
	}

	public void setInputText(String inputText) {
		this.inputText = inputText;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public void doSomething() {
		//PrimeFaces.current().executeScript("PF('growl').renderMessage({summary:'Test', detail:'PrimeFaces is working!', severity:'info'});");
	}
}
