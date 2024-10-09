package pl.keruzam.model;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.apache.webbeans.util.StringUtil;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import pl.keruzam.service.cmm.AbstractEntity;
import pl.keruzam.service.cmm.OperatorPermission;
import pl.keruzam.service.cmm.OperatorRole;
import pl.keruzam.service.cmm.StreamSequenceGenerator;
import pl.keruzam.service.cmm.lib.Date;
import pl.keruzam.service.cmm.lib.Encoder;
import pl.keruzam.service.operator.OperatorDto;

/**
 * @author Bartek Jasik
 */
@Entity
@Table(name = "operator")
public class Operator extends AbstractEntity {

	public static final Long SYSTEM = -1L;
	public static final String LOGIN = "login";
	public static final String EMAIL = "email";

	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@Column(name = "active")
	private Boolean active;

	@Column(name = "creation_date")
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "creation_date")) })
	private Date creationDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", columnDefinition = "enum('ADMIN','USER')")
	private OperatorRole role = OperatorRole.USER;

	@Column(name = "last_login_date")
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "value", column = @Column(name = "last_login_date")) })
	private Date lastLoginDate;

	@Column(name = "last_ip_address")
	private String lastIpAddress;

	@Column(name = "permissions")
	private String permissions;

	public Operator() {
	}

	public Operator(final Long id) {
		this.id = id;
	}

	public Operator(final OperatorDto dto) {
		apply(dto);
		this.creationDate = Date.getCurrentDateAndTime();
	}

	@Id
	@GenericGenerator(
			name = "s_operator", type = StreamSequenceGenerator.class, parameters = { @Parameter(
			name = "sequence_name", value = "s_operator") })
	@GeneratedValue(generator = "s_operator")
	@Column(name = "id")
	@Override
	@Access(AccessType.PROPERTY)

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getLastIpAddress() {
		return lastIpAddress;
	}

	public void setLastIpAddress(final String lastIpAddress) {
		this.lastIpAddress = lastIpAddress;
	}

	public OperatorRole getRole() {
		return role;
	}

	public void setRole(final OperatorRole role) {
		this.role = role;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = Encoder.encode(password, getEmail());
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	public OperatorPermission[] getPermissions() {
		List<OperatorPermission> result = new ArrayList<OperatorPermission>();
		if (permissions != null) {
			for (String token : Splitter.on(",").split(permissions)) {
				result.add(OperatorPermission.valueOf(token));
			}

		}
		return result.toArray(new OperatorPermission[result.size()]);
	}

	private void setPermissions(final String permissions) {
		if (StringUtil.isEmpty(permissions)) {
			this.permissions = null;
		} else {
			this.permissions = permissions;
		}
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public OperatorDto createDto() {
		OperatorDto dto = new OperatorDto();
		dto.setId(id);
		dto.setFirstName(firstName);
		dto.setLastName(lastName);
		dto.setPassword(password);
		dto.setEmail(email);
		dto.setActive(active);
		dto.setRole(role);
		dto.setPermisions(getPermissions());
		dto.setCreationDate(creationDate);
		dto.setLastLoginDate(lastLoginDate);
		dto.setLastIpAddress(lastIpAddress);
		return dto;
	}

	public void apply(final OperatorDto dto) {
		if (dto.getId() != null) {
			setId(dto.getId());
		}
		applyWithoutPassword(dto);
		setPassword(dto.getPassword());
	}

	private boolean isEmpty(final String facebookKey) {
		return StringUtil.isEmpty(facebookKey);
	}

	/**
	 * Edytuje operatora bez zmiany jego aktualnego hasła
	 */
	public void applyWithoutPassword(final OperatorDto dto) {
		setFirstName(dto.getFirstName());
		setLastName(dto.getLastName());
		setEmail(dto.getEmail());
		setActive(dto.getActive());
		setRole(dto.getRole());
		OperatorPermission[] permisions = dto.getPermisions();
		if (permisions != null) {
			setPermissions(Joiner.on(",").join(permisions));
		}

	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(final Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public String getFirstAndLastName() {
		return firstName + " " + lastName;
	}

	public boolean getIsAdmin() {
		return OperatorRole.ADMIN == this.role;
	}

}