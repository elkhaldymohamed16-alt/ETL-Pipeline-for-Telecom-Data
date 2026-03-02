package com.example.telecometl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

@SpringBootApplication
public class TelecomEtlApplication {

    public static void main(String[] args) {
        generateMassiveCsv();

        SpringApplication.run(TelecomEtlApplication.class, args);
    }

    private static void generateMassiveCsv() {
        String filePath = "data/cdr_data.csv";
        System.out.println(" Generating 100,000 records. Please wait...");
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("callerNumber,receiverNumber,durationSeconds,callType,callDate\n");
            Random random = new Random();
            for (int i = 1; i <= 100000; i++) {
                String type = random.nextBoolean() ? "LOCAL" : "INTERNATIONAL";
                int duration = random.nextInt(3600);
                writer.append("066000")
                        .append(String.format("%04d", random.nextInt(10000)))
                        .append(",066999")
                        .append(String.format("%04d", random.nextInt(10000)))
                        .append(",")
                        .append(String.valueOf(duration))
                        .append(",")
                        .append(type)
                        .append(",2026-03-02\n");
            }
            writer.append("BAD_DATA,000,abc,UNKNOWN,INVALID\n");
            System.out.println("✅ 100,000 records generated successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}