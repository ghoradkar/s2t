package com.myhindlab.abkat.activities.attendance_details.model;


import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AttendanceImageModel {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("output")
    @Expose
    private List<Output> output;

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

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }


    public class Output {

        @SerializedName("CampId")
        @Expose
        private Integer campId;
        @SerializedName("Lattitude")
        @Expose
        private Double lattitude;
        @SerializedName("Longitude")
        @Expose
        private Double longitude;
        @SerializedName("In_Image")
        @Expose
        private String inImage;
        @SerializedName("In_Image_UploadedOn")
        @Expose
        private String inImageUploadedOn;
        @SerializedName("Out_Image")
        @Expose
        private String outImage;
        @SerializedName("Out_Image_UploadedOn")
        @Expose
        private String outImageUploadedOn;




        @SerializedName("DuringCamp_Image")
        @Expose
        private String duringCampImages;
        @SerializedName("DuringCamp_Image_UploadedOn")
        @Expose
        private String campImagesUploadedOn;


        @SerializedName("IsInImageApproved")
        @Expose
        private String isInImageApproved;



        @SerializedName("IsOutImageApproved")
        @Expose
        private String isOutImageApproved;


        public String getIsDuringImageApproved() {
            return isDuringImageApproved;
        }

        public void setIsDuringImageApproved(String isDuringImageApproved) {
            this.isDuringImageApproved = isDuringImageApproved;
        }

        public String getIsOutImageApproved() {
            return isOutImageApproved;
        }

        public void setIsOutImageApproved(String isOutImageApproved) {
            this.isOutImageApproved = isOutImageApproved;
        }

        public String getIsInImageApproved() {
            return isInImageApproved;
        }

        public void setIsInImageApproved(String isInImageApproved) {
            this.isInImageApproved = isInImageApproved;
        }

        @SerializedName("IsDuringImageApproved")
        @Expose
        private String isDuringImageApproved;


        public String getDuringCampImages() {
            return duringCampImages;
        }

        public void setDuringCampImages(String duringCampImages) {
            this.duringCampImages = duringCampImages;
        }

        public String getCampImagesUploadedOn() {
            return campImagesUploadedOn;
        }

        public void setCampImagesUploadedOn(String campImagesUploadedOn) {
            this.campImagesUploadedOn = campImagesUploadedOn;
        }

        public Integer getCampId() {
            return campId;
        }

        public void setCampId(Integer campId) {
            this.campId = campId;
        }

        public Double getLattitude() {
            return lattitude;
        }

        public void setLattitude(Double lattitude) {
            this.lattitude = lattitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        public String getInImage() {
            return inImage;
        }

        public void setInImage(String inImage) {
            this.inImage = inImage;
        }

        public String getInImageUploadedOn() {
            return inImageUploadedOn;
        }

        public void setInImageUploadedOn(String inImageUploadedOn) {
            this.inImageUploadedOn = inImageUploadedOn;
        }

        public String getOutImage() {
            return outImage;
        }

        public void setOutImage(String outImage) {
            this.outImage = outImage;
        }

        public String getOutImageUploadedOn() {
            return outImageUploadedOn;
        }

        public void setOutImageUploadedOn(String outImageUploadedOn) {
            this.outImageUploadedOn = outImageUploadedOn;
        }

    }

}