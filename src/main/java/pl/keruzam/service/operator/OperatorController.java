package pl.keruzam.service.operator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ArrayListMultimap;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.inject.Model;
import jakarta.enterprise.inject.Produces;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import org.springframework.stereotype.Controller;
import pl.keruzam.service.OperatorContextWww;
import pl.keruzam.service.cmm.AbstractController;
import pl.keruzam.service.cmm.OperatorPermission;

/**
 * Kontroler Operatora
 *
 * @author Tomasz Mazurek
 */
@Named
@ViewScoped
@Controller
public class OperatorController extends AbstractController {

	public static final String ID_OPERATOR = "idOperator";
	private static final String ALL_FIELDS_REQ = "operator.allFieldsReq";
	private static final String EMAIL_BAD_SYNTAX = "exception.emailBadSyntax";
	private static final String EMAIL_CHANGED_MSG = "operator.emailWasChanged";
	private static final String EMAIL_EXIST_MSG = "simpleRegistrationFail.emailExist";
	private static final String EMAIL_SETTINGS_ERROR = "emailSettings.settingsAreIncorrect";
	private static final String EMAIL_SETTINGS_OK = "emailSettings.settingsAreCorrect";
	private static final String FIELD_EMAIL = "newEmail";
	private static final String MIN_ONE_COMPANY = "operator.assignMinOneCompany";
	private static final String MIN_ONE_STORE_PER_COMPANY = "operator.assignMinOneStore";
	private static final String OPERATORS_PAGE = "/settings/operators";
	private static final String PASSWORD_CHANGED_MSG = "operator.passwordChanged";
	private final ArrayListMultimap<PermisionsGroup, OperatorPermission> permisions = ArrayListMultimap.create();
	@Inject
	private OperatorService operatorService;
	@Inject
	OperatorContextWww operatorContextWww;

	private String newEmail;
	private String oldPassword;
	private String newPassword1;
	private String newPassword2;
	private Boolean isOperatorLoged;
	private Boolean isAccountOwner;
	private Boolean isAbleToAddOperator;
	private Boolean isAdmin;
	private Boolean isCheckAdminDisabled;
	private Boolean isAllCheckboxDisabled;
	private Boolean isOperatorContextAdmin;
	private Boolean isDeactivateAction = Boolean.FALSE;
	private OperatorDto dto;
	private OperatorDto dtoBeforeChanges;

	private Boolean isAdminChangedOnTrue = Boolean.FALSE;


	@Produces
	@Model
	public OperatorDto getOperatorDto() {
		return dto;
	}




	@PostConstruct
	public void init() {
		Long idOperator = getId();
//		dto = idOperator != null ? loadOperatorControllerDto(idOperator) : initDto();
//		dtoBeforeChanges = idOperator != null ? operatorService.load(idOperator) : null;
//		setFlagsForOperator(idOperator);
	}



	private OperatorDto initDto() {
		OperatorDto operatorDto = operatorService.init();
		initBasicPermissions();
		return operatorDto;
	}

	private void initBasicPermissions() {
		List<OperatorPermission> salePurchasePermisions = getSalePurchasePermisions();
		salePurchasePermisions.add(OperatorPermission.COMPANY_CONDITION);
		salePurchasePermisions.add(OperatorPermission.SALE);
		salePurchasePermisions.add(OperatorPermission.ARTICLE);

		List<OperatorPermission> crmPermisions = getCrmPermisions();
		crmPermisions.add(OperatorPermission.CUSTOMER);
		crmPermisions.add(OperatorPermission.CONTACT);
		crmPermisions.add(OperatorPermission.CALENDAR);

		List<OperatorPermission> administrationPermisions = getAdministrationPermisions();
		administrationPermisions.add(OperatorPermission.EXPORT);
		administrationPermisions.add(OperatorPermission.IMPORT);
		administrationPermisions.add(OperatorPermission.ANALYSIS);
	}

	private OperatorDto loadOperatorControllerDto(final Long idOperator) {
		OperatorDto operatorDto = operatorService.load(idOperator);
		initPermissions(operatorDto);
		return operatorDto;
	}

	private void initPermissions(final OperatorDto operatorDto) {
		getSalePurchasePermisions().addAll(getFiltredPermissions(OperatorPermission.getPermisionsGroupSalePurchase(), operatorDto.getPermisions()));
		getAdministrationPermisions().addAll(getFiltredPermissions(OperatorPermission.getPermisionsGroupAdministration(), operatorDto.getPermisions()));
		getCrmPermisions().addAll(getFiltredPermissions(OperatorPermission.getPermisionsGroupCrm(), operatorDto.getPermisions()));
		getModulesPermisions().addAll(getFiltredPermissions(OperatorPermission.getPermisionsGroupModules(), operatorDto.getPermisions()));
		getStorePermisions().addAll(getFiltredPermissions(OperatorPermission.getPermisionsGroupStore(), operatorDto.getPermisions()));
	}

