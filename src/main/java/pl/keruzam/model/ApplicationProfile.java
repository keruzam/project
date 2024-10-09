package pl.keruzam.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import pl.keruzam.service.cmm.AbstractEntity;
import pl.keruzam.service.cmm.StreamSequenceGenerator;

/**
 * @author Paweł Niedzielan
 */
@Entity
@Table(name = "application_profile")
public class ApplicationProfile extends AbstractEntity {

	public static final String CURRENCY_RATE_LAST_SAVE_DATE = "currencyRate.lastSaveDate";
	public static final String IS_DOMAIN_CHECKER_ACTIVE = "isDomainCheckerActive";

	public static final Long CURRENCY_RATE_LAST_SAVE_DATE_ID = 1L;
	public static final Long TIMERS_SEMAPTHORE = 2L;
	public static final Long ACCOUNT_PAYMENT_SEMAPHORE = 3L;
	public static final Long COUNTER_ADLEADER = 4L;

	@Id
	@GenericGenerator(
			name = "s_application_profile", type = StreamSequenceGenerator.class, parameters = { @Parameter(
			name = "sequence_name", value = "s_application_profile") })
	@GeneratedValue(generator = "s_application_profile")
	@Column(name = "id")
	private Long id;

	@Column(name = "key_name")
	private String keyName;

	@Column(name = "key_value")
	private String keyValue;

	public ApplicationProfile() {
	}

	public ApplicationProfile(final Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(final String keyName) {
		this.keyName = keyName;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(final String keyValue) {
		this.keyValue = keyValue;
	}

	/**
	 * @author Mirek Szajowski
	 */
	public static class Propery {
		public static final String KEY_NAME = "keyName";
	}

}
