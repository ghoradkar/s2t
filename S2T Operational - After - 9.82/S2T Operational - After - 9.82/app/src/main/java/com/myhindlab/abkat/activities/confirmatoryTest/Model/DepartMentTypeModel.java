package com.myhindlab.abkat.activities.confirmatoryTest.Model;


import java.util.List;

import javax.annotation.processing.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class DepartMentTypeModel {

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


    @Generated("jsonschema2pojo")
    public class Output {

        @SerializedName("DeptTypeId")
        @Expose
        private Integer deptTypeId;
        @SerializedName("DepartmentType")
        @Expose
        private String departmentType;

        public Integer getDeptTypeId() {
            return deptTypeId;
        }

        public void setDeptTypeId(Integer deptTypeId) {
            this.deptTypeId = deptTypeId;
        }

        public String getDepartmentType() {
            return departmentType;
        }

        public void setDepartmentType(String departmentType) {
            this.departmentType = departmentType;
        }

    }
}