package pl.keruzam;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.PrimeFaces;
import pl.keruzam.model.BankTransaction;
import pl.keruzam.service.DbService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ViewScoped
@Named
public class TestBean {

	@Inject
	DbService dbService;

	private String message = "Hello from Spring!";
	private String inputText;
	private String output;

	public void submit() {
		addTransaction();
		this.output = "You entered: " + inputText;
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
		PrimeFaces.current().executeScript("PF('growl').renderMessage({summary:'Test', detail:'PrimeFaces is working!', severity:'info'});");
	}

	public void addTransaction() {
		BankTransaction bankTransaction = new BankTransaction();
		bankTransaction.setQuota(BigDecimal.valueOf(123));
		bankTransaction.setOrderDate(new Date());
		bankTransaction.setOperationDate(new Date());
		bankTransaction.setNote("notatka");

		dbService.save(bankTransaction);
	}

	public List<BankTransactionRow> getBankTransactions() {
		List<BankTransactionRow> list = new ArrayList<>();
		list.add(new BankTransactionRow("opis", "123.33"));
		list.add(new BankTransactionRow("opis2", "333.33"));
		list.add(new BankTransactionRow("inny opis", "1 323.66"));
		return list;
	}
}
