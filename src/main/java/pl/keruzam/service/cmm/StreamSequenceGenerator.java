package pl.keruzam.service.cmm;

import java.util.Properties;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

/**
 * Hack dla generator sekwencji ktory nadaj jest do dupy w hibernate
 *
 * @author Mirek Szajowski
 */
public class StreamSequenceGenerator extends SequenceStyleGenerator {

	public static final String CLASS = "pl.com.stream.topfirma.blc.lib.StreamSequenceGenerator";
	public static final long SEQ_INCREMENT_BY = 10;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
		return super.generate(session, object);
	}

	@Override
	public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) throws MappingException {
		parameters.setProperty("initial_value", "1");
		parameters.setProperty("increment_size", "10");
		parameters.setProperty("optimizer", "pooled-lo");
		super.configure(type, parameters, serviceRegistry);
	}
}
