package pm.mbo.license.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;

public final class NetworkTool {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkTool.class);

    public static final int PORT_MIN = 1;
    public static final int PORT_MAX = 0xffff;

    public static int findRandomOpenPortOnAllLocalInterfaces() throws IOException {
        try (final ServerSocket socket = new ServerSocket(0)) {
            final int port = socket.getLocalPort();
            LOG.trace("found free port {}", port);
            return port;
        }
    }

    public static void checkPort(final int port) {
        if (port < PORT_MIN || port > PORT_MAX)
            throw new IllegalArgumentException(String.format("port out of range (%d-%d): %d", PORT_MIN, PORT_MAX, port));
    }

    private NetworkTool() {
        throw new IllegalAccessError();
    }
}
