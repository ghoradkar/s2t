package com.myhindlab.abkat.pojos.doortodoor;

import java.util.ArrayList;

public class DependentPojo {
    private ArrayList<Output> output;

    private String message;

    private String status;

    public ArrayList<Output> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<Output> output) {
        this.output = output;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ClassPojo [output = " + output + ", message = " + message + ", status = " + status + "]";
    }

    public static class Output {
        private String Releation;

        private String ShareOfNominee;

        private String DependentName;

        private String ISNominee;

        private String MariatalStatus;

        private String AddressOfNominee;

        private String Education;

        private String BenificiaryNo;

        private String Sex;

        private String Age;

        private String DependentID;
        private String RelId;

        public String getRelId() {
            return RelId;
        }

        public void setRelId(String relId) {
            RelId = relId;
        }

        public String getReleation ()
        {
            return Releation;
        }

        public void setReleation (String Releation)
        {
            this.Releation = Releation;
        }

        public String getShareOfNominee ()
        {
            return ShareOfNominee;
        }

        public void setShareOfNominee (String ShareOfNominee)
        {
            this.ShareOfNominee = ShareOfNominee;
        }

        public String getDependentName ()
        {
            return DependentName;
        }

        public void setDependentName (String DependentName)
        {
            this.DependentName = DependentName;
        }

        public String getISNominee ()
        {
            return ISNominee;
        }

        public void setISNominee (String ISNominee)
        {
            this.ISNominee = ISNominee;
        }

        public String getMariatalStatus ()
        {
            return MariatalStatus;
        }

        public void setMariatalStatus (String MariatalStatus)
        {
            this.MariatalStatus = MariatalStatus;
        }

        public String getAddressOfNominee ()
        {
            return AddressOfNominee;
        }

        public void setAddressOfNominee (String AddressOfNominee)
        {
            this.AddressOfNominee = AddressOfNominee;
        }

        public String getEducation ()
        {
            return Education;
        }

        public void setEducation (String Education)
        {
            this.Education = Education;
        }

        public String getBenificiaryNo ()
        {
            return BenificiaryNo;
        }

        public void setBenificiaryNo (String BenificiaryNo)
        {
            this.BenificiaryNo = BenificiaryNo;
        }

        public String getSex ()
        {
            return Sex;
        }

        public void setSex (String Sex)
        {
            this.Sex = Sex;
        }

        public String getAge ()
        {
            return Age;
        }

        public void setAge (String Age)
        {
            this.Age = Age;
        }

        public String getDependentID ()
        {
            return DependentID;
        }

        public void setDependentID (String DependentID)
        {
            this.DependentID = DependentID;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [Releation = "+Releation+", ShareOfNominee = "+ShareOfNominee+", DependentName = "+DependentName+", ISNominee = "+ISNominee+", MariatalStatus = "+MariatalStatus+", AddressOfNominee = "+AddressOfNominee+", Education = "+Education+", BenificiaryNo = "+BenificiaryNo+", Sex = "+Sex+", Age = "+Age+", DependentID = "+DependentID+"]";
        }
    }

}
