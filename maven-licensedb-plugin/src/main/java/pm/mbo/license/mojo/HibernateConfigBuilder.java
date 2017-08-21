package pm.mbo.license.mojo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;

public class HibernateConfigBuilder {

    private String databaseUrl;

    private String databaseUser;

    private String databasePassword;

    private String databaseDriverClass;

    private String databaseDialect;

    private String databaseHbm2ddl;

    private boolean debug;

    public HibernateConfigBuilder url(final String databaseUrl) {
        this.databaseUrl = databaseUrl;
        return this;
    }

    public HibernateConfigBuilder user(final String databaseUser) {
        this.databaseUser = databaseUser;
        return this;
    }

    public HibernateConfigBuilder password(final String databasePassword) {
        this.databasePassword = databasePassword;
        return this;
    }

    public HibernateConfigBuilder driverClass(final String databaseDriverClass) {
        this.databaseDriverClass = databaseDriverClass;
        return this;
    }

    public HibernateConfigBuilder dialect(final String databaseDialect) {
        this.databaseDialect = databaseDialect;
        return this;
    }

    public HibernateConfigBuilder hbm2ddl(final String databaseHbm2ddl) {
        this.databaseHbm2ddl = databaseHbm2ddl;
        return this;
    }

    public HibernateConfigBuilder debug(final boolean debug) {
        this.debug = debug;
        return this;
    }

    public Map<String, String> build() {
        final Map<String, String> props = new HashMap<>();
        props.put("hibernate.connection.url", databaseUrl);
        props.put("hibernate.connection.username", databaseUser);
        props.put("hibernate.connection.password", databasePassword);

        props.put("hibernate.connection.driver_class", databaseDriverClass);
        props.put("hibernate.dialect", databaseDialect);

        props.put("hibernate.show_sql", String.valueOf(debug));
        props.put("hibernate.format_sql", String.valueOf(debug));
        props.put("hibernate.hbm2ddl.auto", databaseHbm2ddl);
        return props;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("databaseUrl", databaseUrl)
                .append("databaseUser", databaseUser)
                .append("databasePassword", databasePassword)
                .append("databaseDriverClass", databaseDriverClass)
                .append("databaseDialect", databaseDialect)
                .append("databaseHbm2ddl", databaseHbm2ddl)
                .append("debug", debug)
                .toString();
    }
}
