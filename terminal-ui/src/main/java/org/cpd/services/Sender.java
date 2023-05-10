package org.cpd.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

@Slf4j
public class Sender {

    public void send(String json) {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("http://localhost:8081/write");

        httppost.setEntity(new StringEntity(json, "UTF-8"));
        httppost.setHeader("Content-type", "application/json");

        try {
            httpclient.execute(httppost);
        } catch (IOException e) {
            log.error("Could not send :(");
        }
    }
}
