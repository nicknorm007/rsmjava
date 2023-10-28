package com.nicknorman.com.rsmservice;

public class ReportData {

    public ReportData()
    {

    }
    public ReportData(DownloadInfo item)
    {

        this.reportItem = item;
    }

    public DownloadInfo getReportItem() {
        return reportItem;
    }

    public void setReportItem(DownloadInfo infoitem) {
        this.reportItem = infoitem;
    }

    DownloadInfo reportItem;
}
