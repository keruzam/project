package pl.keruzam.service.operator;



import jakarta.inject.Inject;

import pl.keruzam.db.AccountRepository;
import pl.keruzam.model.Operator;
import pl.keruzam.service.cmm.AbstractApplicationService;
import pl.keruzam.service.cmm.StringUtil;
import pl.keruzam.service.cmm.lib.AuthenticationException;
import pl.keruzam.service.cmm.lib.BusinessException;
import pl.keruzam.service.cmm.lib.Date;
import pl.keruzam.service.cmm.lib.DictionaryBinding;
import pl.keruzam.service.cmm.lib.DictionaryProvider;
import pl.keruzam.service.cmm.lib.Encoder;
import pl.keruzam.service.cmm.lib.PasswordEncoder;
import pl.keruzam.service.cmm.stereotypes.ApplicationService;

/**
 * Serwis aplikacyjny Operatora
 *
 * @author Barłomiej Jasik
 */
@ApplicationService
@DictionaryProvider
@DictionaryBinding({ "idOperator", "idOperatorCreate" })
public class OperatorServiceImpl extends AbstractApplicationService<OperatorDto, OperatorRow> implements OperatorService {

	public static final String GENERATED_PASSWORD = "generated.password";
	private static final String CANT_ADD_NEW_OPERATOR_EXCEPTION = "exception.cantAddNewOperator";
	private static final int IP_ADDRESS_LENGTH = 16;
	private static final String WRONG_EMAIL_EXCEPTION = "exception.wrongEmail";
	private static final String WRONG_PASSWORD_EXCEPTION = "exception.wrongPassword";

	@Inject
	protected AccountRepository accountRepository;

	@Override
	public void login(final String email, final String password) throws AuthenticationException {
		checkIsValuesNotEmpty(email, password);
		Operator operator = accountRepository.findByEmail(email);
		prepareOperatorDataIfPasswordCorrect(operator, password, email);
	}



	@Override
	public boolean checkPassword(final String email, final String password) {
		Operator operator = accountRepository.findByEmail(email);
		if (operator != null) {
			return isCorrectPassword(password, operator);
		}
		return false;
	}

	private void prepareOperatorDataIfPasswordCorrect(final Operator operator, final String password, final String email) {
		if (isCorrectPassword(password, operator)) {
			prepareOperatorApplicationData(operator);
		} else {
			throw new AuthenticationException(email);
		}
	}

	private boolean isCorrectPassword(final String password, final Operator operator) {
		return operator.getPassword().equals(Encoder.encode(password, operator.getEmail()));
	}

	private void checkIsValuesNotEmpty(final String email, final String password) {
		if (emptyValues(email, password)) {
			throw new AuthenticationException(email);
		}
	}

	private boolean emptyValues(final String login, final String password) {
		return StringUtil.isEmpty(login, password);
	}

	@Override
	public OperatorDto findByEmail(final String email) {
		Operator operator = accountRepository.findByEmail(email);
		return operator == null ? null : operator.createDto();
	}


	private String encode(final String key) {
		return PasswordEncoder.encode(key);
	}

	@Override
	protected OperatorDto loadImpl(final Long id) {
		Operator load = accountRepository.load(Operator.class, id);
		return load.createDto();
	}

	@Override
	protected Long saveImpl(final OperatorDto dto) throws BusinessException {
		if (getIsAbleToAddOperator()) {
			return saveOperator(dto);
		} else {
			throw new BusinessException(resourceService.getMessage(CANT_ADD_NEW_OPERATOR_EXCEPTION));
		}
	}

	private Long saveOperator(final OperatorDto dto) {
		Operator operator = new Operator(dto);
		if (operatorContext.isSetOperatorContext()) {
			//sendGeneratedPassword(dto.getEmail(), dto.getPassword(), null, OperatorEmailTemplate.NEW_OPERATOR_SUBJECT);
		}
		saveOperator(dto.getCompanies(), operator);
		return operator.getId();
	}







	private void saveOperator(final Long[] companies, final Operator operator) {
	//	operator.setAccount(accountRepository.load(Account.class, operatorContext.getIdAccount()));
		accountRepository.save(operator);
		//operatorCompanyService.assignCompanies(operator.getId(), companies);
	}

