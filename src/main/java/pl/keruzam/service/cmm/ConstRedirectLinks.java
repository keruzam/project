package pl.keruzam.service.cmm;

/**
 * Linki dolaczane do tabel jako link powrotny po nacisnieciu 'Powrot'
 *
 * @author MKowalski
 */
public class ConstRedirectLinks {

	public static final String BACKTO_PARAM = "&backTo=";
	public static final String DOCUMENT_FUNCTION_PARAM = "&documentFunction=";

	// SPRZEDAŻ -> DOKUMENTY
	public static final String SALE_DOCUMENTS_PARAM = "saleDocuments";
	// SPRZEDAŻ -> KOREKTY
	public static final String SALE_DOCUMENTS_CORRECTION_PARAM = "saleDocumentsCorrection";
	// SPRZEDAŻ -> PROFORMY
	public static final String SALE_DOCUMENTS_PROFORM_PARAM = "saleDocumentsProform";
	// SPRZEDAŻ -> ZAMÓWIENIA
	public static final String SALE_DOCUMENTS_ORDER_PARAM = "saleDocumentsOrder";

	// ZAKUP -> DOKUMENTY
	public static final String PURCHASE_DOCUMENTS_PARAM = "purchaseDocuments";
	// ZAKUP -> KOREKTY
	public static final String PURCHASE_DOCUMENTS_CORRECTION_PARAM = "purchaseDocumentsCorrection";

	// ROZRACHUNKI -> DOKUMENTY NIEZAPŁACONE
	public static final String UNSETTLED_SETTLEMENTS_PARAM = "unsettledSettlements";
	// ROZRACHUNKI -> DOKUMENTY ZAPŁACONE
	public static final String SETTLED_SETTLEMENTS_PARAM = "settledSettlements";
	// ROZRACHUNKI -> ROZLICZENIA
	public static final String SETTLEMENT_PAYMENTS_PARAM = "settlementPayments";
	// ROZRACHUNKI -> WINDYKACJA
	public static final String VINDICATION_DOCUMENTS_PARAM = "vindicationDocuments";

	// ANALIZY -> SPRZEDAŻ -> RANKINGI -> ODBIORCÓW
	public static final String RANKING_CUSTOMER_PARAM = "rankingCustomer";
	// ANALIZY -> SPRZEDAŻ -> RANKINGI -> SPRZEDAŻY PRODUKTÓW
	public static final String RANKING_SALE_ARTICLE_PARAM = "rankingSaleArticle";
	// ANALIZY -> SPRZEDAŻ -> RANKINGI -> OBSŁUGI ODBIORCÓW
	public static final String RANKING_CUSTOMER_HANDLING_PARAM = "rankingCustomerHandling";
	// ANALIZY -> SPRZEDAŻ -> ZESTAWIENIA -> DOKUMENTY NIEZAPŁACONE
	public static final String DOCUMENT_UNPAID_PARAM = "documentUnpaid";
	// ANALIZY -> SPRZEDAŻ -> ZESTAWIENIA -> ZAMÓWIONE PRODUKTY
	public static final String ORDER_ARTICLES_ANALYSIS_PARAM = "orderArticlesAnalysis";
	// ANALIZY -> SPRZEDAŻ -> REJESTRY -> DOKUMENTÓW
	public static final String DOCUMENT_PARAM = "document";
	// ANALIZY -> SPRZEDAŻ -> REJESTRY -> POZYCJI DOKUMENTÓW
	public static final String DOCUMENT_ITEM_PARAM = "documentItem";
	// ANALIZY -> SPRZEDAŻ -> REJESTRY -> SPRZEDAŻY VAT
	public static final String VAT_SALE_PARAM = "vatSale";
	// ANALIZY -> ZAKUP -> RANKINGI -> DOSTAWCÓW
	public static final String RANKING_DELIVERES_PARAM = "rankingDeliverers";
	// ANALIZY -> ZAKUP -> RANKING -> ZAKUPU PRODUKTÓW
	public static final String RANKING_PURCHASE_ARTICLES_PARAM = "rankingPurchaseArticles";
	// ANALIZY -> ZAKUP -> ZESTAWIENIA -> DOKUMENTY NIEZAPŁACONE
	public static final String PURCHASE_DOCUMENT_UNPAID_PARAM = "purchaseDocumentUnpaid";
	// ANALIZY ZAKUP -> REJESTRY -> DOKUMENTÓW
	public static final String PURCHASE_DOCUMENT_REGISTER_PARAM = "purchaseDocumentRegister";
	// ANALIZY ZAKUP -> REJESTRY -> POZYCJI DOKUMENTÓW
	public static final String PURCHASE_DOCUMENT_ITEM_PARAM = "purchaseDocumentItem";
	// ANALIZY ZAKUP -> REJESTRY -> ZAKUPU VAT
	public static final String VAT_PURCHASE_PARAM = "vatPurchase";
	// ANALIZY -> MAGAZYN -> RANKINGI -> ODBROTU TOWARÓW
	public static final String ARTICLES_REGISTRY_PARAM = "articlesRegistry";
	// ANALIZY -> MAGAZYN -> RANKINGI -> DOKUMENTÓW
	public static final String STORE_DOCUMENT_PARAM = "storeDocument";
	// ANALIZY -> MAGAZYN -> RANKINGI -> POZYCJI DOKUEMNTÓW
	public static final String STORE_DOCUMENT_ITEM_PARAM = "storeDocumentItem";
	// USTAWIENIA BASELINKER -> ZAMÓWIENIA / PRODUKTY
	public static final String SETTINGS_BASELINKER_PARAM = "baselinkerIntegration";
	// ANALIZY -> MAGAZYN -> ZESTAWIENIA -> ILOSCI I WARTOSCI PRODUKTOW
	public static final String STATEMENT_ARTICLES_STATE_AND_VALUE = "articlesStateAndValue";
	// ANALIZY -> SPRZEDAZ -> ZESTAWIENIA -> SPREDAZY PRODUKTOW
	public static final String STATEMENT_ARTICLES_SALE = "articlesSale";

