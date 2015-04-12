package com.whiteboxteam.gliese.data.server;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import com.google.common.io.ByteStreams;
import com.whiteboxteam.gliese.data.server.content.FileContentHandler;
import com.whiteboxteam.gliese.data.server.content.JSONContentHandler;
import com.whiteboxteam.gliese.data.server.keystore.ClientKeyStore;
import com.whiteboxteam.gliese.data.server.keystore.MyAnkaaKeyStore;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.zip.GZIPInputStream;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 23.01.14
 * Time: 17:58
 */
public final class ServerHelper {

    private static final int CONNECTION_TIMEOUT = 3000;
    private static final int SOCKET_TIMEOUT = 5000;

    public static JSONObject downloadJSONObject(String url) throws IOException {
        InputStream inputStream = getGetInputStream(url);
        return (JSONObject) JSONContentHandler.getContent(ByteStreams.toByteArray(inputStream));
    }

    private static InputStream getGetInputStream(String url) throws IOException {
        HttpEntity entity = getHttpGetEntity(url);
        InputStream stream = entity.getContent();
        if (entity.getContentEncoding() != null && "gzip".equalsIgnoreCase(entity.getContentEncoding().getValue())) {
            stream = new GZIPInputStream(stream);
        }
        return stream;
    }

    private static HttpEntity getHttpGetEntity(String url) throws IOException {
        HttpResponse response = getHttpGetResponse(url);
        int statusCode = response.getStatusLine().getStatusCode();
        if ((statusCode == HttpStatus.SC_OK) || (statusCode == HttpStatus.SC_NOT_MODIFIED)) return response.getEntity();
        else throw new IOException("Wrong Status Code returned from server: " + String.valueOf(statusCode));
    }

    private static HttpResponse getHttpGetResponse(String url) throws IOException {
        HttpClient client = buildHttpClient();
        HttpGet request = new HttpGet(url);
        request.addHeader("Accept-Encoding", "gzip");
        return client.execute(request);
    }

    private static HttpClient buildHttpClient() {
        try {
            SchemeRegistry registry = getSchemeRegistry();
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpParams, SOCKET_TIMEOUT);
            return new DefaultHttpClient(new ThreadSafeClientConnManager(httpParams, registry), httpParams);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static SchemeRegistry getSchemeRegistry() throws KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException, KeyManagementException, UnrecoverableKeyException {
        KeyStore myAnkaaKeyStore = getMyAnkaaKeyStore();
        KeyStore clientKeyStore = getClientKeyStore();
        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(clientKeyStore, null, myAnkaaKeyStore);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("https", sslSocketFactory, 443));
        return registry;
    }

    private static KeyStore getMyAnkaaKeyStore() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException {
        KeyStore serverRootCert = KeyStore.getInstance("BKS");
        serverRootCert.load(new ByteArrayInputStream(MyAnkaaKeyStore.MYANKAA_KEY_STORE),
                            MyAnkaaKeyStore.MYANKAA_KEY_STORE_PASSWORD);
        return serverRootCert;
    }

    private static KeyStore getClientKeyStore() throws KeyStoreException, IOException, NoSuchAlgorithmException,
            CertificateException {
        KeyStore clientCert = KeyStore.getInstance("pkcs12");
        clientCert.load(new ByteArrayInputStream(ClientKeyStore.CLIENT_KEY_STORE), ClientKeyStore
                .CLIENT_KEY_STORE_PASSWORD);
        return clientCert;
    }

    public static JSONArray downloadJSONArray(String url) throws IOException {
        InputStream inputStream = getGetInputStream(url);
        return (JSONArray) JSONContentHandler.getContent(ByteStreams.toByteArray(inputStream));
    }

    public static File downloadFile(Context context, String url) throws IOException {
        InputStream inputStream = getGetInputStream(url);
        return FileContentHandler.getContent(context, ByteStreams.toByteArray(inputStream));
    }

    public static void uploadJSONObject(Context context, String url, JSONObject json) throws IOException {
        HttpResponse response = getHttpPostResponse(context, url, json);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK)
            throw new IOException("Wrong Status Code returned from server: " + String.valueOf(statusCode));
    }

    private static HttpResponse getHttpPostResponse(Context context, String url, JSONObject json) throws IOException {
        HttpPost request = getJSONHttpPost(context, url, json);
        HttpClient client = buildHttpClient();
        return client.execute(request);
    }

    private static HttpPost getJSONHttpPost(Context context, String url, JSONObject json) throws IOException {
        HttpPost request = new HttpPost(url);

        byte[] content = json.toString().getBytes("UTF-8");
        AbstractHttpEntity compressedEntity = AndroidHttpClient.getCompressedEntity(content, context
                .getContentResolver());
        request.setEntity(compressedEntity);

        request.setHeader("Content-type", "application/json");
        if (content.length > AndroidHttpClient.getMinGzipSize(context.getContentResolver())) {
            request.setHeader("Content-Encoding", "gzip");
        }
        return request;
    }

    public static void uploadQueryString(String url) throws IOException {
        HttpResponse response = getHttpGetResponse(url);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode != HttpStatus.SC_OK)
            throw new IOException("Wrong Status Code returned from server: " + String.valueOf(statusCode));
    }

}
