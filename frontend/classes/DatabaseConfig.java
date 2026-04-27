package classes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Utility class for database operations. Handles JDBC connections and data
 * persistence for the VCRTS system.
 */
public class DatabaseConfig {

    // configuration from environment and/or .env file.
    private static final Properties ENV_PROPERTIES = loadEnvFile();
    private static final String DB_HOST = readConfig("DB_HOST", "localhost");
    private static final String DB_PORT = readConfig("DB_PORT", "3306");
    private static final String DB_NAME = readConfig("DB_NAME", "vcrts");
    private static final String URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME;
    private static final String USERNAME = readConfig("DB_USER", "root");
    private static final String PASSWORD = readConfig("DB_PASSWORD", "");

    private static Properties loadEnvFile() {
        Properties properties = new Properties();
        File envFile = new File(".env");
        if (!envFile.exists()) {
            return properties;
        }
        try (FileInputStream in = new FileInputStream(envFile)) {
            properties.load(in);
        } catch (IOException e) {
        }
        return properties;
    }

    private static String readConfig(String key, String defaultValue) {
        String envValue = System.getenv(key);
        if (envValue != null && !envValue.trim().isEmpty()) {
            return envValue.trim();
        }
        String fileValue = ENV_PROPERTIES.getProperty(key);
        if (fileValue != null && !fileValue.trim().isEmpty()) {
            return fileValue.trim();
        }
        return defaultValue;
    }

    public static String getURL() {
        return URL;
    }

    public static String getUsername() {
        return USERNAME;
    }

    public static String getPassword() {
        return PASSWORD;
    }

}
