package com.symphony.bdk.core.api.invoker;

import lombok.Getter;
import org.apiguardian.api.API;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.GenericType;

/**
 * Main exception raised when invoking {@link ApiClient#invokeAPI(String, String, List, Object, Map, Map, Map, String, String, String[], GenericType)}.
 *
 * Initially generated by the OpenAPI Maven Generator
 */
@Getter
@API(status = API.Status.STABLE)
public class ApiException extends Exception {

    private int code = 0;
    private Map<String, List<String>> responseHeaders = null;
    private String responseBody = null;

    /**
     * Creates new {@link ApiException} instance.
     *
     * @param message the detail message.
     * @param throwable the cause.
     */
    public ApiException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Creates new {@link ApiException} instance.
     *
     * @param code the HTTP response status code.
     * @param message the detail message.
     */
    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * Creates new {@link ApiException} instance.
     *
     * @param code the HTTP response status code.
     * @param message the detail message.
     * @param responseHeaders list of headers returned by the server.
     * @param responseBody content of the response sent back by the server.
     */
    public ApiException(int code, String message, Map<String, List<String>> responseHeaders, String responseBody) {
        this(code, message);
        this.responseHeaders = responseHeaders;
        this.responseBody = responseBody;
    }

    /**
     * Check if response status if unauthorized or not.
     *
     * @return true if response status is 401, false otherwise
     */
    public boolean isUnauthorized() {
        return this.code == HttpURLConnection.HTTP_UNAUTHORIZED;
    }
}
