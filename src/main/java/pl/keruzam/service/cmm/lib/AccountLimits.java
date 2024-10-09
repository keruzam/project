package pl.keruzam.service.cmm.lib;

/**
 * Inferfejs dla dostawcow limitow dla konta
 * 
 * @author Mirek Szajowski
 * 
 */
public interface AccountLimits {
	void check(String operation) throws AccountLimitException;
}
