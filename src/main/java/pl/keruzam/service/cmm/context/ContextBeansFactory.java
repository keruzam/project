package pl.keruzam.service.cmm.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import pl.keruzam.service.cmm.ContextHolder;

/**
 * Fabryka ktora tworzy obiekty konteksowe
 *
 * @author Mirek Szajowski
 */
@SuppressWarnings("unchecked")
public class ContextBeansFactory {

	public static final String OPERATOR_SESSION_SCOPE = "sessionScopeOperatorContext";
	public static final String OPERATOR_THREAD_SCOPE = "threadScopeOperatorContext";
	public static final String REQUEST_SESSION_SCOPE = "requestScopeRequestContext";
	public static final String REQUEST_THREAD_SCOPE = "threadScopeRequestContext";
	public static final String PANEL_SESSION_SCOPE = "sessionScopePanelContext";
	public static final String PANEL_THREAD_SCOPE = "threadScopePanelContext";
	public static final String PERMISSION_SESSION_SCOPE = "sessionScopePermissionContext";
	public static final String PERMISSION_THREAD_SCOPE = "threadScopePermissionContext";
	public static final String ACCOUNTING_SESSION_SCOPE = "sessionScopeAccountingContext";
	public static final String ACCOUNTING_THREAD_SCOPE = "threadScopeAccountingContext";

	public static <I> I createOperatorContextProxy() {
		return (I) Proxy.newProxyInstance(ContextBeansFactory.class.getClassLoader(), OperatorContextImpl.class.getInterfaces(), new UserContextInvocation());
	}

	public static <I> I createRequestContextProxy() {
		return (I) Proxy.newProxyInstance(ContextBeansFactory.class.getClassLoader(), RequestContextImpl.class.getInterfaces(), new RequestContextInvocation());
	}

	public static <I> I createPermissionContextProxy() {
		return (I) Proxy.newProxyInstance(ContextBeansFactory.class.getClassLoader(), PermissionContextImpl.class.getInterfaces(),
				new PermissionContextInvocation());
	}

	private static class UserContextInvocation implements InvocationHandler {
		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			OperatorContext result;
			try {
				result = (OperatorContext) ContextHolder.getBean(OPERATOR_SESSION_SCOPE);
			} catch (Exception e) {
				result = (OperatorContext) ContextHolder.getBean(OPERATOR_THREAD_SCOPE);
			}
			return method.invoke(result, args);
		}
	}

	private static class RequestContextInvocation implements InvocationHandler {
		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			RequestContext result;
			try {
				result = (RequestContext) ContextHolder.getBean(REQUEST_SESSION_SCOPE);
			} catch (Exception e) {
				result = (RequestContext) ContextHolder.getBean(REQUEST_THREAD_SCOPE);
			}
			return method.invoke(result, args);
		}
	}

	private static class PermissionContextInvocation implements InvocationHandler {
		@Override
		public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
			PermissionContext result;
			try {
				result = (PermissionContext) ContextHolder.getBean(PERMISSION_SESSION_SCOPE);
			} catch (Exception e) {
				result = (PermissionContext) ContextHolder.getBean(PERMISSION_THREAD_SCOPE);
			}
			return method.invoke(result, args);
		}
	}

}
