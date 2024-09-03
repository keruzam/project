import jakarta.inject.Inject;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;
import pl.keruzam.service.Service2Example;
import pl.keruzam.service.ServiceExample;

@ContextConfiguration(locations = {"classpath:/spring/context.xml"})
public class SpringContextTest extends AbstractTestNGSpringContextTests {

	@Inject
	private ServiceExample serviceExample;
	@Inject
	private Service2Example service2Example;

	@Test
	public void testInject() {
		String text = service2Example.getText();
		String text2 = serviceExample.getTextFromService2();

		Assert.assertSame(text, text2);
	}

}
