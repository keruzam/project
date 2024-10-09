package pl.keruzam.service.cmm;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Kreator proxy
 *
 * @author Mirek Szajowski
 */
public class ProxyCreator {
	public static <O> O createProxy(final Class<O> target, final ProxyProvider provider) {
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setTargetClass(target);
		proxyFactory.addAdvice(new MethodInterceptor() {

			@Override
			public Object invoke(final MethodInvocation invocation) throws Throwable {
				return invocation.getMethod().invoke(provider.getObject(), invocation.getArguments());
			}
		});
		return (O) proxyFactory.getProxy();
	}

	public static <O> O createProxy(final Class<O> target) {
		return createProxy(target, new ProxyProvider() {

			@Override
			public Object getObject() {
				return ContextHolder.getBean(target);
			}
		});
	}
}