	@Override
	protected void updateImpl(final OperatorDto dto) {
		Operator operator = accountRepository.load(Operator.class, dto.getId());
		operator.applyWithoutPassword(dto);
		// eventHistoryService.addEvent(EventHistoryType.CHANGE_OPERATOR_DATA, operator.getEmail());
	}

	@Override
	protected void deleteImpl(final Long id) {
		//settingsCmmService.protectLastOnePositionFromDelete(findAll().size(), Operator.class, id);
	}



	@Override
	public OperatorDto setOperatorContext(final String email) {
		Operator operator = accountRepository.findByEmail(email);
		OperatorDto operatorDto = null;
		if (operator != null) {
			prepareOperatorApplicationData(operator);
			operatorDto = operator.createDto();
		}
		return operatorDto;
	}

	@Override
	public OperatorDto setOperatorContext(final String email, final String ipAddress) {
		Operator operator = accountRepository.findByEmail(email);
		OperatorDto operatorDto = null;
		if (operator != null) {
			prepareOperatorApplicationData(operator, ipAddress);
			operatorDto = operator.createDto();
		}
		return operatorDto;
	}

	private void prepareOperatorApplicationData(final Operator operator, final String ipAddress) {
		operatorContext.setOperatorContext(operator);
		// synchronizeWithGoogleCalendar();
		saveLoginDateAndIpAddress(operator, ipAddress);
	}

	private void prepareOperatorApplicationData(final Operator operator) {
		operatorContext.setOperatorContext(operator);
		// synchronizeWithGoogleCalendar();
		saveLoginDate(operator);
	}



	private void saveLoginDateAndIpAddress(final Operator operator, final String ipAddress) {
		String validatedIpAddress = getValidatedIpAddress(ipAddress);
		operator.setLastIpAddress(validatedIpAddress);
		// 100 ostatnich adresow ip podczas logowania
		// eventHistoryService.addEvent(EventHistoryType.LOGIN_OPERATOR, "z adresu ip: " +
		// ipAddress);
		saveLoginDate(operator);
	}

	private String getValidatedIpAddress(final String ipAddress) {
		if (StringUtil.ipV4AddressValidator(ipAddress)) {
			return ipAddress;
		} else {
			logger.info("Incorrect IP address" + ipAddress);
			return validLengthIpAddress(ipAddress);
		}
	}

	private String validLengthIpAddress(final String ipAddress) {
		if (ipAddress.length() > IP_ADDRESS_LENGTH) {
			logger.info("Ip address too long: " + ipAddress);
			return StringUtil.substringTo(ipAddress, IP_ADDRESS_LENGTH);
		} else {
			return ipAddress;
		}
	}

	private void saveLoginDate(final Operator operator) {
		operator.setLastLoginDate(Date.getCurrentDateAndTimeAndMills());
		accountRepository.save(operator);
	}

	@Override
	public void setCurrentCompany(final Long idCurrentCompany) {
		operatorContext.setIdCurrentCompany(idCurrentCompany);
	}

	@Override
	public void generatePassword(final String login) {
		if (operatorContext.isSetOperatorContext()) {
			//generatePasswordForEditOperator(login, PasswordGenerator.generatePassword());
		} else {
			generatePasswordForNotLogedOperator(login);
		}
	}

	private void generatePasswordForNotLogedOperator(final String login) {
		Operator operator = accountRepository.findByEmail(login);
		if (operator != null) {
			operatorContext.setOperatorContext(operator);
			//generatePassword(login, PasswordGenerator.generatePassword());
		} else {
			throw new BusinessException(resourceService.getMessage(WRONG_EMAIL_EXCEPTION) + " " + login);
		}
	}

	void generatePassword(final String login, final String password) {
		Operator operator = accountRepository.findByEmail(login);
		if (operator != null) {
			operatorContext.setOperatorContext(operator);
			//saveGeneratedPassword(password);
			String newPassword = accountRepository.findByEmail(login).getPassword();
			String hash = Encoder.encode(newPassword, login);
			//sendGeneratedPassword(login, password, hash, OperatorEmailTemplate.CHANGE_PASSWORD_SUBJECT);
		}
	}

	private void generatePasswordForEditOperator(final String login, final String generatedPassword) {
		saveGeneratedPasswordForEditOperator(login, generatedPassword);
		//sendGeneratedPassword(login, generatedPassword, null, OperatorEmailTemplate.CHANGE_PASSWORD_FOR_OPERATOR_SUBJECT);
	}

