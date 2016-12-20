package com.mo9.risk.modules.dunning.entity;

import java.text.MessageFormat;

/**
 *  短信催收模版
 * Created by sun on 2016/7/22.
 */
public enum DunningSmsTemplate {
	ST_0("短信  未逾期"),
    ST__1_7("短信 1-7"),
    ST_8_14("短信 8-14"),
    ST_15_21("短信 15-21"),
    ST_22_35("短信 22-35"),
    ST_15_PLUS("短信 15+");

    private String templateName; //短信模版名称
    DunningSmsTemplate(String name)
    {
            this.templateName = name;
    }

    public String getContent(String... args)
    {
        String source = null;
        switch (this)
        {
        	case ST_0:
        		source =  "【mo9】江湖救急用户，您的借款已到还款期，应还金额{0}元。为避免影响您的个人信用，请收到短信后立刻前往江湖救急页面完成还款或申请续期。详询: 02180163503";
        		break;
            case ST__1_7:
                source =  "【mo9】江湖救急用户，您的借款已过还款期，应还金额{0}元。为避免影响您的个人信用，请收到短信后立刻前往江湖救急页面完成还款或申请续期。详询: 02180163503";
                break;
            case ST_8_14:
                source = "【mo9】江湖救急用户，您的借款已过还款期，应还金额{0}元。请于2天之内完成还款或申请续期。否则我司将按《征信业管理条例》如实上报金融信用信息基础数据库，" +
                        "如对个人征信造成影响请自行负责。详询: 02180163503";
                break;
            case ST_15_21:
                source = "【mo9】您好: {0}，身份证尾号{1}。您申请的江湖救急借款已经拖欠{2}天，逾期金额{3}元，相关法律函件近期将邮寄至您的户籍地址，请注意查收。" +
                        "详询请回电02180163503。请确保按函件要求还款，否则我们将考虑详您户籍地村委会/居委会以及工作单位通报情况，协助调查。";
                break;
            case ST_22_35:
                source = "【mo9】您好: {0},身份证尾号{1}。您长期拖欠江湖救急借款，欠款金额达{2}元。已经涉嫌触犯《刑法》224条和193条，" +
                        "以非法占有为目的骗取对方财物，涉嫌合同诈骗罪和借款诈骗罪。相关法律函件近期将邮寄至您户籍所在地。请注意查收。" +
                        "请确保按函件要求还款，否则我司将向公安机关报案，依法追究您的法律责任。详询02180163503";
                break;
            case ST_15_PLUS:
                source = "【mo9】您好，您的亲属/朋友：{0}, 向江湖救急的借款已经严重逾期，截至今日逾期{1}天，欠款金额{2}元。" +
                        "我司多次电话和短信通知其支付此款项，至今仍未还款。鉴于{3}已严重违反借款合同约定及相关法律的规定，" +
                        "为避免催收给您的工作和生活带来不便，请您务必通知其于2天之内偿还上述欠款。否则我司将通过新闻媒体公告催收、" +
                        "委托相关公司进行催收或向法院提起诉讼，依法追究刑事责任。详询请其至电02180163503，感谢您的配合";
                break;
        }
        if(source == null)
        {
            return null;
        }
        return MessageFormat.format(source,args);
    }

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
    
    

}

