package com.nicknorman.com.rsmservice;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RsmUploadDownloadService {
    private DownloadService downloadService;
    private UploadService uploadService;
    private static final int MAX_DOWNLOAD_MB = 1024 * 100; // 100MB?

    public RsmUploadDownloadService(DownloadService downloadService, UploadService uploadService) {
        this.downloadService = downloadService;
        this.uploadService = uploadService;
    }

    public void downloadFromOneAndUploadToAnother(long packageId) {
        List<DownloadInfo> downloadInfos = downloadService.getDownloadInfos(packageId);

        ExecutorService executorService = Executors.newFixedThreadPool(downloadInfos.size());

        for (DownloadInfo downloadInfo : downloadInfos) {
            executorService.execute(() -> {
                try {
                    downloadAndUpload(downloadInfo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
    }

    private synchronized void downloadAndUpload(DownloadInfo downloadInfo) throws Exception {
        String fileKey = downloadInfo.getFileKey();
        int size = downloadInfo.getSize();

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
            // Handle exceptions here
            e.printStackTrace();
        }
    }
}
