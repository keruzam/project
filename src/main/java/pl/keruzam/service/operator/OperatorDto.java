package pl.keruzam.service.operator;

import jakarta.validation.constraints.Size;
import pl.keruzam.service.cmm.AbstractDto;
import pl.keruzam.service.cmm.AccountCreationSource;
import pl.keruzam.service.cmm.OperatorPermission;
import pl.keruzam.service.cmm.OperatorRole;
import pl.keruzam.service.cmm.lib.ConstMaxSize;
import pl.keruzam.service.cmm.lib.Date;
import pl.keruzam.service.cmm.lib.Email;
import pl.keruzam.service.cmm.lib.NotNull;

/**
 * Dto Operatora
 *
 * @author Tomasz Mazurek
 *
 *
 */

public class OperatorDto extends AbstractDto {

	Long id;
	@NotNull
	@Size(max = ConstMaxSize.FIRST_NAME)
	String firstName;
	@NotNull
	@Size(max = ConstMaxSize.LAST_NAME)
	String lastName;
	@NotNull
	@Email
	@Size(max = ConstMaxSize.EMAIL)
	String email;
	@NotNull
	@Size(max = ConstMaxSize.PASSWORD)
	String password;
	@NotNull
	Boolean active = Boolean.TRUE;
	String promotionCodeName;
	Date creationDate;
	Date lastLoginDate;
	String lastIpAddress;
	Boolean passwordExpired = Boolean.FALSE;

	Long idAccount;
	Long[] companies;
	OperatorPermission[] permisions;
	OperatorRole role = OperatorRole.USER;
	AccountCreationSource creationSource = AccountCreationSource.D;
	Long idAccountingOffice;
	String facebookKey;
	String googleKey;

	Long idEmailSettings;

	Long[] stores;

	public OperatorDto(final String login, final String pass) {
		this.email = login.replaceAll(" ", "");
		this.password = pass;
	}

	public OperatorDto(final Long id, final String login, final Date lastLoginDate) {
		this.email = login.replaceAll(" ", "");
		this.id = id;
		this.lastLoginDate = lastLoginDate;
	}

	public OperatorDto() {
	}

	public Long getIdAccount() {
		return idAccount;
	}

	public void setIdAccount(final Long idAccount) {
		this.idAccount = idAccount;
	}

	@Override
	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
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
		this.email = email.replaceAll(" ", "");
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(final Boolean active) {
		this.active = active;
	}

	public void setCompanies(final Long[] array) {
		this.companies = array;
	}

	public Long[] getCompanies() {
		return companies;
	}

	public OperatorRole getRole() {
		return role;
	}

	public void setRole(final OperatorRole role) {
		this.role = role;
	}

	public Boolean getIsAdmin() {
		return role.equals(OperatorRole.ADMIN);
	}

	public void setIsAdmin(final Boolean isAdmin) {
		if (isAdmin) {
			setRole(OperatorRole.ADMIN);
		} else {
			setRole(OperatorRole.USER);
		}

	}

	public AccountCreationSource getCreationSource() {
		return creationSource;
	}

	public void setCreationSource(final AccountCreationSource creationSource) {
		this.creationSource = creationSource;
	}

	public String getPromotionCodeName() {
		return promotionCodeName;
	}

	public void setPromotionCodeName(final String promotionCodeName) {
		this.promotionCodeName = promotionCodeName;
	}

	public OperatorPermission[] getPermisions() {
		return permisions;
	}

	public void setPermisions(final OperatorPermission[] permisions) {
		this.permisions = permisions;
	}

	public Long getIdAccountingOffice() {
		return idAccountingOffice;
	}

	public void setIdAccountingOffice(final Long idAccountingOffice) {
		this.idAccountingOffice = idAccountingOffice;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(final Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Boolean getPasswordExpired() {
		return passwordExpired;
	}

	public void setPasswordExpired(final Boolean passwordExpired) {
		this.passwordExpired = passwordExpired;
	}

	public Long getIdEmailSettings() {
		return idEmailSettings;
	}

	public void setIdEmailSettings(final Long idEmailSettings) {
		this.idEmailSettings = idEmailSettings;
	}

	public String getFacebookKey() {
		return facebookKey;
	}

	public void setFacebookKey(final String facebookKey) {
		this.facebookKey = facebookKey;
	}

	public String getGoogleKey() {
		return googleKey;
	}

	public void setGoogleKey(final String googleKey) {
		this.googleKey = googleKey;
	}

	public String getLastIpAddress() {
		return lastIpAddress;
	}

	public void setLastIpAddress(final String lastIpAddress) {
		this.lastIpAddress = lastIpAddress;
	}

	public Long[] getStores() {
		return stores;
	}

	public void setStores(final Long[] stores) {
		this.stores = stores;
	}

}
