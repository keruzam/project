package pl.keruzam.service;


import jakarta.inject.Inject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import pl.keruzam.model.BankTransaction;

@Service
public class DbService {

	@Inject
	SessionFactory sessionFactory;

	public Long save(BankTransaction bankTransaction) {
		Session session = openSession();
		org.hibernate.Transaction hibernateTransaction = null;
		Long id = null;
		try {
			if (session != null) {
				hibernateTransaction = session.beginTransaction();
				session.persist(bankTransaction);
				hibernateTransaction.commit();
				id = bankTransaction.getId();
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


	public BankTransaction load(Long id) {
		Session session = openSession();
		try {
			if (session != null) {
				return session.get(BankTransaction.class, id);
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

	public void delete(Long id) {
		Session session = openSession();
		org.hibernate.Transaction hibernateTransaction = null;
		try {
			if (session != null) {
				hibernateTransaction = session.beginTransaction();
				BankTransaction bankTransaction = session.get(BankTransaction.class, id);
				session.remove(bankTransaction);
				hibernateTransaction.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			hibernateTransaction.rollback();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
