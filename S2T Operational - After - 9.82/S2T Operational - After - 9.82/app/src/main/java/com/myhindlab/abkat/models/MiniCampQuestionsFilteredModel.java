package com.myhindlab.abkat.models;

import java.util.List;

public class MiniCampQuestionsFilteredModel {

    private String QHeaderId;
    private String QHeaderMName;
    private List<OutputBean> output;

    public String getQHeaderId() {
        return QHeaderId;
    }

    public void setQHeaderId(String QHeaderId) {
        this.QHeaderId = QHeaderId;
    }

    public String getQHeaderMName() {
        return QHeaderMName;
    }

    public void setQHeaderMName(String QHeaderMName) {
        this.QHeaderMName = QHeaderMName;
    }

    public List<OutputBean> getOutput() {
        return output;
    }

    public void setOutput(List<OutputBean> output) {
        this.output = output;
    }

    public static class OutputBean {

        private String QuestionType;
        private String QHeaderId;
        private String QHeaderMName;
        private String QLevel1Id;
        private String QLevel1MName;
        private String QLevel2Id;
        private String QLevel2MName;

        public String getQuestionType() {
            return QuestionType;
        }

        public void setQuestionType(String QuestionType) {
            this.QuestionType = QuestionType;
        }

        public String getQHeaderId() {
            return QHeaderId;
        }

        public void setQHeaderId(String QHeaderId) {
            this.QHeaderId = QHeaderId;
        }

        public String getQHeaderMName() {
            return QHeaderMName;
        }

        public void setQHeaderMName(String QHeaderMName) {
            this.QHeaderMName = QHeaderMName;
        }

        public String getQLevel1Id() {
            return QLevel1Id;
        }

        public void setQLevel1Id(String QLevel1Id) {
            this.QLevel1Id = QLevel1Id;
        }

        public String getQLevel1MName() {
            return QLevel1MName;
        }

        public void setQLevel1MName(String QLevel1MName) {
            this.QLevel1MName = QLevel1MName;
        }

        public String getQLevel2Id() {
            return QLevel2Id;
        }

        public void setQLevel2Id(String QLevel2Id) {
            this.QLevel2Id = QLevel2Id;
        }

        public String getQLevel2MName() {
            return QLevel2MName;
        }

        public void setQLevel2MName(String QLevel2MName) {
            this.QLevel2MName = QLevel2MName;
        }
    }
}
