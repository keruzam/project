package pl.keruzam.db;

import org.hibernate.boot.model.FunctionContributions;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.BasicTypeRegistry;

/**
 * Rozszerzenie PostgreSQLDialect
 *
 * dodanie funkcji agregujacej dwa stringi uzyte w select
 *
 * @author tomasz.mazurek
 */
public class PGDialect extends PostgreSQLDialect {

	public PGDialect() {
		super();
		///registerColumnTypes(Types.NUMERIC,DecimalTypeDescriptor,
		//	this.registerColumnType(Types.NUMERIC, DecimalTypeDescriptor.INSTANCE.getName());
	}

	@Override
	public void initializeFunctionRegistry(FunctionContributions functionContributions) {
		super.initializeFunctionRegistry(functionContributions);
		BasicTypeRegistry basicTypeRegistry = functionContributions.getTypeConfiguration().getBasicTypeRegistry();
		SqmFunctionRegistry functionRegistry = functionContributions.getFunctionRegistry();
		functionRegistry.registerPattern("string_agg", "string_agg(?1, ?2)");
		functionRegistry.registerPattern("cast_long", "CAST(?1 AS long)");
		functionRegistry.registerPattern("cast_bigdecimal", "CAST(?1 AS decimal(16,2))");
		functionRegistry.registerPattern("decimal_nullsafe", "COALESCE(?1, CAST(0 AS decimal))");
		functionRegistry.registerPattern("cast_date", "CAST(?1 AS timestamp)");
		//functionRegistry.registerPattern("cast_decimal", "CAST(?1 AS decimal)");
		//functionRegistry.registerPattern("sum_decimal", "CAST(SUM(?1) AS decimal)");
		//functionRegistry.registerPattern("diff_zero", "(?1 <> CAST(0 AS decimal))", basicTypeRegistry.resolve(StandardBasicTypes.BOOLEAN));
	}

	//	@Override
	//	public JdbcType resolveSqlTypeDescriptor(String columnTypeName, int jdbcTypeCode, int precision, int scale, JdbcTypeRegistry jdbcTypeRegistry) {
	//		return super.resolveSqlTypeDescriptor(columnTypeName, jdbcTypeCode, precision, scale, jdbcTypeRegistry);
	//	}

}