	// KSIĘGOWOŚĆ -> ROZLICZ MIESIĄC
	public static final String TAX_SUMMARY_PARAM = "taxSummary";
	// KSIĘGOWOŚĆ -> DEKLARACJE -> WŁAŚCICIEL -> DEKLARACJE ROCZNE
	public static final String YEAR_DECLAR_PARAM = "yearDeclarationSummary";

	public static final String BASKET_PRODUCT_PARAM = "product";
	public static final String STORE_PLUS_PARAM = "storePlus";
	public static final String STORE_PARAM = "store";
	public static final String OPERATOR_PARAM = "operator";
	public static final String COMPANY_PARAM = "company";
	public static final String VINDICATION_PARAM = "vindication";
	public static final String ACCOUNTING_PARAM = "accounting";
	public static final String FISCALIZATION_PARAM = "fiscalization";
	private static final String ANALYSIS_PURCHASE_RANKING_GROUP = "/analysis/ranking/";
	private static final String ANALYSIS_PURCHASE_REGISTER_GROUP = "/analysis/register/";
	private static final String ANALYSIS_PURCHASE_STATEMENT_GROUP = "/analysis/statement/";
	private static final String ANALYSIS_SALE_RANKING_GROUP = "/analysis/ranking/";
	private static final String ANALYSIS_SALE_REGISTER_GROUP = "/analysis/register/";
	private static final String ANALYSIS_SALE_STATEMENT_GROUP = "/analysis/statement/";
	private static final String ANALYSIS_STORE_RANKING_GROUP = "/analysis/store/";
	private static final String PURCHASE_GROUP = "/purchase/";
	private static final String SALE_GROUP = "/sale/";
	private static final String SETTINGS_BASELINKER = "/settings/baselinker/";
	private static final String SETTLEMENT_GROUP = "/settlement/";
	private static final String TAX_SUMMARY_GROUP = "/accounting/tax/summary/";
	private static final String YEAR_DECLAR_GROUP = "/accounting/tax/owner/year/";

	public static String getUrl(final String param) {
		if (SALE_DOCUMENTS_PARAM.equals(param) || SALE_DOCUMENTS_CORRECTION_PARAM.equals(param) || SALE_DOCUMENTS_PROFORM_PARAM.equals(
				param) || SALE_DOCUMENTS_ORDER_PARAM.equals(param)) {
			return SALE_GROUP + param;
		} else if (PURCHASE_DOCUMENTS_PARAM.equals(param) || PURCHASE_DOCUMENTS_CORRECTION_PARAM.equals(param)) {
			return PURCHASE_GROUP + param;
		} else if (UNSETTLED_SETTLEMENTS_PARAM.equals(param) || SETTLED_SETTLEMENTS_PARAM.equals(param) || SETTLEMENT_PAYMENTS_PARAM.equals(
				param) || VINDICATION_DOCUMENTS_PARAM.equals(param)) {
			return SETTLEMENT_GROUP + param;
		} else if (RANKING_CUSTOMER_PARAM.equals(param) || RANKING_SALE_ARTICLE_PARAM.equals(param) || RANKING_CUSTOMER_HANDLING_PARAM.equals(param)) {
			return ANALYSIS_SALE_RANKING_GROUP + param;
		} else if (DOCUMENT_UNPAID_PARAM.equals(param) || ORDER_ARTICLES_ANALYSIS_PARAM.equals(param) || STATEMENT_ARTICLES_SALE.equals(param)) {
			return ANALYSIS_SALE_STATEMENT_GROUP + param;
		} else if (DOCUMENT_PARAM.equals(param) || DOCUMENT_ITEM_PARAM.equals(param) || VAT_SALE_PARAM.equals(param)) {
			return ANALYSIS_SALE_REGISTER_GROUP + param;
		} else if (RANKING_DELIVERES_PARAM.equals(param) || RANKING_PURCHASE_ARTICLES_PARAM.equals(param)) {
			return ANALYSIS_PURCHASE_RANKING_GROUP + param;
		} else if (PURCHASE_DOCUMENT_UNPAID_PARAM.equals(param)) {
			return ANALYSIS_PURCHASE_STATEMENT_GROUP + param;
		} else if (PURCHASE_DOCUMENT_REGISTER_PARAM.equals(param) || PURCHASE_DOCUMENT_ITEM_PARAM.equals(param) || VAT_PURCHASE_PARAM.equals(param)) {
			return ANALYSIS_PURCHASE_REGISTER_GROUP + param;
		} else if (ARTICLES_REGISTRY_PARAM.equals(param) || STORE_DOCUMENT_PARAM.equals(param) || STORE_DOCUMENT_ITEM_PARAM.equals(
				param) || STATEMENT_ARTICLES_STATE_AND_VALUE.equals(param)) {
			return ANALYSIS_STORE_RANKING_GROUP + param;
		} else if (TAX_SUMMARY_PARAM.equals(param)) {
			return TAX_SUMMARY_GROUP + param;
		} else if (YEAR_DECLAR_PARAM.equals(param)) {
			return YEAR_DECLAR_GROUP + param;
		} else if (SETTINGS_BASELINKER_PARAM.equals(param)) {
			return SETTINGS_BASELINKER + param;
		}
		return null;
	}
}
