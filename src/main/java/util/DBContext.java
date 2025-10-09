package util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database Connection Manager using HikariCP
 * Provides connection pooling for better performance and resource management
 */
public class DBContext {
    private static final Logger logger = LoggerFactory.getLogger(DBContext.class);
    private static HikariDataSource dataSource;

    static {
        try {
            initializeDataSource();
        } catch (Exception e) {
            logger.error("Failed to initialize database connection pool", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private static void initializeDataSource() {
        Properties props = loadDatabaseProperties();

        HikariConfig config = new HikariConfig();

        // Database connection settings
        config.setJdbcUrl(props.getProperty("db.url",
                "jdbc:sqlserver://localhost:1433;databaseName=CarSalesWebsite;encrypt=true;trustServerCertificate=true"));
        config.setUsername(props.getProperty("db.user", "sa"));
        config.setPassword(props.getProperty("db.password", "123456"));
        config.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

        // Connection pool settings
        config.setMaximumPoolSize(Integer.parseInt(props.getProperty("db.pool.maxSize", "10")));
        config.setMinimumIdle(Integer.parseInt(props.getProperty("db.pool.minIdle", "2")));
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes
        config.setLeakDetectionThreshold(60000); // 1 minute

        // Performance optimization
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.addDataSourceProperty("useServerPrepStmts", "true");

        // Connection test query
        config.setConnectionTestQuery("SELECT 1");

        // Pool name for monitoring
        config.setPoolName("CarSalesWebsite-Pool");

        dataSource = new HikariDataSource(config);
        logger.info("HikariCP connection pool initialized successfully");
    }

    /**
     * Load database properties from file or environment variables
     */
    private static Properties loadDatabaseProperties() {
        Properties props = new Properties();

        // Try to load from db.properties file first
        try (InputStream input = DBContext.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
                logger.info("Database properties loaded from db.properties");
                return props;
            }
        } catch (IOException e) {
            logger.warn("Could not load db.properties, using environment variables or defaults", e);
        }

        // Fallback to environment variables
        String url = System.getenv("DB_URL");
        String user = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        if (url != null) props.setProperty("db.url", url);
        if (user != null) props.setProperty("db.user", user);
        if (password != null) props.setProperty("db.password", password);

        return props;
    }

    /**
     * Get a connection from the pool
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized");
        }
        return dataSource.getConnection();
    }

    /**
     * Close the connection pool (call on application shutdown)
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP connection pool closed");
        }
    }

    /**
     * Get pool statistics for monitoring
     */
    public static String getPoolStats() {
        if (dataSource != null) {
            return String.format("Active: %d, Idle: %d, Total: %d, Waiting: %d",
                    dataSource.getHikariPoolMXBean().getActiveConnections(),
                    dataSource.getHikariPoolMXBean().getIdleConnections(),
                    dataSource.getHikariPoolMXBean().getTotalConnections(),
                    dataSource.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        return "DataSource not initialized";
    }
}