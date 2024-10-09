package pl.keruzam.service.cmm;

import java.io.Serializable;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Bazowa klasa dto
 *
 * @author Mirek Szajowski
 */

public abstract class AbstractDto implements Serializable {

	private Timestamp version;

	public abstract Long getId();

	public Timestamp getVersion() {
		return version;
	}

	public void setVersion(final Timestamp version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
