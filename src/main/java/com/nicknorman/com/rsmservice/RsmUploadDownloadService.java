package com.nicknorman.com.rsmservice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//I attempted to sketch out code for the multi-threaded test task and implement the methods asked for and generate
// some sample structure.  I was not familiar enough with developing java multithreaded code to accurately complete this
// but wanted to give it an attempt and take it as far as I could.

public class RsmUploadDownloadService {
    private DownloadService downloadService;
    private UploadService uploadService;
    private static final int MAX_DOWNLOAD_MB = 1024 * 1000; // 100MB?

    public RsmUploadDownloadService(DownloadService downloadService, UploadService uploadService) {
        this.downloadService = downloadService;
        this.uploadService = uploadService;
    }

    public void downloadFromOneAndUploadToAnother(long packageId) {
        List<DownloadInfo> downloadInfoItems = downloadService.getDownloadInfos(packageId);

        ExecutorService executorService = Executors.newFixedThreadPool(downloadInfoItems.size());
        //is every upload and download its own thread? maybe need a list of threads??
        List<Thread> uploadsAndDownloads = new ArrayList<>();
        List<ReportData> data = new ArrayList<>();

        for (DownloadInfo entry : downloadInfoItems) {
            uploadsAndDownloads.add(new Thread(() -> {
                try {
                    long start = System.currentTimeMillis();
                    uploadService.doUpload(entry.getFileKey(),
                            entry.downloadFile(entry.getOriginalFileName()), entry.getSize());
                    long end = System.currentTimeMillis();
                    data.add(new ReportData(entry));
                } catch (Exception e) {
                }
            }));
        }

        // was unsure here - maybe start a thread then wait for the other to complete first?
        for (Thread thread : uploadsAndDownloads) {
            thread.start();
        }

        for (Thread t : uploadsAndDownloads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }


    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        return fileName.substring(dotIndex + 1).toLowerCase();
    }

    private boolean fileExtensionIsNotAllowed(String extension) {
        List<String> extensionsNotAllowed = List.of("cmd", "com", "dll", "dmg", "exe", "iso", "jar", "js");
        return extensionsNotAllowed.contains(extension);
    }

    public static void main(String[] args) {
        DownloadService downloadService = new DownloadServiceImpl();
        UploadService uploadService = new UploadServiceImpl();

        RsmUploadDownloadService rsmDownloadService = new RsmUploadDownloadService(downloadService, uploadService);

        long packageId = 11111;
        try {
            rsmDownloadService.downloadFromOneAndUploadToAnother(packageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
