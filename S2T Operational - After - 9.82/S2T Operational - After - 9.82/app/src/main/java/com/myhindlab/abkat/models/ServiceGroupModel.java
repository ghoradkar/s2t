package com.myhindlab.abkat.models;

import java.util.List;

public class ServiceGroupModel {
    
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

    public static class OutputBean {
        
        private String GroupID;
        private String GroupName;
        private boolean isCheckedInListOne;
        private boolean isCheckedInListTwo;
        private boolean isCheckedInListThree;


        public boolean isCheckedInListThree() {
            return isCheckedInListThree;
        }

        public void setCheckedInListThree(boolean checkedInListThree) {
            isCheckedInListThree = checkedInListThree;
        }

        public String getGroupID() {
            return GroupID;
        }

        public void setGroupID(String GroupID) {
            this.GroupID = GroupID;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public boolean isCheckedInListOne() {
            return isCheckedInListOne;
        }

        public void setCheckedInListOne(boolean checkedInListOne) {
            isCheckedInListOne = checkedInListOne;
        }

        public boolean isCheckedInListTwo() {
            return isCheckedInListTwo;
        }

        public void setCheckedInListTwo(boolean checkedInListTwo) {
            isCheckedInListTwo = checkedInListTwo;
        }
    }
}
