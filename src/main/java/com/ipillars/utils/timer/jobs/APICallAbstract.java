package com.ipillars.utils.timer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;

import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

@Slf4j
abstract class APICallAbstract {

    protected final static String TEXT_CONTENT = "text/html; charset=UTF-8";
    protected final static String FORM_CONTENT = "application/x-www-form-urlencoded";

    protected String url = "http://ip.jsontest.com/";
    protected String userAgent = "Common-Scheduler";

    protected JobDataMap dataMap;

    public void contextReader(JobExecutionContext context) {

        JobKey key = context.getJobDetail().getKey();
        dataMap = context.getMergedJobDataMap();

        // dataMap.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    abstract void run(JobExecutionContext context);

    // Sample: 'password=123&custom=secret&username=abc&ts=1570704369823'
    public HttpRequest.BodyPublisher ofFormData(Map<Object, Object> data) {
        var builder = new StringBuilder();
        for (Map.Entry<Object, Object> entry : data.entrySet()) {
            if (builder.length() > 0) {
                builder.append("&");
            }
            builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
            builder.append("=");
            builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
        }
        return HttpRequest.BodyPublishers.ofString(builder.toString());
    }

    public HttpClient getHttpClient() {

        return HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                //.proxy(ProxySelector.of(new InetSocketAddress("your-company-proxy.com", 8080)))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                // .proxy(ProxySelector.of(new InetSocketAddress("proxy.yourcompany.com", 80)))
                // .authenticator(Authenticator.getDefault())
                /*
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                "user",
                                "password".toCharArray());
                    }
                })
                */
                .build();
    }

    public JobDataMap getDataMap() {
        return dataMap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
