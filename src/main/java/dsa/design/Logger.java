package dsa.design;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ============================================================================
 * Implement Logger (Singleton + Strategy Pattern)
 * ============================================================================
 * Companies: Amazon, Microsoft | Difficulty: Medium
 *
 * Thread-safe singleton logger with configurable log levels and appenders.
 */
public class Logger {

    enum Level { DEBUG, INFO, WARN, ERROR }

    interface Appender { void append(String message); }

    private static volatile Logger instance;
    private Level minLevel = Level.INFO;
    private Appender appender = System.out::println; // default: console

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (Logger.class) {
                if (instance == null) instance = new Logger();
            }
        }
        return instance;
    }

    public void setLevel(Level level) { this.minLevel = level; }
    public void setAppender(Appender appender) { this.appender = appender; }

    public void log(Level level, String message) {
        if (level.ordinal() >= minLevel.ordinal()) {
            String formatted = String.format("[%s] [%s] [%s] %s",
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS")),
                    level, Thread.currentThread().getName(), message);
            synchronized (this) { appender.append(formatted); }
        }
    }

    public void debug(String msg) { log(Level.DEBUG, msg); }
    public void info(String msg) { log(Level.INFO, msg); }
    public void warn(String msg) { log(Level.WARN, msg); }
    public void error(String msg) { log(Level.ERROR, msg); }

    public static void main(String[] args) {
        System.out.println("=== Logger Implementation ===\n");
        Logger log = Logger.getInstance();

        log.setLevel(Level.DEBUG);
        log.debug("Debug message");
        log.info("Info message");
        log.warn("Warning message");
        log.error("Error message");

        log.setLevel(Level.WARN);
        System.out.println("\nAfter setLevel(WARN):");
        log.debug("Hidden — below WARN");
        log.info("Hidden — below WARN");
        log.warn("Visible — WARN level");
        log.error("Visible — ERROR level");
    }
}
