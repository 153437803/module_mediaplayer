package lib.kalu.mediaplayer.cache.ram;

/**
 * Indicates any error in work of {@link ProxyCache}.
 */
public class ProxyCacheException extends Exception {

    private static final String LIBRARY_VERSION = ". Version: module_mediaplayer_videocache";

    public ProxyCacheException(String message) {
        super(message + LIBRARY_VERSION);
    }

    public ProxyCacheException(String message, Throwable cause) {
        super(message + LIBRARY_VERSION, cause);
    }

    public ProxyCacheException(Throwable cause) {
        super("No explanation error" + LIBRARY_VERSION, cause);
    }
}
