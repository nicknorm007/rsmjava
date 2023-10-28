package com.nicknorman.com.rsmservice;

import java.io.InputStream;

public interface DownloadInfo{
    public int getSize(); //bytes
    public String getOriginalFileName();
    public String getFileKey();
    public String getDownloadURL();
    public InputStream downloadFile(String downloadURL);
}
