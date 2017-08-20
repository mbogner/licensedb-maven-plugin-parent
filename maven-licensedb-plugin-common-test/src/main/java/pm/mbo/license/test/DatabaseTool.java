package pm.mbo.license.test;

import org.h2.tools.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.SQLException;

public final class DatabaseTool {

    private static final Logger LOG = LoggerFactory.getLogger(DatabaseTool.class);

    public static int startDatabase() throws IOException, SQLException {
        return startDatabase(NetworkTool.findRandomOpenPortOnAllLocalInterfaces());
    }

    public static int startDatabase(final int port) throws IOException, SQLException {
        Server.createTcpServer("-tcpPort", String.valueOf(port), "-tcpAllowOthers").start();
        LOG.trace("created server on port {}", port);
        return port;
    }

    public static void stopDatabase(final int port) throws SQLException {
        Server.shutdownTcpServer(String.format("tcp://localhost:%d", port), "", true, false);
        LOG.trace("stopped server on port {}", port);
    }

    public static String createConnectionString(final int port) {
        final String name = String.format("./target/test%d", port);
        final String constr = String.format("jdbc:h2:tcp://localhost:%d/%s", port, name);
        LOG.trace("created connection string {}", constr);
        return constr;
    }

    private DatabaseTool() {
        throw new IllegalAccessError();
    }

}