	private void saveGeneratedPasswordForEditOperator(final String login, final String generatedPassword) {
		Operator operator = accountRepository.findByEmail(login);
		//operator.setPasswordExpired(Boolean.TRUE);
		operator.setPassword(generatedPassword);
	}





	@Override
	public void activateGeneratedPassword(final String login, final String hash) {
		Operator operator = accountRepository.findByEmail(login);
		String checkHash = Encoder.encode(operator.getPassword(), login);
		if (checkHash.equals(hash)) {
			//operator.setPasswordExpired(Boolean.TRUE);
			operatorContext.setOperatorContext(operator);
			//String password = operatorProfileService.getValue(GENERATED_PASSWORD);
//			if (!StringUtil.isEmpty(password)) {
//				operator.setPassword(password);
//			}
			//operatorProfileService.deleteValue(GENERATED_PASSWORD);
		}
	}

	@Override
	public void changePassword(final String oldPassword, final String newPassword) {
		Operator logInOperator = accountRepository.load(Operator.class, operatorContext.getIdOperator());
		String encodePassword = Encoder.encode(oldPassword, logInOperator.getEmail());
		if (encodePassword.equals(logInOperator.getPassword())) {
			logInOperator.setPassword(newPassword);
		} else {
			throw new BusinessException(resourceService.getMessage(WRONG_PASSWORD_EXCEPTION));
		}
	}

	@Override
	public void changeLoginOperatorPassAndEmail(final String newEmail, final String oldEmail, final String newPassword, final String oldPassword) {
		Operator logInOperator = accountRepository.load(Operator.class, operatorContext.getIdOperator());
		logInOperator.setEmail(newEmail);
		changePassword(oldPassword, newPassword, oldEmail);
	}

	@Override
	public void changeSelectedDtoEmail(final OperatorDto dto) {
		Operator selectedOperator = accountRepository.load(Operator.class, dto.getId());
		selectedOperator.setEmail(dto.getEmail());
	}

	@Override
	public Boolean isAccountOwner(Long idOperator) {
		return null;
	}

	@Override
	public Boolean isOperatorAccountActive(Long idOperator) {
		return null;
	}

	@Override
	public void changePassword(final String oldPassword, final String newPassword, final String oldEmail) {
		Operator logInOperator = accountRepository.load(Operator.class, operatorContext.getIdOperator());
		String encodePassword = Encoder.encode(oldPassword, oldEmail);
		if (encodePassword.equals(logInOperator.getPassword())) {
			logInOperator.setPassword(newPassword);
			//logInOperator.setPasswordExpired(Boolean.FALSE);
		} else {
			throw new BusinessException(resourceService.getMessage(WRONG_PASSWORD_EXCEPTION));
		}
	}








	@Override
	public Long findAccount(final String login) {
		Operator operator = accountRepository.findByEmail(login.trim());
		if (operator != null) {
			//return operator.getAccount().getId();
		}
		return null;
	}

	@Override
	public Boolean getIsAbleToAddOperator() {
		return null;
	}

	@Override
	public Boolean setFirtAvailabeCompany(Long idOperator) {
		return null;
	}

	@Override
	public OperatorDto loadAdminOperatorByIdCompany(Long idCompany) {
		return null;
	}

	@Override
	public void activateAccount(String email) {

	}

	@Override
	public void activateAccount(final String login, final String hash) {
		Operator operator = accountRepository.findByEmail(login);
		String checkHash = Encoder.encode(operator.getPassword(), login);
		if (checkHash.equals(hash)) {
			activateAccount(login);
		}
	}

	@Override
	public Long findIdByName(final Long idAccount, final String lastName, final String firstName) {
		return null;
	}

	@Override
	public OperatorDto findMainOperatorForAccount(Long idAccount) {
		return null;
	}

	@Override
	public void updateBasicData(final OperatorDto dto) {
		Operator operator = accountRepository.load(Operator.class, dto.getId());
		operator.setEmail(dto.getEmail());
		operator.setFirstName(dto.getFirstName());
		operator.setLastName(dto.getLastName());
	}



	@Override
	public void logout() {
		operatorContext.cleanContext();
	}


	@Override
	public String getTestText() {
		return "To jest testowy tekst z serwisu";
	}
}
