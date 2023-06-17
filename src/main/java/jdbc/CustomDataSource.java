package jdbc;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.Synchronized;
import lombok.Value;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

@Getter
@Setter
public class CustomDataSource implements DataSource {

    private static final String PROPERTIES_FILE_NAME = "app.properties";
    private static volatile CustomDataSource instance;

    private final String driver;
    private final String url;
    private final String name;
    private final String password;

    private CustomDataSource(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.password = password;
        this.name = name;
    }

    public static CustomDataSource getInstance() {
        CustomDataSource localInstance = instance;
        if (localInstance == null) {
            synchronized (CustomDataSource.class) {
                localInstance = instance;
                if (localInstance == null) {
                    try {
                        Properties properties = new Properties();
                        properties.load(CustomDataSource.class
                            .getClassLoader()
                            .getResourceAsStream(PROPERTIES_FILE_NAME));
                        instance = localInstance = new CustomDataSource(
                            properties.getProperty("postgres.driver"),
                            properties.getProperty("postgres.url"),
                            properties.getProperty("postgres.password"),
                            properties.getProperty("postgres.name")
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return localInstance;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return new CustomConnector().getConnection(url, name, password);
    }

    @Override
    public Connection getConnection(final String username, final String password) throws SQLException {
        return new CustomConnector().getConnection(url, username, password);
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new SQLException();
    }

    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        throw new SQLException();
    }

    @Override
    public void setLoginTimeout(final int seconds) throws SQLException {
        throw new SQLException();
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new SQLException();
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new SQLFeatureNotSupportedException();
    }

    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new SQLException();
    }

    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        throw new SQLException();
    }
}