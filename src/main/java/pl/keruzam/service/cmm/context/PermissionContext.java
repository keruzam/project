package pl.keruzam.service.cmm.context;

import java.io.Serializable;
import java.util.List;

import pl.keruzam.service.cmm.OperatorPermission;

/**
 * Kontekt uprawnien
 *
 * @author Marcel Matuszak
 */

public interface PermissionContext extends Serializable {

	void setPermissionContext(final Long idAccountType, List<OperatorPermission> operatorPermissionList, Boolean isAdmin);

	Long getIdAccountType();

	List<OperatorPermission> getOperatorPermissionList();

	//AccountTypeState getAccountTypeState();

	boolean isAdmin();
}
