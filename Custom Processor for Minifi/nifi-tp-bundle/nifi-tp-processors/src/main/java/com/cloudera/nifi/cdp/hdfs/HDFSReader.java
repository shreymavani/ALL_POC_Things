package com.cloudera.nifi.cdp.hdfs;

import java.io.IOException;
import java.security.PrivilegedExceptionAction;
import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

public class HDFSReader {


    public static void main(String... args) {
        try {
            new HDFSReader().readFileFromHDFS();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFileFromHDFS() throws IOException, InterruptedException {

        UserGroupInformation.createProxyUser("hdfs", UserGroupInformation.getCurrentUser()).doAs(new PrivilegedExceptionAction<Boolean>() {

            @Override public Boolean run() throws Exception {
                Configuration configuration = new Configuration();
                configuration.set("fs.hdfs.impl",
                        org.apache.hadoop.hdfs.DistributedFileSystem.class.getName()
                );
                configuration.set("fs.file.impl",
                        org.apache.hadoop.fs.LocalFileSystem.class.getName()
                );
                configuration.set("fs.defaultFS", "hdfs://spandey-1.spandey.root.hwx.site:8022");
                FileSystem fileSystem = FileSystem.get(configuration);

                final String fileName = "application_1671424190335_0001_1";
                final Path hdfsReadPath = new Path("/user/spark/applicationHistory/" + fileName);



                FSDataInputStream inputStream = fileSystem.open(hdfsReadPath);
                //Classical input stream usage
                String out= IOUtils.toString(inputStream, "UTF-8");
                System.out.println(out);
                /*BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                String line = null;
                while ((line=bufferedReader.readLine())!=null){
                    System.out.println(line);
                }*/
                inputStream.close();
                fileSystem.close();

                return Boolean.TRUE;
            }
        });


    }
}
