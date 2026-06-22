package com.myhindlab.abkat.models.couriermodule;

import java.io.Serializable;

public class ReceivedCourierModel implements Serializable {
    private String RecievedStatus;

    private String SentToLab;

    private String RecievedByLabCode;

    private String SentByLabCode;

    private String Amount;

    private String CourierNo;

    private String ReceivedTime;

    private String BoxCount;

    private String Remark;

    private String CourierStatus;

    private String CourierDate;

    private String CourierRecievedDate;

    private String CourierCompany;

    private String CourierDetailsId;

    private String SampleCount;

    private String SentByLab;

    private String SampleType;

    private String CourierSentTime;

    private String SentTime;

    private String BusNo;

    private String SentFilePath;

    private String ReceivedFilePath;

    private String CourierMode;

    private String ExpectedArrivalDate;

    private String ExpectedArrivalTime;

    private String SenderName;

    private String SenderMobNo;

    private String AmountPaidByReceiver;

    public String getSentByLabCode() {
        return SentByLabCode;
    }

    public void setSentByLabCode(String sentByLabCode) {
        SentByLabCode = sentByLabCode;
    }

    public String getSentFilePath() {
        return SentFilePath;
    }

    public void setSentFilePath(String sentFilePath) {
        SentFilePath = sentFilePath;
    }

    public String getReceivedFilePath() {
        return ReceivedFilePath;
    }

    public void setReceivedFilePath(String receivedFilePath) {
        ReceivedFilePath = receivedFilePath;
    }

    public String getBusNo() {
        return BusNo;
    }

    public void setBusNo(String busNo) {
        BusNo = busNo;
    }

    public String getDriverMobNo() {
        return DriverMobNo;
    }

    public void setDriverMobNo(String driverMobNo) {
        DriverMobNo = driverMobNo;
    }

    private String DriverMobNo;

    public String getRecievedStatus() {
        return RecievedStatus;
    }

    public void setRecievedStatus(String RecievedStatus) {
        this.RecievedStatus = RecievedStatus;
    }

    public String getSentToLab() {
        return SentToLab;
    }

    public void setSentToLab(String SentToLab) {
        this.SentToLab = SentToLab;
    }

    public String getRecievedByLabCode() {
        return RecievedByLabCode;
    }

    public void setRecievedByLabCode(String RecievedByLabCode) {
        this.RecievedByLabCode = RecievedByLabCode;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String Amount) {
        this.Amount = Amount;
    }

    public String getCourierNo() {
        return CourierNo;
    }

    public void setCourierNo(String CourierNo) {
        this.CourierNo = CourierNo;
    }

    public String getReceivedTime() {
        if (ReceivedTime != null) {
            return ReceivedTime;
        } else {
            return "NA";
        }
    }

    public void setReceivedTime(String ReceivedTime) {
        this.ReceivedTime = ReceivedTime;
    }

    public String getBoxCount() {
        return BoxCount;
    }

    public void setBoxCount(String BoxCount) {
        this.BoxCount = BoxCount;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getCourierStatus() {
        return CourierStatus;
    }

    public void setCourierStatus(String CourierStatus) {
        this.CourierStatus = CourierStatus;
    }

    public String getCourierDate() {
        return CourierDate;
    }

    public void setCourierDate(String CourierDate) {
        this.CourierDate = CourierDate;
    }

    public String getCourierRecievedDate() {
        if (CourierRecievedDate != null) {
            return CourierRecievedDate;
        } else {
            return "NA";
        }
    }

    public void setCourierRecievedDate(String CourierRecievedDate) {
        this.CourierRecievedDate = CourierRecievedDate;
    }

    public String getCourierCompany() {
        return CourierCompany;
    }

    public void setCourierCompany(String CourierCompany) {
        this.CourierCompany = CourierCompany;
    }

    public String getCourierDetailsId() {
        return CourierDetailsId;
    }

    public void setCourierDetailsId(String CourierDetailsId) {
        this.CourierDetailsId = CourierDetailsId;
    }

    public String getSampleCount() {
        return SampleCount;
    }

    public void setSampleCount(String SampleCount) {
        this.SampleCount = SampleCount;
    }

    public String getSentByLab() {
        return SentByLab;
    }

    public void setSentByLab(String SentByLab) {
        this.SentByLab = SentByLab;
    }

    public String getSampleType() {
        return SampleType;
    }

    public void setSampleType(String SampleType) {
        this.SampleType = SampleType;
    }

    public String getCourierSentTime() {
        return CourierSentTime;
    }

    public void setCourierSentTime(String courierSentTime) {
        CourierSentTime = courierSentTime;
    }

    public String getSentTime() {
        return SentTime;
    }

    public void setSentTime(String sentTime) {
        SentTime = sentTime;
    }

    public String getCourierMode() {
        return CourierMode;
    }

    public void setCourierMode(String courierMode) {
        CourierMode = courierMode;
    }

    public String getExpectedArrivalDate() {
        if (ExpectedArrivalDate != null) {
            return ExpectedArrivalDate;
        } else {
            return "NA";
        }
    }

    public void setExpectedArrivalDate(String expectedArrivalDate) {
        ExpectedArrivalDate = expectedArrivalDate;
    }

    public String getExpectedArrivalTime() {
        if (ExpectedArrivalTime != null) {
            return ExpectedArrivalTime;
        } else {
            return "NA";
        }
    }

    public void setExpectedArrivalTime(String expectedArrivalTime) {
        ExpectedArrivalTime = expectedArrivalTime;
    }

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String senderName) {
        SenderName = senderName;
    }

    public String getSenderMobNo() {
        return SenderMobNo;
    }

    public void setSenderMobNo(String senderMobNo) {
        SenderMobNo = senderMobNo;
    }

    public String getAmountPaidByReceiver() {
        if (AmountPaidByReceiver != null) {
            return AmountPaidByReceiver;
        } else {
            return "NA";
        }
    }

    public void setAmountPaidByReceiver(String amountPaidByReceiver) {
        AmountPaidByReceiver = amountPaidByReceiver;
    }
}
