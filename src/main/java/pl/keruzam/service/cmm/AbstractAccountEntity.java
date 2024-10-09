package pl.keruzam.service.cmm;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;
import pl.keruzam.db.BaseRepository;
import pl.keruzam.service.cmm.context.OperatorContext;

/**
 * Bazowa encja dla tabel ktore zawieraja dane konto centryczne
 *
 * @author Mirek Szajowski
 */

@FilterDef(
		name = AbstractAccountEntity.ACCOUNT_FILTER, parameters = @ParamDef(name = AbstractAccountEntity.ID_ACCOUNT_FIELD, type = long.class))
@Filters(@Filter(name = AbstractAccountEntity.ACCOUNT_FILTER, condition = "id_account = :idAccount"))
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractAccountEntity implements Serializable {

	public static final int SEQ_START = 1000;
	public static final int ALLOCATION_SIZE = 1;

	public static final String ACCOUNT_FILTER = "accountFilter";
	public static final String ID_ACCOUNT_FIELD = "idAccount";
	@Transient
	protected BaseRepository repo = ProxyCreator.createProxy(BaseRepository.class);
	@Transient
	protected OperatorContext operatorContext = ProxyCreator.createProxy(OperatorContext.class);
	@Column(name = "id_account", nullable = false, updatable = false)
	Long idAccount;
	@Version
	@Column(name = "version")
	Timestamp version;

	public abstract Object getId();

	public Long getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(final Long idAccount) {
		this.idAccount = idAccount;
	}

	protected Timestamp getVersion() {
		return version;
	}

	protected void setVersion(final Timestamp version) {
		if (version == null) {
			return;
		}
		if (this.version != null && !version.equals(this.version)) {
			throw new OptimisticLockException();
		}
		this.version = version;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof AbstractAccountEntity)) {
			return false;
		}
		AbstractAccountEntity entity = (AbstractAccountEntity) obj;
		if (this.getId() == null && entity.getId() == null) {
			return true;
		}
		if (this.getId() == null || entity.getId() == null) {
			return false;
		}
		return this.getId().equals(entity.getId());
	}

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return this.getId().hashCode();
	}
}
