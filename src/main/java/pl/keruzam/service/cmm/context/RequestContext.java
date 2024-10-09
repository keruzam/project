package pl.keruzam.service.cmm.context;

/**
 * Kontekst rzadania
 * 
 * @author Mirek Szajowski
 * 
 */
public interface RequestContext {
	public static final String QUERY_REQUEST_COUNTER = "queryRequestCounter";
	public static final String DISABLE_QUERY_REQUEST_DIAGNOSTIC = "disableQueryRequestDiagnostic";
	public static final String ROOT_INVOCATION = "rootInvocation";

	public Object get(final String key);

	public void put(final String key, final Object value);

	String getCurrentAppLocation();
}
