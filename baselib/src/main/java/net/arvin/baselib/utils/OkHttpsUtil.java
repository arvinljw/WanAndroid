package net.arvin.baselib.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by arvinljw on 2018/10/31 18:05
 * Function：
 * Desc：
 */
public class OkHttpsUtil {
    private static final String TAG = "OkHttpsUtil";

    private static InputStream[] getCertificatesByAssert(Context context, String... certificateNames) {
        if (context == null) {
            ALog.d(TAG, "context is empty");
            return null;
        }
        if (certificateNames == null) {
            ALog.d(TAG, "certificate is empty");
            return null;
        }

        AssetManager assets = context.getAssets();
        InputStream[] certificates = new InputStream[certificateNames.length];
        for (int i = 0; i < certificateNames.length; i++) {
            String certificateName = certificateNames[i];
            try {
                certificates[i] = assets.open(certificateName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return certificates;
    }

    public static SSLSocketFactory getSslSocketFactory(Context context, String... certificateNames) {
        InputStream[] certificates = getCertificatesByAssert(context, certificateNames);
        if (certificates == null) {
            return null;
        }
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
