package com.github.parisoft.resty.request;

/**
 * The request method token is the primary source of request semantics;
 * it indicates the purpose for which the request has made this request
 * and what is expected by the request as a successful result.
 *
 * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.1>RFC 7231</a>
 * @author Andre Paris
 *
 */
public enum RequestMethod {

    /**
     * Transfer a current representation of the target resource.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.1>RFC 7231</a>
     */
    GET,

    /**
     * Same as GET, but only transfer the status line and header section.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.2>RFC 7231</a>
     */
    HEAD,

    /**
     * Perform resource-specific processing on the request payload.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.3>RFC 7231</a>
     */
    POST,

    /**
     * Replace all current representations of the target resource with the request payload.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.4>RFC 7231</a>
     */
    PUT,

    /**
     * Remove all current representations of the target resource.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.5>RFC 7231</a>
     */
    DELETE,

    /**
     * Establish a tunnel to the server identified by the target resource.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.6>RFC 7231</a>
     */
    CONNECT,

    /**
     * Describe the communication options for the target resource.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.7>RFC 7231</a>
     */
    OPTIONS,

    /**
     * Perform a message loop-back test along the path to the target resource.
     *
     * @see <a href=https://tools.ietf.org/html/rfc7231#section-4.3.8>RFC 7231</a>
     */
    TRACE
}
