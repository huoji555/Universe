package com.ragty.universe.Analyzer;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Fetcher {

    private long lastRequestTime = -1;
    private long minIterval = 1000;


    /**
     * @Author Ragty
     * @Description 找到并解析数据
     * @Date 10:15 2019/4/16
     **/
    public Elements fetchUrl(String url) throws IOException {
        sleepIfNeed();
        Connection connection = Jsoup.connect(url).ignoreContentType(true);
        Document doc = connection.get();
        Elements res = new Elements();
        Elements para = doc.getElementsByClass("main-content");
        for (Element element : para) {
            Elements e1 = element.getElementsByClass("para");
            for (Element element1 : e1) {
                res.add(element1);
            }
        }
        return res;
    }



    /**
     * @Author Ragty
     * @Description 从src读取网页的内容
     * @Date 10:32 2019/4/16
     **/
    public Elements readUrl(String url) throws IOException {
        URL realUrl = new URL(url);

        // 组成文件名
        String slash = File.separator;
        String filename = "resources" + slash + realUrl.getHost() + realUrl.getPath();

        // 读取文件
        InputStream stream = Fetcher.class.getClassLoader().getResourceAsStream(filename);
        Document doc = Jsoup.parse(stream, "UTF-8", filename);

        Element content = doc.getElementById("mw-content-text");
        Elements para = content.select("p");
        return para;
    }


    /**
     * @Author Ragty
     * @Description 通过最小访问时间来限制范围访问频率
     * @Date 10:43 2019/4/16
     **/
    private void sleepIfNeed() {
        if (lastRequestTime != -1) {
            long currentTime = System.currentTimeMillis();
            long nextRequestTime = lastRequestTime + minIterval;
            if (currentTime < lastRequestTime) {
                try {
                    Thread.sleep(nextRequestTime - currentTime);
                } catch (InterruptedException e) {
                    System.err.println("Warning: sleep interrupted in fetchWikipedia.");
                }
            }
        }
        lastRequestTime = System.currentTimeMillis();
    }


    public static void main(String[] args) throws Exception {
        Fetcher wf = new Fetcher();
        String url = "https://baike.baidu.com/item/javascript";
        Elements paragraphs = wf.fetchUrl(url);

        System.out.println(paragraphs);

        for (Element paragraph : paragraphs) {
            System.out.println(paragraph);
        }
    }


}
