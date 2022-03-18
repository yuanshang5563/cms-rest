package org.ys.crawler.downloader;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.PlainText;

@Component("footballLeagueMatchDownloader")
public class FootballLeagueMatchDownloader implements Downloader {
    private RemoteWebDriver driver;
    //private String startUrl = "https://www.hao123.com/?tn=94158081_hao_pg";
    private String startUrl = "http://web.leidata.com/stats/competition/";

    /**
     * 在构造方法中初始流浪器
     */
    private void createChromeDriver() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Administrator\\AppData\\Local\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        //设置无头模式
        //chromeOptions.addArguments("--headless");
        chromeOptions.setHeadless(true);
        //设置浏览器打开时的大小
        chromeOptions.addArguments("--window-size-1366,700");
        //禁止使用gpu
        chromeOptions.addArguments("--disable-gpu");
        //禁止加载图片
        chromeOptions.addArguments("--blink-settings=imagesEnabled=false");
        //禁止加载所有插件
        chromeOptions.addArguments("--disable-plugins");
        //当html下载完成之后，不等待解析完成，selenium会直接返回
        //(1) NONE: 当html下载完成之后，不等待解析完成，selenium会直接返回
        //(2) EAGER: 要等待整个dom树加载完成，即DOMContentLoaded这个事件完成，仅对html的内容进行下载解析
        //(3) NORMAL: 即正常情况下，selenium会等待整个界面加载完成（指对html和子资源的下载与解析,如JS文件，图片等，不包括ajax）
        //chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
        driver = new ChromeDriver(chromeOptions);
    }

    @Override
    public Page download(Request request, Task task) {
        Page page = null;
        try {
            createChromeDriver();
            String url = request.getUrl();
            //访问url
            driver.get(url);
            Thread.sleep(3000);
            //下滑到底部
            driver.executeScript("window.scrollTo(0,document.body.scrollHeight - 300)");
            Thread.sleep(3000);
            String html = driver.getPageSource();
            page = createPage(html,driver.getCurrentUrl());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            driver.close();
        }
        return page;
    }

    @Override
    public void setThread(int i) {

    }

    private Page createPage(String html,String url){
        Page page = new Page();
        page.setRawText(html);
        page.setUrl(new PlainText(url));
        page.setRequest(new Request(url));
        page.setDownloadSuccess(true);
        return page;
    }
}
