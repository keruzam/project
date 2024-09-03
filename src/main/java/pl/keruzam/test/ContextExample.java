package pl.keruzam.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import pl.keruzam.service.ServiceExample;

public final class ContextExample {

	public static void main(final String[] args) throws Exception {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("/spring/context.xml");
		//ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
		ServiceExample serviceExample = ctx.getBean("serviceExample", ServiceExample.class);
		System.out.println(serviceExample.getTextFromService2());
	}
}