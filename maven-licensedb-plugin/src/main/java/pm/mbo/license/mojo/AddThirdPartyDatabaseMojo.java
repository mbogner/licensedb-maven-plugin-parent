package pm.mbo.license.mojo;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.codehaus.mojo.license.AddThirdPartyMojo;

import java.util.Properties;

@Mojo(name = "add-third-party-database", requiresDependencyResolution = ResolutionScope.TEST,
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES)
public class AddThirdPartyDatabaseMojo extends AddThirdPartyMojo {

    @Parameter(property = "licensedb.database.url", defaultValue = "jdbc:h2:file:~/license")
    private String databaseUrl;

    @Parameter(property = "licensedb.database.user", defaultValue = "sa")
    private String databaseUser;

    @Parameter(property = "licensedb.database.password", defaultValue = "")
    private String databasePassword;

    @Parameter(property = "licensedb.database.driver_class", defaultValue = "org.h2.Driver")
    private String databaseDriverClass;

    @Parameter(property = "licensedb.database.dialect", defaultValue = "org.hibernate.dialect.H2Dialect")
    private String databaseDialect;

    @Parameter(property = "licensedb.database.hbm2ddl", defaultValue = "update")
    private String databaseHbm2ddl;

    @Parameter(property = "licensedb.database.debug", defaultValue = "false")
    private boolean databaseDebug;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doAction() throws Exception {
        final Log log = getLog();
        log.info("do something");
    }

    protected Properties createHibernateProperties() {
        final Properties props = new Properties();
        props.setProperty("hibernate.connection.url", databaseUrl);
        props.setProperty("hibernate.connection.username", databaseUser);
        props.setProperty("hibernate.connection.password", databasePassword);

        props.setProperty("hibernate.connection.driver_class", databaseDriverClass);
        props.setProperty("hibernate.dialect", databaseDialect);

        props.setProperty("hibernate.show_sql", String.valueOf(databaseDebug));
        props.setProperty("hibernate.format_sql", String.valueOf(databaseDebug));
        props.setProperty("hibernate.hbm2ddl.auto", databaseHbm2ddl);
        return props;
    }
}
