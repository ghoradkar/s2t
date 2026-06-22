package com.myhindlab.abkat.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;




import java.util.List;

public class TalukaListForFilterModel {

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

        @SerializedName("TALLGDCODE")
        @Expose
        private Integer tallgdcode;
        @SerializedName("TALNAME")
        @Expose
        private String talname;

        private boolean isChecked;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public Integer getTallgdcode() {
            return tallgdcode;
        }

        public void setTallgdcode(Integer tallgdcode) {
            this.tallgdcode = tallgdcode;
        }

        public String getTalname() {
            return talname;
        }

        public void setTalname(String talname) {
            this.talname = talname;
        }

    }

}