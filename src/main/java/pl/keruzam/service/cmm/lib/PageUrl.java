package pl.keruzam.service.cmm.lib;

/**
 * Klasa pomocnicza z adresami stron
 *
 * @author Tomasz Mazurek
 */
public class PageUrl {

	public static final String FACES_REDIRECT = "?faces-redirect=true";

	public static final String HOME_PAGE = "/home.do" + FACES_REDIRECT;
	public static final String LOGIN_PAGE = "/login" + FACES_REDIRECT;
	public static final String DEMO_PAGE = "demo.do" + FACES_REDIRECT;

	// PARTNER
	public static final String PARTNERS_PAGE = "partners" + FACES_REDIRECT;
	public static final String PARTNER_LOGIN_PAGE = "loginPartner.do" + FACES_REDIRECT;
	public static final String PARTNER_FIRMINO_MAIN_PAGE = ConstHost.FIRMINO_WWW;
	public static final String PARTNER_FIRMINO_ACCOUNTING_PAGE = ConstHost.FIRMINO_WWW + "ksiegowosc-online/";
	public static final String PARTNER_FIRMINO_WAREHOUSE_PAGE = ConstHost.FIRMINO_WWW + "magazyn/";
	public static final String PARTNER_FIRMINO_E_COMMERCE_PAGE = ConstHost.FIRMINO_WWW + "e-commerce/";
	public static final String PARTNER_FIRMINO_FISCALIZATION_PAGE = ConstHost.FIRMINO_WWW + "fiskalizacja/";
	public static final String PARTNER_FIRMINO_VINDICATION_PAGE = ConstHost.FIRMINO_WWW + "windykacja/";

	// REGISTRATION
	public static final String REGISTRATION_COMPLETE_PAGE = "info/registrationComplete.do" + FACES_REDIRECT;
	public static final String SIMPLE_REGISTRATION_FAIL_PAGE = "simpleRegistrationFail.do" + FACES_REDIRECT;
	public static final String HACK_PAGE = "/info/empty.do" + FACES_REDIRECT;

	// PURCHASE
	public static final String VIEWS_PURCHASE_DOCUMENT_PAGE = "views/purchase/purchaseDocument.do" + FACES_REDIRECT;
	public static final String PURCHASE_DOCUMENT_PAGE = "purchaseDocument.do" + FACES_REDIRECT;

	// COST
	public static final String VIEWS_COST_DOCUMENT_PAGE = "views/booking/cost/costDocument.do" + FACES_REDIRECT;
	public static final String VIEWS_COST_MANAGER_PAGE = "views/booking/cost/costManager.do" + FACES_REDIRECT;
	public static final String COST_DOCUMENT_PAGE = "costDocument.do" + FACES_REDIRECT;

	// INCOME
	public static final String INCOME_DOCUMENT_PAGE = "incomeDocument.do" + FACES_REDIRECT;
	// PERIOD REPORTS - RAPORTY OKRESOWE
	public static final String PERIOD_REPORT_DOCUMENT_PAGE = "periodReportDocument.do" + FACES_REDIRECT;

	// CASH
	public static final String CASH_DOCUMENT_PAGE = "cashDocument.do" + FACES_REDIRECT;

	// BANK
	public static final String BANK_DOCUMENT_PAGE = "bankDocument.do" + FACES_REDIRECT;

	// JPK
	public static final String JPK_FILE_PAGE = "jpkFile.do" + FACES_REDIRECT;

	// SALE
	public static final String VIEWS_SALE_DOCUMENT_PAGE = "views/sale/saleDocument.do" + FACES_REDIRECT;

	// SETTINGS
	public static final String COMPANY_PAGE = "views/settings/company.do" + FACES_REDIRECT;
	public static final String OPERATOR_PAGE = "views/settings/operator.do" + FACES_REDIRECT;

	// STOCKTAKING
	public static final String VIEWS_STOCKTAKING_PAGE = "views/store/stocktaking.do" + FACES_REDIRECT;
	public static final String VIEWS_STOCKTAKING_TABLE_PAGE = "views/store/stocktakingDocuments.do" + FACES_REDIRECT;

	// ASSETS
	public static final String ASSET_DOCUMENT_PAGE = "assetDocument.do" + FACES_REDIRECT;

}
