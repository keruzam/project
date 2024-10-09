package pl.keruzam.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Parameter;
import pl.keruzam.service.cmm.AbstractEntity;
import pl.keruzam.service.cmm.OperatorProfileDto;
import pl.keruzam.service.cmm.StreamSequenceGenerator;

/**
 * @author Bartek Jasik
 */

//@NaturalIdCache(region = "pl.keruzam.OperatorProfile-naturalId")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Entity
@Table(name = "operator_profile")
public class OperatorProfile extends AbstractEntity {

	public static final String FIELD_KEY_NAME = "keyName";
	public static final String FIELD_OPERATOR = "operator";

	@Id
	@GenericGenerator(
			name = "s_operator_profile", type = StreamSequenceGenerator.class, parameters = { @Parameter(
			name = "sequence_name", value = "s_operator_profile") })
	@GeneratedValue(generator = "s_operator_profile")
	@Column(name = "id")
	private Long id;

	@NaturalId
	@Column(name = "key_name")
	private String keyName;

	@Column(name = "key_value")
	private String keyValue;

	@NaturalId
	@JoinColumn(name = "id_operator", referencedColumnName = "id")
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Operator operator;

	public OperatorProfile() {
	}

	public OperatorProfile(final Long id) {
		this.id = id;
	}

	public OperatorProfile(final OperatorProfileDto dto) {
		apply(dto);
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

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(final Operator operator) {
		this.operator = operator;
	}

	public OperatorProfileDto createDto() {
		OperatorProfileDto dto = new OperatorProfileDto();
		dto.setId(id);
		dto.setKeyName(keyName);
		dto.setKeyValue(keyValue);
		return dto;
	}

	public void apply(final OperatorProfileDto dto) {
		setId(dto.getId());
		setKeyName(dto.getKeyName());
		setKeyValue(dto.getKeyValue());
	}
}
