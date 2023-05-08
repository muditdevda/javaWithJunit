package com.qat.ssl;

import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlakySSLConfigurator implements X509TrustManager, HostnameVerifier {

	private static Logger logger = LoggerFactory.getLogger(FlakySSLConfigurator.class);

	public X509Certificate[] getAcceptedIssuers() {
		return ArrayUtils.toArray();
	}

	@Override
	public void checkClientTrusted(X509Certificate[] certs, String authType) {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] certs, String authType) {
	}

	public boolean verify(String hostname, SSLSession session) {
		return true;
	}

	public static void install() {
		try {
			FlakySSLConfigurator trustAll = new FlakySSLConfigurator();
			SSLContext sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, new TrustManager[] { trustAll }, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(trustAll);
		} catch (Exception e) {
			logger.error("Exception disabling ssl", e);
		}
	}
}
