package pm.mbo.license.mojo.dal;

import edu.emory.mathcs.backport.java.util.Arrays;
import org.apache.commons.lang3.builder.ToStringBuilder;
import pm.mbo.license.mojo.helper.Conditions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HibernateConfigBuilder {

    private static final Set<String> HBM2DDL_VALUES = new HashSet<String>(Arrays.asList(new String[]{
            "update",
            "create",
            "create-drop",
            "validate",
            ""
    }));

    private String databaseUrl;

    private String databaseUser;

    private String databasePassword;

    private String databaseDriverClass;

    private String databaseDialect;

    private String databaseHbm2ddl;

    private Boolean debug;

    private HibernateConfigBuilder() {
    }

    public static HibernateConfigBuilder builder() {
        return new HibernateConfigBuilder();
    }

    public HibernateConfigBuilder url(final String databaseUrl) {
        Conditions.notBlank("databaseUrl", databaseUrl);
        this.databaseUrl = databaseUrl;
        return this;
    }

    public HibernateConfigBuilder user(final String databaseUser) {
        Conditions.notBlank("databaseUser", databaseUser);
        this.databaseUser = databaseUser;
        return this;
    }

    public HibernateConfigBuilder password(final String databasePassword) {
        Conditions.notNull("databasePassword", databasePassword);
        this.databasePassword = databasePassword;
        return this;
    }

    public HibernateConfigBuilder driverClass(final String databaseDriverClass) {
        Conditions.notBlank("databaseDriverClass", databaseDriverClass);
        this.databaseDriverClass = databaseDriverClass;
        return this;
    }

    public HibernateConfigBuilder dialect(final String databaseDialect) {
        Conditions.notBlank("databaseDialect", databaseDialect);
        this.databaseDialect = databaseDialect;
        return this;
    }

    public HibernateConfigBuilder hbm2ddl(final String databaseHbm2ddl) {
        Conditions.inList("databaseHbm2ddl", databaseHbm2ddl, HBM2DDL_VALUES);
        this.databaseHbm2ddl = databaseHbm2ddl;
        return this;
    }

    public HibernateConfigBuilder debug(final Boolean debug) {
        Conditions.notNull("debug", debug);
        this.debug = debug;
        return this;
    }

    public Map<String, String> build() {
        Conditions.notBlank("databaseUrl", databaseUrl);
        Conditions.notBlank("databaseUser", databaseUser);
        Conditions.notBlank("databaseDriverClass", databaseDriverClass);
        Conditions.notBlank("databaseDialect", databaseDialect);
        Conditions.notNull("debug", debug);
        Conditions.inList("databaseHbm2ddl", databaseHbm2ddl, HBM2DDL_VALUES);

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
