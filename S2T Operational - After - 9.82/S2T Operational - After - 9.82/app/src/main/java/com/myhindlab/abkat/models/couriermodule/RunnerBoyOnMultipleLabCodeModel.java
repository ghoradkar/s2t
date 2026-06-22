package com.myhindlab.abkat.models.couriermodule;

import java.util.List;

public class RunnerBoyOnMultipleLabCodeModel {

    /**
     * status : Success
     * message : Runner Boy Details
     * output : [{"userid":9462,"RunnerBoyName":"AFRA BAGWAN ","MOBNO":"9373204077"},{"userid":3521,"RunnerBoyName":"AMIT SHANKAR PAWAR","MOBNO":"9158642472"},{"userid":4311,"RunnerBoyName":"NIKHIL ANANT THAKUR","MOBNO":"7498933293"},{"userid":5668,"RunnerBoyName":"NITAM  DATTATREY  GOPALE","MOBNO":"8149718848"},{"userid":9573,"RunnerBoyName":"pranali A Gawli","MOBNO":"7620059902"},{"userid":9528,"RunnerBoyName":"Prashant Targe ","MOBNO":"8655581490"},{"userid":4157,"RunnerBoyName":"RAMESH VITTAL KOKARE","MOBNO":"8624929555"},{"userid":5297,"RunnerBoyName":"RUSHIKESH KISHOR MATE","MOBNO":"8983618889"},{"userid":4009,"RunnerBoyName":"SAMBHAJI M PANCHAL","MOBNO":"7066738106"},{"userid":9465,"RunnerBoyName":"SANGEETA KISHANRAO WANKHEDE","MOBNO":"8788301028"},{"userid":5485,"RunnerBoyName":"TEJAS SHRIPAD MUKADAM","MOBNO":"9930687830"}]
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
         * userid : 9462
         * RunnerBoyName : AFRA BAGWAN
         * MOBNO : 9373204077
         */

        private String userid;
        private String RunnerBoyName;
        private String MOBNO;

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getRunnerBoyName() {
            return RunnerBoyName;
        }

        public void setRunnerBoyName(String RunnerBoyName) {
            this.RunnerBoyName = RunnerBoyName;
        }

        public String getMOBNO() {
            return MOBNO;
        }

        public void setMOBNO(String MOBNO) {
            this.MOBNO = MOBNO;
        }
    }
}
