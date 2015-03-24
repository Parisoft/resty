package com.github.parisoft.resty.request.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import org.apache.http.ssl.TrustStrategy;

import com.github.parisoft.resty.client.Client;

/**
 * Implementation of {@link TrustStrategy} that ignores the certificate verification process.
 *
 * @author Andre Paris
 * @see Client#bypassSSL(boolean)
 */
public class BypassTrustStrategy implements TrustStrategy {

    @Override
    public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        return true;
    }

}
