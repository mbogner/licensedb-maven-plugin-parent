package pm.mbo.license.test;

import org.junit.Test;

import java.sql.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DatabaseToolTest {

    @Test
    public void startStopDatabase() throws Exception {
        final int port = DatabaseTool.startDatabase();
        testDatabaseConnection(port);
        DatabaseTool.stopDatabase(port);
    }

    @Test
    public void startTwice() throws Exception {
        final int port = DatabaseTool.startDatabase();
        try {
            DatabaseTool.startDatabase(port);
            fail("should not be reached");
        } catch (SQLException exc) {
            // expected
        }
        testDatabaseConnection(port);
        DatabaseTool.stopDatabase(port);
    }

    @Test
    public void startTwo() throws Exception {
        final int port1 = DatabaseTool.startDatabase();
        final int port2 = DatabaseTool.startDatabase();

        testDatabaseConnection(port1);
        testDatabaseConnection(port2);

        DatabaseTool.stopDatabase(port1);

        testDatabaseConnection(port2);
        DatabaseTool.stopDatabase(port2);

        try {
            testDatabaseConnection(port2);
            fail("should not be reached");
        } catch (final SQLException exc) {
            // expected
        }
    }

    public static void testDatabaseConnection(final int port) throws Exception {
        final Connection connection = DriverManager.getConnection(DatabaseTool.createConnectionString(port), "sa", "");
        assertNotNull(connection);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT 1");
        ResultSet resultSet = preparedStatement.executeQuery();
        assertNotNull(resultSet);
        resultSet.close();
        preparedStatement.close();

        connection.close();
    }

}