package com.example.telecometl.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "call_records", indexes = {
        @Index(name = "idx_caller_date", columnList = "callerNumber, callDate")
})
public class CallRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String callerNumber;

    @Column(nullable = false)
    private String receiverNumber;

    @Column(nullable = false)
    private int durationSeconds;

    @Column(nullable = false)
    private String callType;

    @Column(nullable = false)
    private LocalDate callDate;

    private double price;

    public CallRecord() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCallerNumber() { return callerNumber; }
    public void setCallerNumber(String callerNumber) { this.callerNumber = callerNumber; }

    public String getReceiverNumber() { return receiverNumber; }
    public void setReceiverNumber(String receiverNumber) { this.receiverNumber = receiverNumber; }

    public int getDurationSeconds() { return durationSeconds; }
    public void setDurationSeconds(int durationSeconds) { this.durationSeconds = durationSeconds; }

    public String getCallType() { return callType; }
    public void setCallType(String callType) { this.callType = callType; }

    public LocalDate getCallDate() { return callDate; }
    public void setCallDate(LocalDate callDate) { this.callDate = callDate; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}