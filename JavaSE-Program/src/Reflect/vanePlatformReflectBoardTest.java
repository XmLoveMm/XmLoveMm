package Reflect;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class vanePlatformReflectBoardTest {

    private final static String reg = "^(-)?[1-9]\\d*\\.\\d*|(-)?0\\.\\d*[1-9]\\d*$";
    private final static Pattern pattern = Pattern.compile(reg);
    private final static String[] radarZb = {//需要处理的指标
            "userEvaluateScore",
            "customServiceConsultScore",
            "logisticsLvyueScore",
            "afterServiceScore",
            "disputeScore",
            "fxgRankRate",
            "affectFactor"
    };

    public static void main(String args[]) {

        vanePlatformReflectBoardTest vanePlatformReflectBoardTest = new vanePlatformReflectBoardTest();

        List<domainTestBoard> list = new ArrayList<>();
        list.add(new domainTestBoard("99.83352", null, null, "96.53135", "01", "电脑", null, null, null, null, null, "8.65321", "9.54213", "差", "9.65123", "9.55312", "差", "9.73124", "7.5324", "差", "9.61252", "9.54312", "很差", "9.74231", "89", "-999", "-999", "9.5123", "8.6523", "-999", "8.2131", "0"));
        list.add(new domainTestBoard("96.53352", null, null, "96.53135", "02", "彩电", null, null, null, null, null, "8.76321", "9.84213", "好", "9.65123", "9.83312", "好", "9.73124", "7.5724", "很差", "9.61252", "9.64312", "差", "9.74231", "89", "-999", "-999", "4.5123", "5.6523", "-999", "7.2131", "-999"));

        /**反射处理数据*/
        for (domainTestBoard domainTestBoard : list) {
            vanePlatformReflectBoardTest.reflectObject(domainTestBoard);
            /**处理雷达图相关的指标 为 ”-999“ ？ ”0“ ： 指标值*/
            vanePlatformReflectBoardTest.handle999To0(radarZb, domainTestBoard);
        }
        /**处理业务*/
        vanePlatformReflectBoardTest.handleBusiness(list);

        /**【测试--展示数据】*/
        for (domainTestBoard domainTestBoard : list) {
            System.out.println(domainTestBoard.toString());
        }
        System.out.println("list(0): " + list.get(0).toString());
    }

    /**
     * 处理业务
     */
    public void handleBusiness(List<domainTestBoard> list) {
        domainTestBoard domainTestBoard = list.get(0);
        String hb = handleDataByTypeToSubString("double", null, Double.valueOf(handlCalculate("-", list.get(0).getScoreRankRate(), list.get(1).getScoreRankRate())));

        domainTestBoard.sethScoreRankRate(hb);//环比
        domainTestBoard.setMainItmScndCatgCdBy2(list.get(1).getMainItmScndCatgCdBy1());//T+2类目ID
        domainTestBoard.setMainItmScndCatgNmBy2(list.get(1).getMainItmScndCatgNmBy1());//T+2类目名称
        domainTestBoard.setIsDescend(Double.parseDouble(handlCalculate("-", list.get(0).getScoreRankRate(), list.get(1).getScoreRankRate())) < -5.00 ? "yes" : "no");//是否较昨日下降>5%（“yes”:下降超过5%；“”：不超过）
        /**通过反射获取"差"|"很差"*相关的属性*/
        String result = handleInfoChaOrTooChaToString(list.get(0));
        handleInfoChaOrToocha(domainTestBoard, result, result.split(";").length);
    }

    /**
     * 【工具】处理存放"差"|"很差"字符串
     */
    private void handleInfoChaOrToocha(domainTestBoard domainTestBoard, String chaOrTooChaStr, int length) {
        switch (length) {
            case 0:
                domainTestBoard.setIndexCha("");//"差"||"很差"都没有
                domainTestBoard.setIndexHencha("");
                break;
            case 1:
                domainTestBoard.setIndexCha(chaOrTooChaStr.split(";")[0]);//"差"的指标
                break;
            case 2:
                if (chaOrTooChaStr.split(";")[0].equals("")) {//"很差"的指标
                    domainTestBoard.setIndexHencha(chaOrTooChaStr.split(";")[1]);
                } else {
                    domainTestBoard.setIndexCha(chaOrTooChaStr.split(";")[0]);//"差"||"很"的指标都有
                    domainTestBoard.setIndexHencha(chaOrTooChaStr.split(";")[1]);
                }
                break;
        }
    }

    /**
     * 【工具】通过反射获取"差"|"很差"*相关的属性
     */
    private String handleInfoChaOrTooChaToString(Object Obj) {
        Field[] fields = Obj.getClass().getDeclaredFields();
        StringBuffer stringBufferCha = new StringBuffer();//存放"差"相关的属性
        StringBuffer stringBufferTooCha = new StringBuffer();//存放"很差"相关的属性

        try {
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                if (fields[i].get(Obj).equals("差")) {
                    stringBufferCha.append(zbDescribeEnum.valueOfName(fields[i].getName()) + ",");
                } else if (fields[i].get(Obj).equals("很差")) {
                    stringBufferTooCha.append(zbDescribeEnum.valueOfName(fields[i].getName()) + ",");
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return stringBufferCha + ";" + stringBufferTooCha;
    }

    /**
     * 【工具】通过反射处理属性
     */
    private void reflectObject(Object obj) {
        Field[] field = obj.getClass().getDeclaredFields();
        try {
            for (int i = 0; i < field.length; i++) {
                field[i].setAccessible(true);
                field[i].set(obj, handleNull(handleDataByTypeToSubString("string", field[i].get(obj) == null ? "null" : field[i].get(obj).toString(), null)));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 【工具】根据类型来截取数据
     *
     * @param dataType：数据类型
     * @param dataString:String
     * @param dataDouble:Double
     */
    private String handleDataByTypeToSubString(String dataType, String dataString, Double dataDouble) {
        String resultData = "";
        switch (dataType) {
            case "string":
                Matcher matcherString = pattern.matcher(dataString);
                if (matcherString.find()) {
                    resultData = dataString.split("\\.")[0] + "." + dataString.split("\\.")[1].substring(0, 2);
                } else {
                    resultData = dataString;
                }
                break;
            case "double":
                Matcher matcherDouble = pattern.matcher(String.valueOf(dataDouble));
                if (matcherDouble.find()) {
                    resultData = handleDataToString(dataDouble).split("\\.")[0] + "."
                            + (handleDataToString(dataDouble).split("\\.")[1].length() == 1 ? handleDataToString(dataDouble).split("\\.")[1].substring(0, 1) : handleDataToString(dataDouble).split("\\.")[1].substring(0, 2));
                }
                break;
        }
        return resultData;
    }

    /**
     * 【工具】将Object 转换为 String
     */
    private String handleDataToString(Object obj) {
        return String.valueOf(obj);
    }

    /**
     * 【工具】处理为"null"
     */
    private String handleNull(String str) {
        return str.equals("null") ? "" : str;
    }

    /**
     * 【工具】使用BigDecimal 对数据进行计算："+","-","*","/"
     */
    private String handlCalculate(String calculateType, String num01, String num02) {
        String resultData = "";
        BigDecimal b1 = new BigDecimal(num01);
        BigDecimal b2 = new BigDecimal(num02);

        switch (calculateType) {
            case "+":
                resultData = b1.add(b2).toString();
                break;
            case "-":
                resultData = b1.subtract(b2).toString();
                break;
            case "*":
                resultData = b1.multiply(b2).toString();
                break;
            case "/":
                resultData = b1.multiply(b2).toString();
                break;
        }
        return resultData;
    }

    /**
     * 处理雷达图相关的指标 为 ”-999“ ？ ”0“ ： 指标值
     */
    private void handle999To0(String[] zbs, Object obj) {
        try {
            for (int i = 0; i < zbs.length; i++) {
                Field field = obj.getClass().getDeclaredField(zbs[i]);
                field.setAccessible(true);
                field.set(obj, field.get(obj).equals("-999") ? "0" : field.get(obj).toString());
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private enum zbDescribeEnum {
        INFOSKUQUALITYSCORE("infoSkuQualityScore", "商品质量满意度"),
        INFOVENDEREXPRESSSCORE("infoVenderExpressScore", "物流速度满意度"),
        INFOVENDERSERVICEATTISCORE("infoVenderServiceAttiScore", "卖家服务满意度"),
        INFOSKUDESCSCORE("infoSkuDescScore", "商品描述满意度"),
        INFORESPONSERATE("infoResponseRate", "咚咚30s应答率"),
        INFORESPONSESPEED("infoResponseSpeed", "咚咚平均响应时长"),
        INFOTIMELYRATE("infoTimelyRate", "48小时揽件及时率"),
        INFOOTHERDAYRATE("infoOtherDayRate", "隔日达订单占比"),
        INFOSKUSATISFY("infoSkuSatisfy", "退换货满意度"),
        INFOCHECKPROCDURATION("infoCheckProcDuration", "售后处理时长"),
        INFOSKUREPAIR("infoSkuRepair", "退换货返修率"),
        INFOCANCELORDERDURATION("infoCancelOrderDuration", "取消订单时长"),
        INFOTRANSDISPUTERATE("infoTransDisputeRate", "交易纠纷率"),
        INFOTRANSDISPUTESELFDONE("infoTransDisputeSelfDone", "纠纷自主完成率"),
        INFOTRANSDISPUTEPROCESSONTIMERATE("infoTransDisputeProcessOntimeRate", "纠纷处理遵时率"),;
        private String zb;
        private String describe;

        zbDescribeEnum(String zb, String describe) {
            this.zb = zb;
            this.describe = describe;
        }

        public String getZb() {
            return this.zb;
        }

        public String getDescribe() {
            return this.describe;
        }

        /**
         * 通过属性名称获取，属性描述
         */
        public static String valueOfName(String name) {
            for (zbDescribeEnum zb : values()) {
                if (zb.getZb().equals(name)) {
                    return zb.getDescribe();
                }
            }

            return null;
        }
    }
}


class domainTestBoard {
    private String scoreRankRate;
    private String yesterdayScoreRankRate;
    private String hScoreRankRate;
    private String avgScoreRankRate;
    private String mainItmScndCatgCdBy1;
    private String mainItmScndCatgNmBy1;
    private String mainItmScndCatgCdBy2;
    private String mainItmScndCatgNmBy2;
    private String isDescend;
    private String indexCha;
    private String indexHencha;
    private String totalScore;
    private String skuQualityScore;
    private String infoSkuQualityScore;
    private String targetSkuQualityScore;
    private String venderExpressScore;
    private String infoVenderExpressScore;
    private String targetVenderExpressScore;
    private String venderServiceAttiScore;
    private String infoVenderServiceAttiScore;
    private String targetVenderServiceAttiScore;
    private String skuDescScore;
    private String infoSkuDescScore;
    private String targetSkuDescScore;
    private String openTm;

    private String userEvaluateScore;
    private String customServiceConsultScore;
    private String logisticsLvyueScore;
    private String afterServiceScore;
    private String disputeScore;
    private String fxgRankRate;
    private String affectFactor;

    @Override
    public String toString() {
        return "domainTestBoard{" +
                "scoreRankRate='" + scoreRankRate + '\'' +
                ", yesterdayScoreRankRate='" + yesterdayScoreRankRate + '\'' +
                ", hScoreRankRate='" + hScoreRankRate + '\'' +
                ", avgScoreRankRate='" + avgScoreRankRate + '\'' +
                ", mainItmScndCatgCdBy1='" + mainItmScndCatgCdBy1 + '\'' +
                ", mainItmScndCatgNmBy1='" + mainItmScndCatgNmBy1 + '\'' +
                ", mainItmScndCatgCdBy2='" + mainItmScndCatgCdBy2 + '\'' +
                ", mainItmScndCatgNmBy2='" + mainItmScndCatgNmBy2 + '\'' +
                ", isDescend='" + isDescend + '\'' +
                ", indexCha='" + indexCha + '\'' +
                ", indexHencha='" + indexHencha + '\'' +
                ", totalScore='" + totalScore + '\'' +
                ", skuQualityScore='" + skuQualityScore + '\'' +
                ", infoSkuQualityScore='" + infoSkuQualityScore + '\'' +
                ", targetSkuQualityScore='" + targetSkuQualityScore + '\'' +
                ", venderExpressScore='" + venderExpressScore + '\'' +
                ", infoVenderExpressScore='" + infoVenderExpressScore + '\'' +
                ", targetVenderExpressScore='" + targetVenderExpressScore + '\'' +
                ", venderServiceAttiScore='" + venderServiceAttiScore + '\'' +
                ", infoVenderServiceAttiScore='" + infoVenderServiceAttiScore + '\'' +
                ", targetVenderServiceAttiScore='" + targetVenderServiceAttiScore + '\'' +
                ", skuDescScore='" + skuDescScore + '\'' +
                ", infoSkuDescScore='" + infoSkuDescScore + '\'' +
                ", targetSkuDescScore='" + targetSkuDescScore + '\'' +
                ", openTm='" + openTm + '\'' +
                ", userEvaluateScore='" + userEvaluateScore + '\'' +
                ", customServiceConsultScore='" + customServiceConsultScore + '\'' +
                ", logisticsLvyueScore='" + logisticsLvyueScore + '\'' +
                ", afterServiceScore='" + afterServiceScore + '\'' +
                ", disputeScore='" + disputeScore + '\'' +
                ", fxgRankRate='" + fxgRankRate + '\'' +
                ", affectFactor='" + affectFactor + '\'' +
                '}';
    }

    public domainTestBoard() {
    }

    public domainTestBoard(String scoreRankRate, String yesterdayScoreRankRate, String hScoreRankRate, String avgScoreRankRate, String mainItmScndCatgCdBy1, String mainItmScndCatgNmBy1, String mainItmScndCatgCdBy2, String mainItmScndCatgNmBy2, String isDescend, String indexCha, String indexHencha, String totalScore, String skuQualityScore, String infoSkuQualityScore, String targetSkuQualityScore, String venderExpressScore, String infoVenderExpressScore, String targetVenderExpressScore, String venderServiceAttiScore, String infoVenderServiceAttiScore, String targetVenderServiceAttiScore, String skuDescScore, String infoSkuDescScore, String targetSkuDescScore, String openTm, String userEvaluateScore, String customServiceConsultScore, String logisticsLvyueScore, String afterServiceScore, String disputeScore, String fxgRankRate, String affectFactor) {
        this.scoreRankRate = scoreRankRate;
        this.yesterdayScoreRankRate = yesterdayScoreRankRate;
        this.hScoreRankRate = hScoreRankRate;
        this.avgScoreRankRate = avgScoreRankRate;
        this.mainItmScndCatgCdBy1 = mainItmScndCatgCdBy1;
        this.mainItmScndCatgNmBy1 = mainItmScndCatgNmBy1;
        this.mainItmScndCatgCdBy2 = mainItmScndCatgCdBy2;
        this.mainItmScndCatgNmBy2 = mainItmScndCatgNmBy2;
        this.isDescend = isDescend;
        this.indexCha = indexCha;
        this.indexHencha = indexHencha;
        this.totalScore = totalScore;
        this.skuQualityScore = skuQualityScore;
        this.infoSkuQualityScore = infoSkuQualityScore;
        this.targetSkuQualityScore = targetSkuQualityScore;
        this.venderExpressScore = venderExpressScore;
        this.infoVenderExpressScore = infoVenderExpressScore;
        this.targetVenderExpressScore = targetVenderExpressScore;
        this.venderServiceAttiScore = venderServiceAttiScore;
        this.infoVenderServiceAttiScore = infoVenderServiceAttiScore;
        this.targetVenderServiceAttiScore = targetVenderServiceAttiScore;
        this.skuDescScore = skuDescScore;
        this.infoSkuDescScore = infoSkuDescScore;
        this.targetSkuDescScore = targetSkuDescScore;
        this.openTm = openTm;
        this.userEvaluateScore = userEvaluateScore;
        this.customServiceConsultScore = customServiceConsultScore;
        this.logisticsLvyueScore = logisticsLvyueScore;
        this.afterServiceScore = afterServiceScore;
        this.disputeScore = disputeScore;
        this.fxgRankRate = fxgRankRate;
        this.affectFactor = affectFactor;
    }

    public String getScoreRankRate() {
        return scoreRankRate;
    }

    public void setScoreRankRate(String scoreRankRate) {
        this.scoreRankRate = scoreRankRate;
    }

    public String getYesterdayScoreRankRate() {
        return yesterdayScoreRankRate;
    }

    public void setYesterdayScoreRankRate(String yesterdayScoreRankRate) {
        this.yesterdayScoreRankRate = yesterdayScoreRankRate;
    }

    public String gethScoreRankRate() {
        return hScoreRankRate;
    }

    public void sethScoreRankRate(String hScoreRankRate) {
        this.hScoreRankRate = hScoreRankRate;
    }

    public String getAvgScoreRankRate() {
        return avgScoreRankRate;
    }

    public void setAvgScoreRankRate(String avgScoreRankRate) {
        this.avgScoreRankRate = avgScoreRankRate;
    }

    public String getMainItmScndCatgCdBy1() {
        return mainItmScndCatgCdBy1;
    }

    public void setMainItmScndCatgCdBy1(String mainItmScndCatgCdBy1) {
        this.mainItmScndCatgCdBy1 = mainItmScndCatgCdBy1;
    }

    public String getMainItmScndCatgNmBy1() {
        return mainItmScndCatgNmBy1;
    }

    public void setMainItmScndCatgNmBy1(String mainItmScndCatgNmBy1) {
        this.mainItmScndCatgNmBy1 = mainItmScndCatgNmBy1;
    }

    public String getMainItmScndCatgCdBy2() {
        return mainItmScndCatgCdBy2;
    }

    public void setMainItmScndCatgCdBy2(String mainItmScndCatgCdBy2) {
        this.mainItmScndCatgCdBy2 = mainItmScndCatgCdBy2;
    }

    public String getMainItmScndCatgNmBy2() {
        return mainItmScndCatgNmBy2;
    }

    public void setMainItmScndCatgNmBy2(String mainItmScndCatgNmBy2) {
        this.mainItmScndCatgNmBy2 = mainItmScndCatgNmBy2;
    }

    public String getIsDescend() {
        return isDescend;
    }

    public void setIsDescend(String isDescend) {
        this.isDescend = isDescend;
    }

    public String getIndexCha() {
        return indexCha;
    }

    public void setIndexCha(String indexCha) {
        this.indexCha = indexCha;
    }

    public String getIndexHencha() {
        return indexHencha;
    }

    public void setIndexHencha(String indexHencha) {
        this.indexHencha = indexHencha;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getSkuQualityScore() {
        return skuQualityScore;
    }

    public void setSkuQualityScore(String skuQualityScore) {
        this.skuQualityScore = skuQualityScore;
    }

    public String getInfoSkuQualityScore() {
        return infoSkuQualityScore;
    }

    public void setInfoSkuQualityScore(String infoSkuQualityScore) {
        this.infoSkuQualityScore = infoSkuQualityScore;
    }

    public String getTargetSkuQualityScore() {
        return targetSkuQualityScore;
    }

    public void setTargetSkuQualityScore(String targetSkuQualityScore) {
        this.targetSkuQualityScore = targetSkuQualityScore;
    }

    public String getVenderExpressScore() {
        return venderExpressScore;
    }

    public void setVenderExpressScore(String venderExpressScore) {
        this.venderExpressScore = venderExpressScore;
    }

    public String getInfoVenderExpressScore() {
        return infoVenderExpressScore;
    }

    public void setInfoVenderExpressScore(String infoVenderExpressScore) {
        this.infoVenderExpressScore = infoVenderExpressScore;
    }

    public String getTargetVenderExpressScore() {
        return targetVenderExpressScore;
    }

    public void setTargetVenderExpressScore(String targetVenderExpressScore) {
        this.targetVenderExpressScore = targetVenderExpressScore;
    }

    public String getVenderServiceAttiScore() {
        return venderServiceAttiScore;
    }

    public void setVenderServiceAttiScore(String venderServiceAttiScore) {
        this.venderServiceAttiScore = venderServiceAttiScore;
    }

    public String getInfoVenderServiceAttiScore() {
        return infoVenderServiceAttiScore;
    }

    public void setInfoVenderServiceAttiScore(String infoVenderServiceAttiScore) {
        this.infoVenderServiceAttiScore = infoVenderServiceAttiScore;
    }

    public String getTargetVenderServiceAttiScore() {
        return targetVenderServiceAttiScore;
    }

    public void setTargetVenderServiceAttiScore(String targetVenderServiceAttiScore) {
        this.targetVenderServiceAttiScore = targetVenderServiceAttiScore;
    }

    public String getSkuDescScore() {
        return skuDescScore;
    }

    public void setSkuDescScore(String skuDescScore) {
        this.skuDescScore = skuDescScore;
    }

    public String getInfoSkuDescScore() {
        return infoSkuDescScore;
    }

    public void setInfoSkuDescScore(String infoSkuDescScore) {
        this.infoSkuDescScore = infoSkuDescScore;
    }

    public String getTargetSkuDescScore() {
        return targetSkuDescScore;
    }

    public void setTargetSkuDescScore(String targetSkuDescScore) {
        this.targetSkuDescScore = targetSkuDescScore;
    }

    public String getOpenTm() {
        return openTm;
    }

    public void setOpenTm(String openTm) {
        this.openTm = openTm;
    }

    public String getUserEvaluateScore() {
        return userEvaluateScore;
    }

    public void setUserEvaluateScore(String userEvaluateScore) {
        this.userEvaluateScore = userEvaluateScore;
    }

    public String getCustomServiceConsultScore() {
        return customServiceConsultScore;
    }

    public void setCustomServiceConsultScore(String customServiceConsultScore) {
        this.customServiceConsultScore = customServiceConsultScore;
    }

    public String getLogisticsLvyueScore() {
        return logisticsLvyueScore;
    }

    public void setLogisticsLvyueScore(String logisticsLvyueScore) {
        this.logisticsLvyueScore = logisticsLvyueScore;
    }

    public String getAfterServiceScore() {
        return afterServiceScore;
    }

    public void setAfterServiceScore(String afterServiceScore) {
        this.afterServiceScore = afterServiceScore;
    }

    public String getDisputeScore() {
        return disputeScore;
    }

    public void setDisputeScore(String disputeScore) {
        this.disputeScore = disputeScore;
    }

    public String getFxgRankRate() {
        return fxgRankRate;
    }

    public void setFxgRankRate(String fxgRankRate) {
        this.fxgRankRate = fxgRankRate;
    }

    public String getAffectFactor() {
        return affectFactor;
    }

    public void setAffectFactor(String affectFactor) {
        this.affectFactor = affectFactor;
    }
}