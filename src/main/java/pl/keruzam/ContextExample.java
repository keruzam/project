package pl.keruzam;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class ContextExample {

	public static void main(final String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");
		//ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		ServiceExample serviceExample = ctx.getBean("serviceExample", ServiceExample.class);
		System.out.println(serviceExample.getTextFromService2());
	}
}