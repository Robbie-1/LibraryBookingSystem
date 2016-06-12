package uk.ac.reigate.rm13030.util;

/**
 * @author Robbie McLeod <rm13030@reigate.ac.uk>
 */

public class SimpleLogger {

    public SimpleLogger() {}

    protected enum MessageType {

        DEBUG,
        INFO,
        WARNING,
        ERROR;

    }

    /**
     * Example usage;
     * 		log(this.getClass(), MessageType.ERROR, "JAR non-existent in given directory", "Exiting program...");
     * 
     * Prints:
     * 		--> [Splash] [ERROR]: JAR non-existent in given directory <+> Exiting program...
     */

    @
    SuppressWarnings("rawtypes")
    protected void log(Class thisClass, MessageType mT, String message, String... detail) {
        System.out.println(detail.length >= 1 ? "[" + thisClass.getSimpleName() + "] [" + mT + "]: " + message + " <+>" + buildString(detail) : "[" + thisClass.getSimpleName() + "] [" + mT + "]: " + message);
    }

    private String buildString(String...str) {
        String build = "";
        for (String arg : str) {
            build += (" " + arg);
        }
        return build;
    }

}