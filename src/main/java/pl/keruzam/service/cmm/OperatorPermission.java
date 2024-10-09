package pl.keruzam.service.cmm;

import java.util.Arrays;
import java.util.List;

/**
 * Uprawnienia operatora
 * 
 * @author Marcel Matuszak
 * 
 */
public enum OperatorPermission {

	//@formatter:off
	COMPANY_CONDITION, 
	SALE, 
	SALE_OPERATOR, 
	ARTICLE, 
	CUSTOMER, 
	CONTACT, 
	CALENDAR, 
	CALENDAR_OPERATOR, 
	ANALYSIS, 
	ACCOUNTING, 
	SETTINGS, 
	STORE, 
	STOCKTAKING,
	IMPORT,
	EXPORT,
	SETTLEMENT,
	JPK,
	VINDICATION,
	FISCALIZATION,
	PURCHASE,
	PURCHASE_OPERATOR;
	//@formatter:on

	public static OperatorPermission[] getDefaultPermissionsForNewOperator() {
		return new OperatorPermission[] { COMPANY_CONDITION, SALE, PURCHASE, ARTICLE, CUSTOMER, CONTACT, CALENDAR, ANALYSIS, IMPORT, EXPORT };
	}

	public static List<OperatorPermission> getPermisionsGroupSalePurchase() {
		return Arrays.asList(COMPANY_CONDITION, SALE, SALE_OPERATOR, PURCHASE, PURCHASE_OPERATOR, ARTICLE);
	}

	public static List<OperatorPermission> getPermisionsGroupAdministration() {
		return Arrays.asList(SETTINGS, IMPORT, EXPORT, ANALYSIS);
	}

	public static List<OperatorPermission> getPermisionsGroupCrm() {
		return Arrays.asList(CUSTOMER, CONTACT, CALENDAR, CALENDAR_OPERATOR);
	}

	public static List<OperatorPermission> getPermisionsGroupModules() {
		return Arrays.asList(ACCOUNTING, FISCALIZATION, VINDICATION);
	}

	public static List<OperatorPermission> getPermisionsGroupStore() {
		return Arrays.asList(STORE, STOCKTAKING);
	}
}