	private List<OperatorPermission> getFiltredPermissions(final List<OperatorPermission> permissionsFromGroup,
			final OperatorPermission[] operatorPermissions) {
		List<OperatorPermission> filtredPermissions = new ArrayList<OperatorPermission>();
		for (OperatorPermission operatorPermission : operatorPermissions) {
			if (permissionsFromGroup.contains(operatorPermission)) {
				filtredPermissions.add(operatorPermission);
			}
		}
		return filtredPermissions;
	}

	private void setFlagsForOperator(final Long idOperator) {
		isAbleToAddOperator = operatorService.getIsAbleToAddOperator();
		isOperatorContextAdmin = operatorContextWww.isAdmin();
		if (idOperator != null) {
			setFlagsForExistOperator(idOperator);
		} else {
			setFlagsForNewOperator();
		}
		isAllCheckboxDisabled = getCompanyCheckboxDisabled();
	}

	private boolean getCompanyCheckboxDisabled() {
		// jezeli zalogowany operator jest wlascicielem konta
		Boolean isAccountOwner = operatorContextWww.getIsAccountOwner();
		if (isAccountOwner) {
			// jezeli id zalogowanego operator to id operator, ktorego chcemy edyotwac
			return isOperatorLoged;
		}
		return true;
	}

	private void setFlagsForNewOperator() {
		isAccountOwner = Boolean.FALSE;
		isOperatorLoged = Boolean.FALSE;
		isCheckAdminDisabled = !isOperatorContextAdmin;
		isAdmin = Boolean.FALSE;
	}

	private void setFlagsForExistOperator(final Long idOperator) {
		isAccountOwner = operatorService.isAccountOwner(idOperator);
		isOperatorLoged = operatorContextWww.getIdOperator().equals(idOperator);
		isCheckAdminDisabled = isAccountOwner || !isOperatorContextAdmin || isOperatorLoged;
		isAdmin = dto.getIsAdmin();
	}

	public String save() throws Exception {
		saveInformation();
		return goTo(OPERATORS_PAGE);
	}

	private void saveInformation() {
		OperatorPermission[] array = getPermissions();
		dto.setPasswordExpired(Boolean.TRUE);
		dto.setPermisions(array);


		operatorService.save(dto);
	}

	private OperatorPermission[] getPermissions() {
		Set<OperatorPermission> uniquePermissions = new HashSet<OperatorPermission>(permisions.values());
		OperatorPermission[] array = uniquePermissions.toArray(new OperatorPermission[uniquePermissions.size()]);
		return array;
	}

	public String update() {
		operatorService.update(dto);
		if (!operatorContextWww.hasRights(OperatorPermission.SETTINGS)) {
			redirectToLogoutPage();
			return "";
		} else {
			return goTo(OPERATORS_PAGE);
		}
	}





	public String delete() {
		// delete(dto.getId());
		if (dto.getActive()) {
			return "";
		} else {
			return activateOperator();
		}
	}



	private String activateOperator() {

		dto.setActive(Boolean.TRUE);

		return update();
	}


	@Override
	public void delete(final Long id) {
		// operatorService.delete(id);
	}

	public void generatePassword() {
		operatorService.generatePassword(dto.getEmail());

	}




