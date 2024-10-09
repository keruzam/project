package pl.keruzam.service.cmm.context;

import java.util.List;

import jakarta.inject.Inject;
import org.springframework.stereotype.Component;
import pl.keruzam.service.cmm.OperatorPermission;

/**
 * @author Marcel Matuszak
 */
@Component("permissionContext")
public class PermissionContextImpl implements PermissionContext {



	private Long idAccountType;
	private List<OperatorPermission> operatorPermissionList;
	private Boolean admin;

	public static PermissionContextImpl create() {
		return ContextBeansFactory.createPermissionContextProxy();
	}

	@Override
	public void setPermissionContext(final Long idAccountType, final List<OperatorPermission> operatorPermissionList, final Boolean isAdmin) {
		this.idAccountType = idAccountType;
		this.operatorPermissionList = operatorPermissionList;
		this.admin = isAdmin;
	}

	@Override
	public final Long getIdAccountType() {
		return idAccountType;
	}

	@Override
	public final List<OperatorPermission> getOperatorPermissionList() {
		return operatorPermissionList;
	}



	@Override
	public boolean isAdmin() {
		return admin;
	}

}
