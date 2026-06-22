package com.myhindlab.abkat.models.couriermodule;

import java.io.Serializable;
import java.util.List;

public class CourierForwardedModel implements Serializable {

    private String status;
    private String message;
    private List<OutputBean> output;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(List<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean implements Serializable {
        /**
         * CourierID : 22
         * RegistrationLabID : 22
         * RegistrationLab : Latur
         * CourierToLabID : 112
         * CourierToLab : Solapur
         * ProcessLabID : 29
         * ProcessLab : Akurdi(PUNE)
         * CategoryID : 1
         * CourierType : Samples
         * CourierDate : 2020-09-15
         * CourierTime : 12:34 PM
         * ModeId : 1
         * ModeOfTransport : Courier Service
         * CompanyName : djdjjdjsks
         * DocketNo : 8483828282
         * ContactNo : 8864646464
         * AmountPaidbySender : 86
         * NoOfBoxes : 4
         * NoOfSamples : 25
         * ExpArrivalDate : 2020-09-16
         * ExpArrivalTime : 12:35 PM
         * Barcode : 5894933958
         * SampleTempID : 1
         * SampleTempName : Room Temp
         * PhotoPath : Send_22_112_29_2182301
         * BusVehicleNumber :
         * ProjectID : 1
         * ForwerdedCourierID : 0
         * UserID : 437
         * SendBy : VICKEY KALYANRAO KOKATE
         * Status : Not Received
         */

        private String CourierID;
        private String RegistrationLabID;
        private String RegistrationLab;
        private String CourierToLabID;
        private String CourierToLab;
        private String ProcessLabID;
        private String ProcessLab;
        private String CategoryID;
        private String CourierType;
        private String CourierDate;
        private String CourierTime;
        private String ModeId;
        private String ModeOfTransport;
        private String CompanyName;
        private String DocketNo;
        private String ContactNo;
        private String AmountPaidbySender;
        private String NoOfBoxes;
        private String NoOfSamples;
        private String ExpArrivalDate;
        private String ExpArrivalTime;
        private String Barcode;
        private String SampleTempID;
        private String SampleTempName;
        private String PhotoPath;
        private String BusVehicleNumber;
        private String ProjectID;
        private String ForwerdedCourierID;
        private String UserID;
        private String SendBy;
        private String Status;
        private String IsSampleType;
        private String RunnerBoySendName;
        private String InitCourierID;
        private String ReceivedDate;
        private String ReceivedTime;
        private List<TubeDetails> TubeDetails;
        private boolean isChecked;

        public String getInitCourierID() {
            return InitCourierID;
        }

        public void setInitCourierID(String initCourierID) {
            InitCourierID = initCourierID;
        }

        public String getReceivedDate() {
            return ReceivedDate;
        }

        public void setReceivedDate(String receivedDate) {
            ReceivedDate = receivedDate;
        }

        public String getReceivedTime() {
            return ReceivedTime;
        }

        public void setReceivedTime(String receivedTime) {
            ReceivedTime = receivedTime;
        }

        public String getCourierID() {
            return CourierID;
        }

        public void setCourierID(String CourierID) {
            this.CourierID = CourierID;
        }

        public String getRegistrationLabID() {
            return RegistrationLabID;
        }

        public void setRegistrationLabID(String RegistrationLabID) {
            this.RegistrationLabID = RegistrationLabID;
        }

        public String getRegistrationLab() {
            return RegistrationLab;
        }

        public void setRegistrationLab(String RegistrationLab) {
            this.RegistrationLab = RegistrationLab;
        }

        public String getCourierToLabID() {
            return CourierToLabID;
        }

        public void setCourierToLabID(String CourierToLabID) {
            this.CourierToLabID = CourierToLabID;
        }

        public String getCourierToLab() {
            return CourierToLab;
        }

        public void setCourierToLab(String CourierToLab) {
            this.CourierToLab = CourierToLab;
        }

        public String getProcessLabID() {
            return ProcessLabID;
        }

        public void setProcessLabID(String ProcessLabID) {
            this.ProcessLabID = ProcessLabID;
        }

        public String getProcessLab() {
            return ProcessLab;
        }

        public void setProcessLab(String ProcessLab) {
            this.ProcessLab = ProcessLab;
        }

        public String getCategoryID() {
            return CategoryID;
        }

        public void setCategoryID(String CategoryID) {
            this.CategoryID = CategoryID;
        }

        public String getCourierType() {
            return CourierType;
        }

        public void setCourierType(String CourierType) {
            this.CourierType = CourierType;
        }

        public String getCourierDate() {
            return CourierDate;
        }

        public void setCourierDate(String CourierDate) {
            this.CourierDate = CourierDate;
        }

        public String getCourierTime() {
            return CourierTime;
        }

        public void setCourierTime(String CourierTime) {
            this.CourierTime = CourierTime;
        }

        public String getModeId() {
            return ModeId;
        }

        public void setModeId(String ModeId) {
            this.ModeId = ModeId;
        }

        public String getModeOfTransport() {
            return ModeOfTransport;
        }

        public void setModeOfTransport(String ModeOfTransport) {
            this.ModeOfTransport = ModeOfTransport;
        }

        public String getCompanyName() {
            return CompanyName;
        }

        public void setCompanyName(String CompanyName) {
            this.CompanyName = CompanyName;
        }

        public String getDocketNo() {
            return DocketNo;
        }

        public void setDocketNo(String DocketNo) {
            this.DocketNo = DocketNo;
        }

        public String getContactNo() {
            return ContactNo;
        }

        public void setContactNo(String ContactNo) {
            this.ContactNo = ContactNo;
        }

        public String getAmountPaidbySender() {
            return AmountPaidbySender;
        }

        public void setAmountPaidbySender(String AmountPaidbySender) {
            this.AmountPaidbySender = AmountPaidbySender;
        }

        public String getNoOfBoxes() {
            return NoOfBoxes;
        }

        public void setNoOfBoxes(String NoOfBoxes) {
            this.NoOfBoxes = NoOfBoxes;
        }

        public String getNoOfSamples() {
            return NoOfSamples;
        }

        public void setNoOfSamples(String NoOfSamples) {
            this.NoOfSamples = NoOfSamples;
        }

        public String getExpArrivalDate() {
            return ExpArrivalDate;
        }

        public void setExpArrivalDate(String ExpArrivalDate) {
            this.ExpArrivalDate = ExpArrivalDate;
        }

        public String getExpArrivalTime() {
            return ExpArrivalTime;
        }

        public void setExpArrivalTime(String ExpArrivalTime) {
            this.ExpArrivalTime = ExpArrivalTime;
        }

        public String getBarcode() {
            return Barcode;
        }

        public void setBarcode(String Barcode) {
            this.Barcode = Barcode;
        }

        public String getSampleTempID() {
            return SampleTempID;
        }

        public void setSampleTempID(String SampleTempID) {
            this.SampleTempID = SampleTempID;
        }

        public String getSampleTempName() {
            return SampleTempName;
        }

        public void setSampleTempName(String SampleTempName) {
            this.SampleTempName = SampleTempName;
        }

        public String getPhotoPath() {
            return PhotoPath;
        }

        public void setPhotoPath(String PhotoPath) {
            this.PhotoPath = PhotoPath;
        }

        public String getBusVehicleNumber() {
            return BusVehicleNumber;
        }

        public void setBusVehicleNumber(String BusVehicleNumber) {
            this.BusVehicleNumber = BusVehicleNumber;
        }

        public String getProjectID() {
            return ProjectID;
        }

        public void setProjectID(String ProjectID) {
            this.ProjectID = ProjectID;
        }

        public String getForwerdedCourierID() {
            return ForwerdedCourierID;
        }

        public void setForwerdedCourierID(String ForwerdedCourierID) {
            this.ForwerdedCourierID = ForwerdedCourierID;
        }

        public String getUserID() {
            return UserID;
        }

        public void setUserID(String UserID) {
            this.UserID = UserID;
        }

        public String getSendBy() {
            return SendBy;
        }

        public void setSendBy(String SendBy) {
            this.SendBy = SendBy;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String Status) {
            this.Status = Status;
        }

        public String getIsSampleType() {
            return IsSampleType;
        }

        public void setIsSampleType(String isSampleType) {
            IsSampleType = isSampleType;
        }

        public String getRunnerBoySendName() {
            return RunnerBoySendName;
        }

        public void setRunnerBoySendName(String runnerBoySendName) {
            RunnerBoySendName = runnerBoySendName;
        }

        public List<OutputBean.TubeDetails> getTubeDetails() {
            return TubeDetails;
        }

        public void setTubeDetails(List<OutputBean.TubeDetails> tubeDetails) {
            TubeDetails = tubeDetails;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public static class TubeDetails implements Serializable {

            private String CourierID;
            private String TubeId;
            private String TubeContent;
            private String TotalCount;

            public String getCourierID() {
                return CourierID;
            }

            public void setCourierID(String CourierID) {
                this.CourierID = CourierID;
            }

            public String getTubeId() {
                return TubeId;
            }

            public void setTubeId(String tubeId) {
                TubeId = tubeId;
            }

            public String getTubeContent() {
                return TubeContent;
            }

            public void setTubeContent(String TubeContent) {
                this.TubeContent = TubeContent;
            }

            public String getTotalCount() {
                return TotalCount;
            }

            public void setTotalCount(String TotalCount) {
                this.TotalCount = TotalCount;
            }
        }
    }
}
