import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

@ContextConfiguration(locations = "classpath:/WEB-INF/context.xml")
public class HibernateConnectionTest extends AbstractTestNGSpringContextTests {

	@Inject
	private SessionFactory sessionFactory;

	@Test
	public void testConnection() {
		Session session = null;
		try {
			session = sessionFactory.openSession();
			Assert.assertNotNull(session);
			System.out.println("Successfully connected to the database!");
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Failed to connect to the database!");
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
