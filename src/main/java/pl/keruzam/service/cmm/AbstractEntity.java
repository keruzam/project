package pl.keruzam.service.cmm;

import java.io.Serializable;
import java.sql.Timestamp;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OptimisticLockException;
import jakarta.persistence.Version;

/**
 * Bazowa encja
 *
 * @author Mirek Szajowski
 */
@MappedSuperclass
@Access(AccessType.FIELD)
public abstract class AbstractEntity implements Serializable {

	public static final String ID = "id";

	public static final int SEQ_START = 1000;
	public static final int SEQ_INCREMENT_BY = 1;
	@Version
	@Column(name = "version")
	Timestamp version;

	//	@Transient
	//	protected BaseRepository repo = ProxyCreator.createProxy(BaseRepository.class);

	//	@Transient
	//	protected OperatorContext operatorContext = ProxyCreator.createProxy(OperatorContext.class);

	public abstract Long getId();

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
		if (!(obj instanceof AbstractEntity)) {
			return false;
		}
		AbstractEntity entity = (AbstractEntity) obj;
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
