package com.nicknorman.com.rsmservice;

public interface DownloadInfo{
    public int getSize(); //bytes
    public String getOriginalFileName();
    public String getFileKey();
    public String getDownloadURL();
}
