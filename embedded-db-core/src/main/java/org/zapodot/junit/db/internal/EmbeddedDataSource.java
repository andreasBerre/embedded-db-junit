package org.zapodot.junit.db.internal;

import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Logger;

/**
 * A simple DataSource implementation that simply wraps a single Connection
 * Needs to be public to be used by ByteBuddy. Part of internal api, so it may be changed or removed without prior warning
 */
public class EmbeddedDataSource implements DataSource {

    static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(EmbeddedDataSource.class);

    private final Connection connection;

    private EmbeddedDataSource(final Connection connection) {
        this.connection = connection;
    }

    public static EmbeddedDataSource create(final Connection connection) {
        return new EmbeddedDataSource(connection);
    }

    @Override
    public Connection getConnection() {
        return CloseSuppressedConnectionFactory.createProxy(connection);
    }

    @Override
    public Connection getConnection(final String username, final String password) {
        return getConnection();
    }

    @Override
    public PrintWriter getLogWriter() {
        return new PrintWriter(new Slf4jInfoWriter(LOGGER));
    }

    @Override
    public void setLogWriter(final PrintWriter out) {
        // intentionally kept empty
    }

    @Override
    public void setLoginTimeout(final int seconds) {
        // intentionally kept empty
    }

    @Override
    public int getLoginTimeout() {
        return 0;
    }

    @Override
    public Logger getParentLogger() {
        return Logger.getGlobal();
    }

    @Override
    public <T> T unwrap(final Class<T> iface) {
        throw new UnsupportedOperationException("unWrap(Class) is not supported for embedded datasource");
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) {
        return false;
    }
}
