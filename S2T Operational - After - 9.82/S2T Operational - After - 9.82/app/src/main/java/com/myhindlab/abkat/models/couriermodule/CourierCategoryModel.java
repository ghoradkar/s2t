package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class CourierCategoryModel {


    /**
     * status : Success
     * message : Courier Category Details
     * output : [{"CategoryId":1,"CategoryName":"Samples","IsSampleType":1},{"CategoryId":2,"CategoryName":"Consumables","IsSampleType":0},{"CategoryId":3,"CategoryName":"Reagent","IsSampleType":0},{"CategoryId":4,"CategoryName":"EQUAS Samples","IsSampleType":1}]
     */

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
        /**
         * CategoryId : 1
         * CategoryName : Samples
         * IsSampleType : 1
         */

        private String CategoryId;
        private String CategoryName;
        private String IsSampleType;

        public String getCategoryId() {
            return CategoryId;
        }

        public void setCategoryId(String CategoryId) {
            this.CategoryId = CategoryId;
        }

        public String getCategoryName() {
            return CategoryName;
        }

        public void setCategoryName(String CategoryName) {
            this.CategoryName = CategoryName;
        }

        public String getIsSampleType() {
            return IsSampleType;
        }

        public void setIsSampleType(String IsSampleType) {
            this.IsSampleType = IsSampleType;
        }
    }
}
