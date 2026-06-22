package com.myhindlab.abkat.pojos.doortodoor;

import java.util.ArrayList;

public class RelationPojo {
    private ArrayList<Output> output;

    private String message;

    private String status;

    public ArrayList<Output> getOutput() {
        return output;
    }

    public void setOutput(ArrayList<Output> output) {
        this.output = output;
    }

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [output = "+output+", message = "+message+", status = "+status+"]";
    }

    public static class Output
    {
        private String RelId;

        private String RelName;

        private String RelMName;

        public String getRelId ()
        {
            return RelId;
        }

        public void setRelId (String RelId)
        {
            this.RelId = RelId;
        }

        public String getRelName ()
        {
            return RelName;
        }

        public void setRelName (String RelName)
        {
            this.RelName = RelName;
        }

        public String getRelMName ()
        {
            return RelMName;
        }

        public void setRelMName (String RelMName)
        {
            this.RelMName = RelMName;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [RelId = "+RelId+", RelName = "+RelName+", RelMName = "+RelMName+"]";
        }
    }
}
