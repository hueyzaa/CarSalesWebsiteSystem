package util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class ApplicationListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("Application starting...");

        try {
            // Test database connection
            DBContext.getConnection().close();
            logger.info("Database connection pool initialized successfully");
            logger.info("Pool stats: {}", DBContext.getPoolStats());

        } catch (Exception e) {
            logger.error("Failed to initialize database connection", e);
        }

        sce.getServletContext().log("Car Sales Website application started");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Application shutting down...");

        try {
            // Close HikariCP connection pool
            DBContext.closeDataSource();
            logger.info("Database connection pool closed successfully");

        } catch (Exception e) {
            logger.error("Error closing database connection pool", e);
        }

        sce.getServletContext().log("Car Sales Website application stopped");
    }
}