import jakarta.inject.Inject;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import pl.keruzam.model.Transaction;
import pl.keruzam.service.DbService;

import java.math.BigDecimal;
import java.util.Date;

@ContextConfiguration(locations = "classpath:/spring/context.xml")
public class DbServiceTest extends AbstractTestNGSpringContextTests {

	@Inject
	private DbService dbService;

	@Test
	public void testSave() {
		Transaction transaction = new Transaction();
		transaction.setNote("test");
		transaction.setQuota(new BigDecimal("12.21"));
		transaction.setOperationDate(new Date());
		transaction.setOrderDate(new Date());

		Long id = dbService.save(transaction);

		Assert.assertNotNull(id);

		Transaction loaded = dbService.load(id);

		Assert.assertEquals(loaded.getQuota(), BigDecimal.valueOf(12.21));
	}
}
