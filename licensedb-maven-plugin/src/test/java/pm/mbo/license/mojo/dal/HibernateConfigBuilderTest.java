package pm.mbo.license.mojo.dal;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class HibernateConfigBuilderTest {

    @Test
    public void build() throws Exception {
        assertNotNull(HibernateConfigBuilder.builder()
                .url("databaseUrl")
                .user("databaseUser")
                .password("databasePassword")
                .driverClass("databaseDriverClass")
                .dialect("databaseDialect")
                .debug(true)
                .hbm2ddl("update")
                .build());
    }

    @Test(expected = IllegalArgumentException.class)
    public void url_blank() throws Exception {
        HibernateConfigBuilder.builder().url("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void user_blank() throws Exception {
        HibernateConfigBuilder.builder().user("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void password_null() throws Exception {
        HibernateConfigBuilder.builder().password(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void driverClass_blank() throws Exception {
        HibernateConfigBuilder.builder().driverClass("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void dialect_blank() throws Exception {
        HibernateConfigBuilder.builder().dialect("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void debug_null() throws Exception {
        HibernateConfigBuilder.builder().debug(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hbm2ddl_null() throws Exception {
        HibernateConfigBuilder.builder().hbm2ddl(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hbm2ddl_invalid() throws Exception {
        HibernateConfigBuilder.builder().hbm2ddl("some");
    }
}