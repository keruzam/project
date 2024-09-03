package pl.keruzam.service;


import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import pl.keruzam.model.Transaction;

@Service
public class DbService {

	@Inject
	SessionFactory sessionFactory;

	public Long save(Transaction transaction) {
		Session session = openSession();
		org.hibernate.Transaction hibernateTransaction = null;
		Long id = null;
		try {
			if (session != null) {
				hibernateTransaction = session.beginTransaction();
				session.persist(transaction);
				hibernateTransaction.commit();
				id = transaction.getId();
			}
		} catch (Exception e) {
			e.printStackTrace();
			hibernateTransaction.rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return id;
	}

	private Session openSession() {
		try {
			System.out.println("return new session");
			return sessionFactory.openSession();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed to connect to the database!");
			return null;
		}
	}


	public Transaction load(Long id) {
		Session session = openSession();
		try {
			if (session != null) {
				return session.get(Transaction.class, id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
		return null;
	}
}
