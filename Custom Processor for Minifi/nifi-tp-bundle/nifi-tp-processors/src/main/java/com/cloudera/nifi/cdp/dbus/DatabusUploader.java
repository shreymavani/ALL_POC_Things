package com.cloudera.nifi.cdp.dbus;


import com.cloudera.nifi.cdp.client.ClientFactory;
import com.cloudera.nifi.cdp.dbus.api.DbusClient;
import com.cloudera.nifi.cdp.dbus.model.PutRecordResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabusUploader {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabusUploader.class);

    private static final String STREAM_NAME = "SparkTaskLog";
    private final DbusClient dbusClient;

    //private static final String LOCAL_PATH = "/Users/spandey/nifi-test-dir/minifi-test.log.20221126";

    private static final String LOCAL_PATH = "/Users/spandey/nifi-test-dir/filesToUpload.tar.gz";

    public DatabusUploader(String dbusServiceUrl) {
        try {
            dbusClient = new DbusClient(new ClientFactory(), dbusServiceUrl, "DatabusUploader");
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        try {
            
            new DatabusUploader("https://spandey2-api.sigma-dev.cloudera.com").uploadFile(LOCAL_PATH, STREAM_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uploadFile(String localPath, String streamName) {
        try {
            final PutRecordResponse response = dbusClient.getUploadUrl(localPath, streamName);
            LOGGER.info("response : {}", response);
            uploadObject(response.getRecord().getUploadUrl(), localPath);
        } catch (Exception e) {
            LOGGER.error("Error: ", e);
            throw new RuntimeException(e);
        }
    }

    private void uploadObject(String uploadUrl, String localPath) throws IOException {

        final URL url = new URL(uploadUrl);
        final File uploadFile = new File(localPath);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        connection.setRequestMethod("PUT");
        BufferedOutputStream out = new BufferedOutputStream(
                connection.getOutputStream());
        FileInputStream in = new FileInputStream(uploadFile);
        IOUtils.copy(in,out);
        out.flush();
        out.close();
        in.close();
        int responseCode = connection.getResponseCode();

        LOGGER.info("Upload File Response code {}", responseCode);
    }



}
