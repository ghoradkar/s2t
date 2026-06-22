package com.myhindlab.abkat.activities.payout;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
public class PendingProcessingCountModel {

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

        @SerializedName("TotalMonthsBeneficiaryCount")
        @Expose
        private Integer totalMonthsBeneficiaryCount;
        @SerializedName("TodaysBeneficiaryCount")
        @Expose
        private Integer todaysBeneficiaryCount;
        @SerializedName("HomeLabProcessedCount")
        @Expose
        private Integer homeLabProcessedCount;
        @SerializedName("HomeLabProcessedPendingCount")
        @Expose
        private Integer homeLabProcessedPendingCount;
        @SerializedName("HubLabProcessedCount")
        @Expose
        private Integer hubLabProcessedCount;
        @SerializedName("HubLabProcessedPendingCount")
        @Expose
        private Integer hubLabProcessedPendingCount;

        public Integer getTotalMonthsBeneficiaryCount() {
            return totalMonthsBeneficiaryCount;
        }

        public void setTotalMonthsBeneficiaryCount(Integer totalMonthsBeneficiaryCount) {
            this.totalMonthsBeneficiaryCount = totalMonthsBeneficiaryCount;
        }

        public Integer getTodaysBeneficiaryCount() {
            return todaysBeneficiaryCount;
        }

        public void setTodaysBeneficiaryCount(Integer todaysBeneficiaryCount) {
            this.todaysBeneficiaryCount = todaysBeneficiaryCount;
        }

        public Integer getHomeLabProcessedCount() {
            return homeLabProcessedCount;
        }

        public void setHomeLabProcessedCount(Integer homeLabProcessedCount) {
            this.homeLabProcessedCount = homeLabProcessedCount;
        }

        public Integer getHomeLabProcessedPendingCount() {
            return homeLabProcessedPendingCount;
        }

        public void setHomeLabProcessedPendingCount(Integer homeLabProcessedPendingCount) {
            this.homeLabProcessedPendingCount = homeLabProcessedPendingCount;
        }

        public Integer getHubLabProcessedCount() {
            return hubLabProcessedCount;
        }

        public void setHubLabProcessedCount(Integer hubLabProcessedCount) {
            this.hubLabProcessedCount = hubLabProcessedCount;
        }

        public Integer getHubLabProcessedPendingCount() {
            return hubLabProcessedPendingCount;
        }

        public void setHubLabProcessedPendingCount(Integer hubLabProcessedPendingCount) {
            this.hubLabProcessedPendingCount = hubLabProcessedPendingCount;
        }

    }

}