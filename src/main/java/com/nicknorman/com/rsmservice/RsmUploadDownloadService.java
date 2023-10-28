package com.nicknorman.com.rsmservice;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        for (Thread thread : uploadsAndDownloads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }

    private synchronized void downloadAndUpload(DownloadInfo downloadInfo) throws Exception {
        String fileKey = downloadInfo.getFileKey();
        int size = downloadInfo.getSize();

        List<ReportData> reportEntries = new ArrayList<>();
        int totalSuccesses = 0;
        int totalFailures = 0;
        long startTime = System.currentTimeMillis();

        if (size > MAX_DOWNLOAD_MB) {
            throw new Exception();
        }

        String fileName = downloadInfo.getOriginalFileName();
        String fileExtension = getFileExtension(fileName);

        if (fileExtensionIsNotAllowed(fileExtension)) {
            throw new IllegalArgumentException("File extension not allowed.");
        }

        InputStream dataStream = downloadService.downloadFile(downloadInfo.getDownloadURL());

        try {
            uploadService.doUpload(fileKey, dataStream, size);
        } finally {
            dataStream.close();
        }
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

        RsmUploadDownloadService dataTransferService = new RsmUploadDownloadService(downloadService, uploadService);

        long packageId = 11111;
        try {
            dataTransferService.downloadFromOneAndUploadToAnother(packageId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
