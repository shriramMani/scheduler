package com.ipillars.utils.timer.jobs;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class APIPost extends APICallAbstract implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        run(jobExecutionContext);
    }

    @Override
    void run(JobExecutionContext context) {

        contextReader(context);

        try {

            HttpClient httpClient = getHttpClient();

            // form parameters
            Map<Object, Object> data = new HashMap<>();
            data.put("username", "abc");
            data.put("password", "123");
            data.put("custom", "secret");
            data.put("ts", System.currentTimeMillis());

            HttpRequest request = HttpRequest.newBuilder()
                    .POST(ofFormData(data))
                    .uri(URI.create(url))
                    .setHeader("User-Agent", userAgent)
                    .header("Content-Type", FORM_CONTENT)
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // print status code
            System.out.println(response.statusCode());

            // print response body
            System.out.println(response.body());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
