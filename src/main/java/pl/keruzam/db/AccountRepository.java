package pl.keruzam.db;

import org.hibernate.query.Query;
import pl.keruzam.model.Operator;
import pl.keruzam.service.cmm.stereotypes.Repository;

/**
 * Repozytorium konta (Account)
 *
 * @author Tomasz Mazurek
 */
@Repository
public class AccountRepository extends AbstractRepository {

	private static final String FIND_OPERATOR_BY_EMAIL = "Account.findOperatorByEmail";

	public Operator findByEmail(final String email) {
		Query query = dm.getNamedQuery(FIND_OPERATOR_BY_EMAIL);
		query.setParameter("email", email.toLowerCase().trim());
		return (Operator) query.uniqueResult();
	}

}
