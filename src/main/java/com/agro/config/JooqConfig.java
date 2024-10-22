package com.agro.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.conf.Settings;
import org.jooq.conf.RenderNameStyle;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DataSourceConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.jooq.JooqExceptionTranslator;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class JooqConfig {
    private final DataSource dataSource;

    public JooqConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(
                new TransactionAwareDataSourceProxy(dataSource)
        );
    }

    @Bean
    public DSLContext dslContext() {
        return new DefaultDSLContext(configuration());
    }

    private DefaultConfiguration configuration() {
        DefaultConfiguration config = new DefaultConfiguration();
        config.set(connectionProvider());
        config.set(SQLDialect.POSTGRES);
        config.set(new Settings().
                withRenderNameStyle(RenderNameStyle.AS_IS));
        config.set(new DefaultExecuteListenerProvider(
                new JooqExceptionTranslator()));
        return config;
    }
}
