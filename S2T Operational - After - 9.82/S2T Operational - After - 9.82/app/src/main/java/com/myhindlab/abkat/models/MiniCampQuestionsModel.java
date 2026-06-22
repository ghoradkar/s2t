package com.myhindlab.abkat.models;

import java.util.List;

public class MiniCampQuestionsModel {

    /**
     * status : Success
     * message : Questionnaire Details List
     * output : [{"QuestionType":1,"QHeaderId":1,"QHeaderMName":"à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":1,"QLevel1MName":"à¤¤à¥\u0081à¤®à¤šà¥\u008dà¤¯à¤¾ à¤\u203aà¤¾à¤¤à¥\u20acà¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":1,"QLevel2MName":"à¤¤à¥\u0081à¤®à¤šà¥\u008dà¤¯à¤¾ à¤\u203aà¤¾à¤¤à¥\u20acà¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":1,"QHeaderMName":"à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":2,"QLevel1MName":"à¤¤à¥\u0081à¤®à¤šà¥\u008dà¤¯à¤¾ à¤\u203aà¤¾à¤¤à¥\u20acà¤¤ à¤§à¤¡à¤§à¤¡ à¤¹à¥\u2039à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":2,"QLevel2MName":"à¤¤à¥\u0081à¤®à¤šà¥\u008dà¤¯à¤¾ à¤\u203aà¤¾à¤¤à¥\u20acà¤¤ à¤§à¤¡à¤§à¤¡ à¤¹à¥\u2039à¤¤à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":1,"QHeaderMName":"à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":3,"QLevel1MName":"à¤¤à¥\u0081à¤®à¤šà¥\u2021 à¤¡à¥\u2039à¤\u2022 à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":3,"QLevel2MName":"à¤¤à¥\u0081à¤®à¤šà¥\u2021 à¤¡à¥\u2039à¤\u2022 à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":1,"QHeaderMName":"à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":4,"QLevel1MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤˜à¤¾à¤® à¤¯à¥\u2021à¤¤à¥\u2039 à¤\u2022à¤¾?","QLevel2Id":4,"QLevel2MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤˜à¤¾à¤® à¤¯à¥\u2021à¤¤à¥\u2039 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":1,"QHeaderMName":"à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":5,"QLevel1MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤²à¤¹à¤¾à¤¨à¤ªà¤£à¥\u20ac à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u009dà¤¾à¤²à¤¾ à¤\u2022à¤¾?","QLevel2Id":5,"QLevel2MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤²à¤¹à¤¾à¤¨à¤ªà¤£à¥\u20ac à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u009dà¤¾à¤²à¤¾ à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":2,"QHeaderMName":"à¤¶à¥\u008dà¤µà¤¸à¤¨à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac","QLevel1Id":6,"QLevel1MName":"à¤¶à¥\u008dà¤µà¤¾à¤¸ à¤˜à¥\u2021à¤£à¥\u008dà¤¯à¤¾à¤¸à¤¾à¤ à¥\u20ac à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤¹à¥\u2039à¤¤à¥\u2039 à¤\u2022à¤¾?","QLevel2Id":6,"QLevel2MName":"à¤¶à¥\u008dà¤µà¤¾à¤¸ à¤˜à¥\u2021à¤£à¥\u008dà¤¯à¤¾à¤¸à¤¾à¤ à¥\u20ac à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤¹à¥\u2039à¤¤à¥\u2039 à¤\u2022à¤¾?"},{"QuestionType":2,"QHeaderId":2,"QHeaderMName":"à¤¶à¥\u008dà¤µà¤¸à¤¨à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac","QLevel1Id":7,"QLevel1MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤¦à¤® à¤²à¤¾à¤\u2014à¤¤à¥\u2039 à¤\u2022à¤¾?","QLevel2Id":7,"QLevel2MName":"à¤\u203aà¥\u2039à¤Ÿà¥\u20ac à¤Ÿà¥\u2021à¤\u2022à¤¡à¥\u20ac à¤šà¤¾à¤²à¤²à¥\u008dà¤¯à¤¾à¤µà¤°"},{"QuestionType":2,"QHeaderId":2,"QHeaderMName":"à¤¶à¥\u008dà¤µà¤¸à¤¨à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac","QLevel1Id":7,"QLevel1MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤¦à¤® à¤²à¤¾à¤\u2014à¤¤à¥\u2039 à¤\u2022à¤¾?","QLevel2Id":8,"QLevel2MName":"2-3 à¤œà¤¿à¤¨à¤¾ à¤šà¥\u009dà¤²à¥\u008dà¤¯à¤¾à¤µà¤°"},{"QuestionType":2,"QHeaderId":2,"QHeaderMName":"à¤¶à¥\u008dà¤µà¤¸à¤¨à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac","QLevel1Id":7,"QLevel1MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤¦à¤® à¤²à¤¾à¤\u2014à¤¤à¥\u2039 à¤\u2022à¤¾?","QLevel2Id":9,"QLevel2MName":"1 à¤œà¤¿à¤¨à¤¾ à¤šà¤¢à¤²à¥\u008dà¤¯à¤¾à¤µà¤°"},{"QuestionType":2,"QHeaderId":2,"QHeaderMName":"à¤¶à¥\u008dà¤µà¤¸à¤¨à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac","QLevel1Id":7,"QLevel1MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤¦à¤® à¤²à¤¾à¤\u2014à¤¤à¥\u2039 à¤\u2022à¤¾?","QLevel2Id":10,"QLevel2MName":"à¤¬à¤¸à¥\u201aà¤¨ à¤¬à¤¸à¥\u201aà¤¨ à¤¦à¤® à¤²à¤¾à¤\u2014à¤£à¥\u2021"},{"QuestionType":1,"QHeaderId":2,"QHeaderMName":"à¤¶à¥\u008dà¤µà¤¸à¤¨à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac","QLevel1Id":20,"QLevel1MName":"à¤¶à¥\u008dà¤µà¤¾à¤¸ à¤˜à¥\u2021à¤¤à¤¾à¤¨à¤¾ à¤˜à¤°à¤˜à¤° à¤\u2020à¤µà¤¾à¤œ à¤¯à¥\u2021à¤¤à¥\u2039 à¤\u2022à¤¾?","QLevel2Id":23,"QLevel2MName":"à¤¶à¥\u008dà¤µà¤¾à¤¸ à¤˜à¥\u2021à¤¤à¤¾à¤¨à¤¾ à¤˜à¤°à¤˜à¤° à¤\u2020à¤µà¤¾à¤œ à¤¯à¥\u2021à¤¤à¥\u2039 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":2,"QHeaderMName":"à¤¶à¥\u008dà¤µà¤¸à¤¨à¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac","QLevel1Id":21,"QLevel1MName":" à¤¸à¤°à¥\u008dà¤¦à¥\u20ac à¤\u2013à¥\u2039à¤\u2022à¤²à¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":24,"QLevel2MName":"à¤¸à¤°à¥\u008dà¤¦à¥\u20ac à¤\u2013à¥\u2039à¤\u2022à¤²à¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":3,"QHeaderMName":"à¤ªà¥\u2039à¤Ÿà¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":8,"QLevel1MName":"à¤ªà¥\u2039à¤Ÿà¤¾à¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":11,"QLevel2MName":"à¤ªà¥\u2039à¤Ÿà¤¾à¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":3,"QHeaderMName":"à¤ªà¥\u2039à¤Ÿà¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":9,"QLevel1MName":"à¤ªà¥\u2039à¤Ÿà¤¾à¤µà¤° à¤\u2014à¤¾à¤ , à¤¸à¥\u201aà¤œ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":12,"QLevel2MName":"à¤ªà¥\u2039à¤Ÿà¤¾à¤µà¤° à¤\u2014à¤¾à¤ , à¤¸à¥\u201aà¤œ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":3,"QHeaderMName":"à¤ªà¥\u2039à¤Ÿà¤¾à¤šà¥\u20ac à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20acà¤¸à¤¾à¤ à¥\u20ac","QLevel1Id":10,"QLevel1MName":"à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤œà¥\u0081à¤²à¤¾à¤¬ à¤µ à¤®à¥\u201aà¤³à¤µà¥\u008dà¤¯à¤¾à¤§à¤šà¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":13,"QLevel2MName":"à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤œà¥\u0081à¤²à¤¾à¤¬ à¤µ à¤®à¥\u201aà¤³à¤µà¥\u008dà¤¯à¤¾à¤§à¤šà¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":4,"QHeaderMName":"à¤\u2022à¥\u2021à¤\u201aà¤¦à¥\u008dà¤°à¥\u20acà¤¯ à¤®à¤œà¥\u008dà¤œà¤¾à¤¸à¤\u201aà¤¸à¥\u008dà¤¥à¤¾ à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¤¿à¤¤","QLevel1Id":11,"QLevel1MName":"à¤\u2026à¤\u201aà¤\u2014à¤¾à¤µà¤° à¤\u2013à¤¾à¤œ, à¤\u2013à¥\u0081à¤œà¤²à¥\u20ac , à¤¨à¤¾à¤¯à¤Ÿà¤¾ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":14,"QLevel2MName":"à¤\u2026à¤\u201aà¤\u2014à¤¾à¤µà¤° à¤\u2013à¤¾à¤œ, à¤\u2013à¥\u0081à¤œà¤²à¥\u20ac , à¤¨à¤¾à¤¯à¤Ÿà¤¾ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":4,"QHeaderMName":"à¤\u2022à¥\u2021à¤\u201aà¤¦à¥\u008dà¤°à¥\u20acà¤¯ à¤®à¤œà¥\u008dà¤œà¤¾à¤¸à¤\u201aà¤¸à¥\u008dà¤¥à¤¾ à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¤¿à¤¤","QLevel1Id":12,"QLevel1MName":"à¤\u2026â\u20ac\u008dà¥\u2026à¤²à¤°à¥\u008dà¤œà¥\u20ac à¤šà¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":15,"QLevel2MName":"à¤\u2026â\u20ac\u008dà¥\u2026à¤²à¤°à¥\u008dà¤œà¥\u20ac à¤šà¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":4,"QHeaderMName":"à¤\u2022à¥\u2021à¤\u201aà¤¦à¥\u008dà¤°à¥\u20acà¤¯ à¤®à¤œà¥\u008dà¤œà¤¾à¤¸à¤\u201aà¤¸à¥\u008dà¤¥à¤¾ à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¤¿à¤¤","QLevel1Id":13,"QLevel1MName":"à¤\u2026à¤\u201aà¤\u2014à¤¾à¤µà¤° à¤¡à¤¾à¤\u2014, à¤šà¤Ÿà¥\u008dà¤Ÿà¤¾ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":16,"QLevel2MName":"à¤\u2026à¤\u201aà¤\u2014à¤¾à¤µà¤° à¤¡à¤¾à¤\u2014, à¤šà¤Ÿà¥\u008dà¤Ÿà¤¾ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":5,"QHeaderMName":"à¤¸à¥\u008dà¤¤à¥\u008dà¤°à¥\u20acà¤°à¥\u2039à¤\u2014 à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¥\u20ac","QLevel1Id":14,"QLevel1MName":"à¤\u2026à¤\u201aà¤\u2014à¤¾à¤µà¤°à¥\u201aà¤¨ à¤ªà¤¾à¤\u201aà¤¢à¤°à¥\u2021 à¤œà¤¾à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":17,"QLevel2MName":"à¤¤à¥\u0081à¤®à¤šà¥\u008dà¤¯à¤¾ à¤\u203aà¤¾à¤¤à¥\u20acà¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":5,"QHeaderMName":"à¤¸à¥\u008dà¤¤à¥\u008dà¤°à¥\u20acà¤°à¥\u2039à¤\u2014 à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¥\u20ac","QLevel1Id":15,"QLevel1MName":"à¤²à¤˜à¤µà¥\u20acà¤²à¤¾ à¤œà¤³à¤œà¤³ à¤¹à¥\u2039à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":18,"QLevel2MName":"à¤¤à¥\u0081à¤®à¤šà¥\u008dà¤¯à¤¾ à¤\u203aà¤¾à¤¤à¥\u20acà¤¤ à¤§à¤¡à¤§à¤¡ à¤¹à¥\u2039à¤¤à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":5,"QHeaderMName":"à¤¸à¥\u008dà¤¤à¥\u008dà¤°à¥\u20acà¤°à¥\u2039à¤\u2014 à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¥\u20ac","QLevel1Id":16,"QLevel1MName":"à¤\u2026à¤\u201aà¤\u2014à¤¾à¤µà¤°à¥\u201aà¤¨ à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤°à¤\u2022à¥\u008dà¤¤ à¤œà¤¾à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":19,"QLevel2MName":"à¤¤à¥\u0081à¤®à¤šà¥\u2021 à¤¡à¥\u2039à¤\u2022 à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":5,"QHeaderMName":"à¤¸à¥\u008dà¤¤à¥\u008dà¤°à¥\u20acà¤°à¥\u2039à¤\u2014 à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¥\u20ac","QLevel1Id":17,"QLevel1MName":"à¤ªà¤¾à¤³à¥\u20ac à¤¨à¤¿à¤¯à¤®à¤¿à¤¤ à¤¯à¥\u2021à¤¤à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":20,"QLevel2MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤œà¤¾à¤¸à¥\u008dà¤¤ à¤˜à¤¾à¤® à¤¯à¥\u2021à¤¤à¥\u2039 à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":5,"QHeaderMName":"à¤¸à¥\u008dà¤¤à¥\u008dà¤°à¥\u20acà¤°à¥\u2039à¤\u2014 à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¥\u20ac","QLevel1Id":18,"QLevel1MName":"à¤ªà¤¿à¤¶à¤µà¥\u20acà¤šà¤¾ à¤\u2022à¤¾à¤¹à¥\u20ac à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u2020à¤¹à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":21,"QLevel2MName":"à¤¤à¥\u0081à¤®à¥\u008dà¤¹à¤¾à¤²à¤¾ à¤²à¤¹à¤¾à¤¨à¤ªà¤£à¥\u20ac à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¤¾ à¤¤à¥\u008dà¤°à¤¾à¤¸ à¤\u009dà¤¾à¤²à¤¾ à¤\u2022à¤¾?"},{"QuestionType":1,"QHeaderId":5,"QHeaderMName":"à¤¸à¥\u008dà¤¤à¥\u008dà¤°à¥\u20acà¤°à¥\u2039à¤\u2014 à¤¤à¤ªà¤¾à¤¸à¤£à¥\u20ac à¤¸à¤\u201aà¤¬à¤\u201aà¤§à¥\u20ac","QLevel1Id":19,"QLevel1MName":"à¤®à¥\u0081à¤² à¤¬à¤\u201aà¤¦ à¤\u2018à¤ªà¤°à¥\u2021à¤¶à¤¨ à¤\u009dà¤¾à¤²à¥\u2021 à¤\u2022à¤¾?","QLevel2Id":22,"QLevel2MName":"à¤¤à¥\u0081à¤®à¤šà¥\u008dà¤¯à¤¾ à¤\u203aà¤¾à¤¤à¥\u20acà¤¤ à¤¦à¥\u0081à¤\u2013à¤¤à¥\u2021 à¤\u2022à¤¾?"}]
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
         * QuestionType : 1
         * QHeaderId : 1
         * QHeaderMName : à¤¹à¥ƒà¤¦à¤¯à¤¾à¤šà¥€ à¤¤à¤ªà¤¾à¤¸à¤£à¥€à¤¸à¤¾à¤ à¥€
         * QLevel1Id : 1
         * QLevel1MName : à¤¤à¥à¤®à¤šà¥à¤¯à¤¾ à¤›à¤¾à¤¤à¥€à¤¤ à¤¦à¥à¤–à¤¤à¥‡ à¤•à¤¾?
         * QLevel2Id : 1
         * QLevel2MName : à¤¤à¥à¤®à¤šà¥à¤¯à¤¾ à¤›à¤¾à¤¤à¥€à¤¤ à¤¦à¥à¤–à¤¤à¥‡ à¤•à¤¾?
         */

        private String QuestionType;
        private String QHeaderId;
        private String QHeaderMName;
        private String QLevel1Id;
        private String QLevel1MName;
        private String QLevel2Id;
        private String QLevel2MName;
        private String isYesNo = "";

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

        public String getIsYesNo() {
            return isYesNo;
        }

        public void setIsYesNo(String isYesNo) {
            this.isYesNo = isYesNo;
        }
    }
}
