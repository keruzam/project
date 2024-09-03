import jakarta.inject.Inject;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import pl.keruzam.model.BankTransaction;
import pl.keruzam.service.DbService;

import java.math.BigDecimal;
import java.util.Date;

@ContextConfiguration(locations = "classpath:/spring/context.xml")
public class DbServiceTest extends AbstractTestNGSpringContextTests {

	@Inject
	private DbService dbService;

	@Test
	public void testSave() {
		BankTransaction bankTransaction = new BankTransaction();
		bankTransaction.setNote("test");
		bankTransaction.setQuota(new BigDecimal("12.21"));
		bankTransaction.setOperationDate(new Date());
		bankTransaction.setOrderDate(new Date());

		Long id = dbService.save(bankTransaction);

		Assert.assertNotNull(id);

		BankTransaction loaded = dbService.load(id);

		Assert.assertEquals(loaded.getQuota(), BigDecimal.valueOf(12.21));

		//dbService.delete(loaded.getId());
	}
}
