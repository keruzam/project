package pl.keruzam.service.cmm.context;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.inject.Inject;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.keruzam.model.CompanyRepository;
import pl.keruzam.model.Operator;
import pl.keruzam.service.cmm.OperatorPermission;
import pl.keruzam.service.cmm.OperatorRole;

/**
 * Obiekt kontekstu zalogowanego operatora
 *
 * @author Mirek Szajowski
 */
@Component("operatorContext")
public class OperatorContextImpl implements OperatorContext {

	private static final String DIRECTORY_POSTFIX = "_dir";

	private static final String IS_SETTLEMENT_VISIBLE_FOR_ALL_KEY = "isSettlementVisibleForAll";
//	@Value("${mail.from}")
//	String mailSender;
	String login;
	Long idCurrentCompany = OperatorContext.UNKNOWN;
	Long idCurrentCompanyHistory = OperatorContext.UNKNOWN;
	Map<String, Object> contextData = new HashMap<String, Object>();
	MultiMap attachmentMap = new MultiValueMap();

	@Inject
	private ApplicationContext applicationContext;
	@Inject
	private PermissionContext permissionContext;
	@Inject
	private CompanyRepository companyRepository;

	private Long idOperator = OperatorContext.UNKNOWN;
	private Long idAccount = OperatorContext.UNKNOWN;
	private Long idAccountOwner = OperatorContext.UNKNOWN;
	private List<Long> idsCompanies = new ArrayList<Long>();

	private List<OperatorPermission> permissions;
	private OperatorRole role;


	public static OperatorContextImpl create() {
		return ContextBeansFactory.createOperatorContextProxy();
	}



	private File getFileDir() {
		String fileDir = applicationContext.getFileDir();
		File file = new File(fileDir + idAccount + File.separator + idCurrentCompany);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	private File getAttachmentDirOrderByCompany(final String baseCatalogName, final String filename) {
		String fileDir = applicationContext.getFileDir();
		File file = new File(
				fileDir + idAccount + File.separator + idCurrentCompany + DIRECTORY_POSTFIX + File.separator + baseCatalogName + File.separator + filename);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}


	private File getAttachmentDirOrderByCompany(final String baseCatalogName, final Long idSource, final String filename) {
		String fileDir = applicationContext.getFileDir();
		File file = new File(
				fileDir + idAccount + File.separator + idCurrentCompany + DIRECTORY_POSTFIX + File.separator + baseCatalogName + File.separator + idSource.toString() + File.separator + filename);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	private File getAttachmentDirOrderByOperator(final String baseCatalogName, final String filename) {
		String fileDir = applicationContext.getFileDir();
		File file = new File(
				fileDir + idAccount + File.separator + baseCatalogName + File.separator + idOperator + DIRECTORY_POSTFIX + File.separator + filename);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	private File getFacsimileAndStampDir() {
		String fileDir = applicationContext.getFileDir();
		File file = new File(fileDir + idAccount + File.separator + idCurrentCompany + DIRECTORY_POSTFIX + File.separator + idOperator);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	private File getPreventiveStampDir() {
		String fileDir = applicationContext.getFileDir();
		File file = new File(fileDir + idAccount + File.separator + idCurrentCompany + "stamp");
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	@Override
	public void setOperatorBasicData(final Operator operator) {
		this.login = operator.getEmail();
		this.idOperator = operator.getId();
	}

	@Override
	public void setOperatorContext(final Operator operator) {
		setOperatorBasicData(operator);
		Long idCurrentCompany = 1L;// findCompanyId();
	}

	@Override
	public void setOperatorContext(final Long idOperator, final Long idCompany) {
		Operator operator = companyRepository.load(Operator.class, idOperator);
		setOperatorBasicData(operator);
	}









	private void setPermissionContext(final Operator operator) {
		permissionContext.setPermissionContext(8L, Arrays.asList(operator.getPermissions()),
				OperatorRole.ADMIN.equals(role));
	}





	private boolean isIdCurrentCompanySet() {
		return idCurrentCompany != null && !UNKNOWN.equals(idCurrentCompany);
	}











	@Override
	public Boolean isSetOperatorContext() {
		return getIdOperator() != null && !getIdOperator().equals(-1L);
	}






	@Override
	public Long getIdOperator() {
		return idOperator;
	}

	@Override
	public void setIdOperator(final Long idOperator) {
		this.idOperator = idOperator;
	}

	@Override
	public String getLogin() {
		return login;
	}

	@Override
	public void setIdCurrentCompany(Long idCurrentCompany) {

	}

	@Override
	public Boolean isVatPayer() {
		return null;
	}

	public Long getIdAccountOwner() {
		return idAccountOwner;
	}

	@Override
	public boolean isSystemOperator() {
		return Operator.SYSTEM.equals(idOperator);
	}


	@Override
	public void put(final String name, final Object value) {
		contextData.put(name, value);
	}

	@Override
	public Object get(final String name) {
		return contextData.get(name);
	}

	@Override
	public String getFileDirAbsolutePath() {
		return getFileDir().getAbsolutePath();
	}

	@Override
	public String getFacsimileAndStampDirAbsolutePath() {
		return getFacsimileAndStampDir().getAbsolutePath();
	}

	@Override
	public String getPreventiveStampDirAbsolutePath() {
		return getPreventiveStampDir().getAbsolutePath();
	}

	@Override
	public Boolean isCompanyIntegrateWithAla() {
		return null;
	}

	@Override
	public boolean hasRights(final OperatorPermission permission) {
		if (isAdmin()) {
			return true;
		}
		for (OperatorPermission operatorPermission : permissions) {
			if (operatorPermission.toString().contains(permission.toString())) {
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean wasChangeInVatPayer() {
		return null;
	}

	@Override
	public boolean isAdmin() {
		return OperatorRole.ADMIN == role;
	}


	@Override
	public void cleanContext() {
		this.login = null;
		this.idOperator = null;
		this.idAccount = null;
		this.idAccountOwner = null;
		this.idCurrentCompanyHistory = null;
		this.idsCompanies = null;
		this.permissions = null;
		this.role = null;
		this.idCurrentCompany = null;

		// this.accountingContext.clear();
	}





	private Boolean checkCanViewOnlyOwnDocuments(final OperatorPermission permission) {
		Operator operator = getOperator();
		if (operator != null) {
			if (operator.getIsAdmin()) {
				return Boolean.FALSE;
			} else {
				for (OperatorPermission operatorPermission : operator.getPermissions()) {
					if (permission.equals(operatorPermission)) {
						return Boolean.TRUE;
					}
				}
			}
		}
		return Boolean.FALSE;
	}

	private Operator getOperator() {
		return companyRepository.load(Operator.class, getIdOperator());
	}







	@Override
	public String getCurrentOperatorFirstAndLastName() {
		Operator operator = companyRepository.load(Operator.class, getIdOperator());
		return operator.getFirstAndLastName();
	}

	@Override
	public Boolean getIsPaidAccount() {
		return null;
	}

	@Override
	public Boolean getIsFreeAccount() {
		return null;
	}

	@Override
	public boolean getIsDemoAccount() {
		return false;
	}

	@Override
	public MultiMap getAttachmentMap() {
		return attachmentMap;
	}



	@Override
	public Boolean getIsPaidOrDemoAccount() {
		return getIsPaidAccount() || getIsDemoAccount();
	}



}