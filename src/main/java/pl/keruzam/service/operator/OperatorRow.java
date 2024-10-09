package pl.keruzam.service.operator;

import pl.keruzam.service.cmm.OperatorRole;
import pl.keruzam.service.cmm.lib.Date;

/**
 * 
 * Klasa wiersza dla Operatora
 * 
 * @author Tomasz Mazurek
 * 
 * 
 */

public class OperatorRow extends AbstractDisabledRow {

	public static final String EMAIL = "email";

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private Boolean isActive;
	private Date creationDate;
	private Boolean isAdmin;
	private Boolean isOwner;

	public OperatorRow() {
	}

	public OperatorRow(final Long id, final String firstName, final String lastName, final Object role, final Long idOwner,
			final Boolean selected) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.isAdmin = checkRole(role);
		this.isOwner = id.equals(idOwner);
		this.selected = selected;
	}

	public OperatorRow(final Long id, final String firstName, final String lastName, final String email, final Boolean isActive,
			final Date creationDate, final Object role, final Long idOwner) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.isActive = isActive;
		this.creationDate = creationDate;
		this.isAdmin = checkRole(role);
		this.isOwner = id.equals(idOwner);
	}

	private Boolean checkRole(final Object role) {
		String admin = OperatorRole.ADMIN.name().toString();
		String roleStr = role.toString();
		return admin.equals(roleStr) ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(final Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public Boolean getIsOwner() {
		return isOwner;
	}

	@Override
	public String getLabel() {
		if (firstName == null || lastName == null) {
			return email;
		}
		return firstName + " " + lastName;
	}

	@Override
	public Boolean getIsDisabled() {
		return false;
	}
}