	public void closeModal() {
		dto.setEmail(dtoBeforeChanges.getEmail());
	}






	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(final String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword1() {
		return newPassword1;
	}

	public void setNewPassword1(final String newPassword1) {
		this.newPassword1 = newPassword1;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(final String newEmail) {
		this.newEmail = newEmail.replaceAll(" ", "");
	}

	public String getNewPassword2() {
		return newPassword2;
	}

	public void setNewPassword2(final String newPassword2) {
		this.newPassword2 = newPassword2;
	}

	public Boolean isAdmin() {
		return isAdmin;
	}

	public Boolean isAccountOwner() {
		return isAccountOwner;
	}

	public Boolean getIsAbleToAddOperator() {
		return isAbleToAddOperator;
	}

	public Boolean getIsOperatorLoged() {
		return isOperatorLoged;
	}

	public Boolean getIsCheckAdminDisabled() {
		return isCheckAdminDisabled;
	}

	public Boolean getIsAllCheckboxDisabled() {
		return isAllCheckboxDisabled;
	}

	public Boolean getIsOperatorContextAdmin() {
		return isOperatorContextAdmin;
	}

	public List<OperatorPermission> getSalePurchasePermisions() {
		return permisions.get(PermisionsGroup.SALE_PURCHASE);
	}

	public void setSalePurchasePermisions(final List<OperatorPermission> param) {
		List<OperatorPermission> permissions = permisions.get(PermisionsGroup.SALE_PURCHASE);
		if (permissions.contains(OperatorPermission.SALE) && param.contains(OperatorPermission.SALE_OPERATOR)) {
			param.remove(OperatorPermission.SALE);
		}
		if (permissions.contains(OperatorPermission.SALE_OPERATOR) && param.contains(OperatorPermission.SALE)) {
			param.remove(OperatorPermission.SALE_OPERATOR);
		}
		if (permissions.contains(OperatorPermission.PURCHASE) && param.contains(OperatorPermission.PURCHASE_OPERATOR)) {
			param.remove(OperatorPermission.PURCHASE);
		}
		if (permissions.contains(OperatorPermission.PURCHASE_OPERATOR) && param.contains(OperatorPermission.PURCHASE)) {
			param.remove(OperatorPermission.PURCHASE_OPERATOR);
		}
		permisions.get(PermisionsGroup.SALE_PURCHASE).clear();
		permisions.get(PermisionsGroup.SALE_PURCHASE).addAll(param);
	}

	public List<OperatorPermission> getAdministrationPermisions() {
		return permisions.get(PermisionsGroup.ADMINISTRATION);
	}

	public void setAdministrationPermisions(final List<OperatorPermission> param) {
		permisions.get(PermisionsGroup.ADMINISTRATION).clear();
		permisions.get(PermisionsGroup.ADMINISTRATION).addAll(param);
	}

	public List<OperatorPermission> getCrmPermisions() {
		return permisions.get(PermisionsGroup.CRM);
	}

	public void setCrmPermisions(final List<OperatorPermission> param) {
		List<OperatorPermission> permissions = permisions.get(PermisionsGroup.CRM);
		if (permissions.contains(OperatorPermission.CALENDAR) && param.contains(OperatorPermission.CALENDAR_OPERATOR)) {
			param.remove(OperatorPermission.CALENDAR);
		}
		if (permissions.contains(OperatorPermission.CALENDAR_OPERATOR) && param.contains(OperatorPermission.CALENDAR)) {
			param.remove(OperatorPermission.CALENDAR_OPERATOR);
		}
		permisions.get(PermisionsGroup.CRM).clear();
		permisions.get(PermisionsGroup.CRM).addAll(param);
	}

	public List<OperatorPermission> getModulesPermisions() {
		return permisions.get(PermisionsGroup.MODULES);
	}

	public void setModulesPermisions(final List<OperatorPermission> param) {
		List<OperatorPermission> salePurchasePermission = permisions.get(PermisionsGroup.SALE_PURCHASE);
		if (!(salePurchasePermission.contains(OperatorPermission.SALE)) && !(salePurchasePermission.contains(
				OperatorPermission.SALE_OPERATOR)) && param.contains(OperatorPermission.VINDICATION)) {
			param.remove(OperatorPermission.VINDICATION);
		}
		permisions.get(PermisionsGroup.MODULES).clear();
		permisions.get(PermisionsGroup.MODULES).addAll(param);
	}

	public List<OperatorPermission> getStorePermisions() {
		return permisions.get(PermisionsGroup.STORE);
	}

	public void setStorePermisions(final List<OperatorPermission> param) {
		permisions.get(PermisionsGroup.STORE).clear();
		permisions.get(PermisionsGroup.STORE).addAll(param);
	}

	public void permisionssChange() {
	}



	public OperatorDto getDtoBeforeChanges() {
		return dtoBeforeChanges;
	}

	public boolean isShowNameField() {
		return (isViewInLoggedOperator() || isAccountOwner() || isOperatorContextAdmin || isNewOperator()) && dto.getActive();
	}

	public boolean isDeleteButtonRendered() {
		return !isViewInLoggedOperator() && (isAccountOwner() || isOperatorContextAdmin) && !isNewOperator();
	}

	private boolean isViewInLoggedOperator() {
		return operatorContextWww.getIdOperator().equals(dto.getId());
	}

	private boolean isNewOperator() {
		return dto.getId() == null;
	}




	public Boolean getIsAdminChangedOnTrue() {
		return isAdminChangedOnTrue;
	}


	private enum PermisionsGroup {
		SALE_PURCHASE, ADMINISTRATION, CRM, MODULES, STORE
	}
}