package org.bigant.wf.process.form.option;


public class AmountOption {


    public enum AmountType {
        CNY("人民币"),
        USD("美元"),
        EUR("欧元"),
        GBP("英镑"),
        HKD("港币"),
        JPY("日元"),
        TWD("新台币"),
        AUD("澳元"),
        NZD("新西兰元"),
        CHF("瑞士法郎"),
        SEK("瑞典克朗"),
        NOK("挪威克朗"),
        CAD("加拿大元");

        private String desc;

        AmountType(String desc) {
            this.desc = desc;
        }


    }
}
