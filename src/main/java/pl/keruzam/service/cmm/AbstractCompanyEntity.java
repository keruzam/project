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

/**
 * Bazowa encja dla tabel ktore zawieraja dane firmo centryczne
 *
 * @author Mirek Szajowski
 */
@FilterDef(
		name = AbstractCompanyEntity.COMPANY_FILTER, parameters = @ParamDef(name = AbstractCompanyEntity.ID_COMPANY_FIELD, type = long.class))
@Filters(@Filter(name = AbstractCompanyEntity.COMPANY_FILTER, condition = "id_company = :idCompany"))
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractCompanyEntity implements Serializable {

	public static final int SEQ_START = 1000;
	public static final int ALLOCATION_SIZE = 1;

	public static final String COMPANY_FILTER = "companyFilter";
	public static final String ID_COMPANY_FIELD = "idCompany";

	@Transient
	protected BaseRepository repo = ProxyCreator.createProxy(BaseRepository.class);

	@Column(name = "id_company", nullable = false, updatable = false)
	Long idCompany;
	@Version
	@Column(name = "version")
	Timestamp version;

	public Long getIdCompany() {
		return idCompany;
	}

	public void setIdCompany(final Long idCompany) {
		this.idCompany = idCompany;
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
		if (!(obj instanceof AbstractCompanyEntity)) {
			return false;
		}
		AbstractCompanyEntity entity = (AbstractCompanyEntity) obj;
		if (this.getId() == null && entity.getId() == null) {
			return true;
		}
		if (this.getId() == null || entity.getId() == null) {
			return false;
		}
		return this.getId().equals(entity.getId());
	}

	public abstract Long getId();

	@Override
	public int hashCode() {
		if (this.getId() == null) {
			return super.hashCode();
		}
		return this.getId().hashCode();
	}
}
