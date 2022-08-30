package org.example.factoryDI;

import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class OkHttpTest {
    public static void main(String[] args) {
//        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
        OkHttpClient okHttpClient = (OkHttpClient) ctx.getBean("okHttpClient");

        Request request = new Request.Builder()
                .get()
                .url("https://www.baidu.com")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                System.out.println("e.getMessage() = " + e.getMessage());
            }

            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("response.body().string() = "+response.body().string());
            }
        });
    }
}
