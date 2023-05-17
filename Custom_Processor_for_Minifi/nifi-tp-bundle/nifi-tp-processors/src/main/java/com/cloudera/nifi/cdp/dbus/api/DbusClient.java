package com.cloudera.nifi.cdp.dbus.api;

import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.dbus.model.Header;
import com.cloudera.nifi.cdp.dbus.model.PutRecordRequest;
import com.cloudera.nifi.cdp.dbus.model.PutRecordResponse;
import com.cloudera.nifi.cdp.dbus.model.Record;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.util.PrivateKeyInfoFactory;
import org.bouncycastle.jcajce.provider.asymmetric.edec.KeyFactorySpi;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbusClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbusClient.class);

    private static final int ED25519_KEY_LENGTH = 44;

    private static final String RSA_ALGORITHM = "RSA";

    private static final String ECDSA_ALGORITHM = "EC";

    private static final List<String> POSSIBLE_PEM_PRIVATE_KEY_ALGORITHMS = Arrays.asList(RSA_ALGORITHM, ECDSA_ALGORITHM);

    public static final String SERVICE_NAME = "dbus";

    private static final String ACCESS_KEY_ID = "8a136e61-aedd-43d0-bff6-f965665fdd72";

    private static final String PRIVATE_KEY = "aGvYV3Cuj1kZvNTtkpNlvCKqtVvUKC/xhjllz2+Suxs=";

    // private static final String PRIVATE_KEY = "-----BEGIN PRIVATE KEY-----\n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\nooGd6f1FQjBXl9bOD2yDppRbiraVgA==\n-----END PRIVATE KEY-----";
    private final Client client;

    private final String endpoint;

    private final String clientApplicationName;

    private final URI requestUri;

    private final Map<String, String> headers;

    private final String cmClusterUuid;

    private final Signer signer;

    public DbusClient(ClientFactory clientFactory, String endpoint, String clientApplicationName) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        client = clientFactory.create();
        this.endpoint = endpoint;
        this.clientApplicationName = clientApplicationName;
        this.headers = new HashMap<>();
        requestUri = URI.create(endpoint + "/dbus/putRecord").normalize();
        cmClusterUuid = "da3b4d25-bf9b-449e-89ec-18f5d0462aa1";
        signer = new Signer();
    }

    public PutRecordResponse getUploadUrl(String localPath, String streamName) throws IOException {
        WebTarget t = client.target(requestUri);
        Invocation.Builder builder = t.request();
        computeHeaders();
        String contentType = "application/json";
        for (Map.Entry<String, String> header : headers.entrySet()) {
            String headerName = header.getKey();
            if (headerName.equals(HttpHeaders.CONTENT_TYPE)) {
                contentType = header.getValue();
            } else {
                builder = builder.header(headerName, header.getValue());
            }
        }

        PutRecordRequest request = createUploadRequest(localPath, streamName);

        LOGGER.info("request : {}", headers);

        LOGGER.info("request : {}", request);

        final Response response = builder.post(Entity.json(request));

        LOGGER.info("Response : {}", response);

        if(response.getStatus() == 200) {
            return builder.post(Entity.json(request)).readEntity(PutRecordResponse.class);
        }

        throw new RuntimeException("Error : "+response);

    }


    private void computeHeaders() throws IOException {
        headers.put(HttpHeaders.USER_AGENT, buildUserAgent());
        headers.put(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.put(HttpHeaders.ACCEPT, "application/json");
        headers.put("x-altus-client-app", "DatabusUploader");
        computeAuthHeaders();
    }

    private void computeAuthHeaders() throws IOException {
        PrivateKey privateKey = decodePrivateKey(PRIVATE_KEY.replace("\\n", "\n"));
        String date = ZonedDateTime.now(ZoneId.of("GMT")).format(
                DateTimeFormatter.RFC_1123_DATE_TIME);
        String auth = signer.computeAuthHeader(
                "POST",
                 "application/json",
                date,
                requestUri.getPath(),
                ACCESS_KEY_ID,
                privateKey);
        headers.put("x-altus-date", date);
        headers.put("x-altus-auth", auth);
    }


    private String buildUserAgent() {
        return String.format("CDPSDK/%s Java/%s %s/%s",
                "0.1-SNAPSHOT",
                System.getProperty("java.version"),
                System.getProperty("os.name"),
                System.getProperty("os.version"));
    }

    private PrivateKey decodePrivateKey(String privateKey) throws IOException {
        if (privateKey.length() == ED25519_KEY_LENGTH) {
            return decodeEd25519PrivateKey(privateKey);
        } else {
            return decodePEMPrivateKey(privateKey);
        }
    }

    private PrivateKey decodeEd25519PrivateKey(String privateKey) throws IOException {
        byte[] seed = Base64.getDecoder().decode(privateKey);

            // We instantiate the KeyFactorySpi directly to avoid having to register
            // the BouncyCastle provider, and even if we did, this parameter based
            // instantiation is not exposed through the provider interfaces.
            Ed25519PrivateKeyParameters params = new Ed25519PrivateKeyParameters(seed, 0);
            PrivateKeyInfo info = PrivateKeyInfoFactory.createPrivateKeyInfo(params);
            return new KeyFactorySpi.Ed25519().generatePrivate(info);
    }

    private PrivateKey decodePEMPrivateKey(String privateKey) {
        privateKey = privateKey.replace("\\n", "\n");
        try (PemReader pemReader = new PemReader(new StringReader(privateKey))) {
            PemObject pemObject = pemReader.readPemObject();
            if (pemObject == null) {
                throw new RuntimeException("Invalid private key ");
            }
            PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(pemObject.getContent());

            // This distinguishes between the different algorithms. With Java9 we don't need the
            // try/catches and can check the type with: `privateKeySpec.getAlgorithm()`
            for (String algorithm: POSSIBLE_PEM_PRIVATE_KEY_ALGORITHMS) {
                try {
                    return KeyFactory.getInstance(algorithm).generatePrivate(privateKeySpec);
                } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
                    // no-op;
                }
            }
            throw new RuntimeException("Unable to decode private key: unknown algorithm.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to read private key: " + e.getMessage(), e);
        }
    }

    private PutRecordRequest createUploadRequest(String localPath, String streamName) {
        PutRecordRequest req = new PutRecordRequest();
        Record record = new Record();
        record.setStreamName(streamName);
        record.setPartitionKey(cmClusterUuid);
        record.setPayloadSize(Paths.get(localPath).toFile().length());
        try {
            addHeaders(record);
        } catch (JsonProcessingException e) {
            LOGGER.error("Error", e);
            throw new RuntimeException(e);
        }
        req.setRecord(record);
        return req;
    }

    private void addHeaders(Record record) throws JsonProcessingException {
        List<Header> headers = record.getHeaders();
        //headers.add(header("format", "JSON"));
        //headers.add(header("compression", "GZIP"));
        //headers.add(header("cm-id", "da3b4d25-bf9b-449e-89ec-18f5d0462aa1"));
        //headers.add(header("cdx-stream", "cluster-events"));
        //headers.add(header("cluster-type", "REGULAR"));
        //headers.add(header("time-zone", "UTC"));
        headers.add(header("cluster-id", cmClusterUuid));
        //headers.add(header("cdh-version", "7.2.16"));
       // headers.add(header("cluster-display-name", "TP-POC-Cluster"));
        //headers.add(header("cm-version", "7.8.0"));
    }

    private Header header(String name, String value) {
        Header header = new Header();
        header.setName(name);
        header.setValue(value);
        return header;
    }
}
