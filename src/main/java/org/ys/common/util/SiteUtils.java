package org.ys.common.util;

import us.codecraft.webmagic.Site;

public class SiteUtils {
    private SiteUtils() {}

    public static Site generateSite(){
        Site site = Site.me()
                .setCharset("UTF-8")
                .setSleepTime(3000)
                .setTimeOut(6 * 1000)
                .setRetrySleepTime(3000)
                //.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:62.0) Gecko/20100101 Firefox/62.0")
                .setRetryTimes(3);
        return site;
    }
}
