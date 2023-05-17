package com.cloudera.nifi.cdp.file.compress;

import com.google.common.base.Preconditions;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class FileCompressor {

    public File compressAndDeleteDir(File dir) throws IOException {
        Preconditions.checkArgument(dir.isDirectory());
        String tarGzPath = dir.getAbsolutePath() + ".tar.gz";
        File ret = new File(tarGzPath);
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        GzipCompressorOutputStream gzOut = null;
        TarArchiveOutputStream tOut = null;
        try {
            fOut = new FileOutputStream(ret);
            bOut = new BufferedOutputStream(fOut);
            gzOut = new GzipCompressorOutputStream(bOut);
            tOut = new TarArchiveOutputStream(gzOut);
            // allow long file names
            tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
            tOut.setBigNumberMode(TarArchiveOutputStream.BIGNUMBER_POSIX);
            addFileToTarGz(tOut, dir.toString(), "");
            FileUtils.deleteDirectory(dir);
        } finally {
            IOUtils.closeQuietly(tOut);
            IOUtils.closeQuietly(gzOut);
            IOUtils.closeQuietly(bOut);
            IOUtils.closeQuietly(fOut);
        }
        return ret;
    }

    private void addFileToTarGz(TarArchiveOutputStream tOut, String path, String base)
            throws IOException {
        File f = new File(path);
        String entryName = base + f.getName();
        TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);
        tOut.putArchiveEntry(tarEntry);

        if (f.isFile()) {
            IOUtils.copy(new FileInputStream(f), tOut);
            tOut.closeArchiveEntry();
        } else {
            tOut.closeArchiveEntry();
            File[] children = f.listFiles();
            if (children != null) {
                for (File child : children) {
                    addFileToTarGz(tOut, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
    }
}
