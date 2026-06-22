package com.myhindlab.abkat.activities.re_registration.model;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



import java.util.List;


public class RemarkForReReGistrationModel {

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

        @SerializedName("ArId")
        @Expose
        private Integer arId;
        @SerializedName("AssignmentRemarks")
        @Expose
        private String assignmentRemarks;

        public Integer getArId() {
            return arId;
        }

        public void setArId(Integer arId) {
            this.arId = arId;
        }

        public String getAssignmentRemarks() {
            return assignmentRemarks;
        }

        public void setAssignmentRemarks(String assignmentRemarks) {
            this.assignmentRemarks = assignmentRemarks;
        }

    }
}