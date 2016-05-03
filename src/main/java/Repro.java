import hudson.FilePath;

import java.io.File;
import java.net.URL;

public class Repro {

    private static final String URL_DEFAULT = "https://storage.googleapis.com/golang/go1.6.2.darwin-amd64.tar.gz";

    public static void main(String[] args) throws Exception {
        // Decide which URL to download
        final String url;
        if (args.length > 0) {
            url = args[0];
        } else {
            url = URL_DEFAULT;
        }

        // Create a temporary directory to extract the file to
        File tmpDir = new File(System.getProperty("java.io.tmpdir"), "filepath-download-untar");
        FilePath filePath = new FilePath(tmpDir);
        filePath.deleteRecursive();

        // Print some info
        System.out.println("URL to download file from: " + url);
        System.out.println("Path to extract file to:   " + tmpDir);

        boolean success = false;
        try {
            // Attempt to download and untar on-the-fly to the temporary directory
            filePath.installIfNecessaryFrom(new URL(url), null, null);
            System.err.println("Succeeded!");
            success = true;
        } catch (Exception e) {
            // Download failed; print the stacktrace
            System.err.println("Failed!");
            e.printStackTrace();
        } finally {
            // Check how many files we managed to extract before failing
            FilePath[] fileList = filePath.list("**");
            System.out.println("Extracted " + fileList.length + " files");

            // If we failed, show the few files that were extracted
            if (!success) {
                for (FilePath f : fileList) {
                    System.out.println(f);
                }
                System.exit(1);
            }
        }
    }

}
