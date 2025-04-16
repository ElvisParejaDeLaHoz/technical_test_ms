package co.com.test.r2dbc.config;

import io.r2dbc.spi.ConnectionFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostgreSQLConnectionPoolTest {

    @InjectMocks
    private PostgreSQLConnectionPool connectionPool;

    @BeforeEach
    public void setUp() {
        connectionPool = new PostgreSQLConnectionPool();
    }

    @Test
    void verifyOracleConnectionConfigurationTest() {
        PostgresqlConnectionProperties properties = new PostgresqlConnectionProperties();
        properties.setHost("localhost");
        properties.setPort(1);
        properties.setDatabase("test");
        properties.setUsername("usertest");
        properties.setPassword("userpass");
        properties.setDriver("postgres");

        PostgreSQLConnectionPoolProperties connectionPoolProperties = new PostgreSQLConnectionPoolProperties();
        connectionPoolProperties.setInitialSize(10);
        connectionPoolProperties.setMaxSize(50);
        connectionPoolProperties.setMaxIdleTime(1000);

        ConnectionFactory connectionFactory = connectionPool.getConnectionConfig(properties, connectionPoolProperties);

        Assertions.assertNotNull(connectionFactory);
    }

}