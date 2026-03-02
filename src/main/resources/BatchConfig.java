package com.example.telecometl.config;

import com.example.telecometl.entity.CallRecord;
import com.example.telecometl.entity.CallRecordDto;
import com.example.telecometl.repository.CallRecordRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;

@Configuration
public class BatchConfig {

    // 1. EXTRACT: Read the CSV file line by line safely.
    @Bean
    public FlatFileItemReader<CallRecordDto> reader() {
        return new FlatFileItemReaderBuilder<CallRecordDto>()
                .name("cdrReader")
                .resource(new FileSystemResource("data/cdr_data.csv"))
                .linesToSkip(1)
                .delimited()
                .names("callerNumber", "receiverNumber", "durationSeconds", "callType", "callDate")
                .targetType(CallRecordDto.class)
                .build();
    }

    // 2. TRANSFORM: Clean data and apply Business Logic (Math).
    @Bean
    public ItemProcessor<CallRecordDto, CallRecord> processor() {
        return dto -> {
            int duration = Integer.parseInt(dto.getDurationSeconds());

            //  BUSINESS RULE: Drop calls with 0 duration (Missed calls).
            if (duration == 0) {
                System.out.println(" Dropped Missed Call from: " + dto.getCallerNumber());
                return null;
            }

            CallRecord record = new CallRecord();
            record.setCallerNumber(dto.getCallerNumber());
            record.setReceiverNumber(dto.getReceiverNumber());
            record.setDurationSeconds(duration);
            record.setCallType(dto.getCallType());
            record.setCallDate(LocalDate.parse(dto.getCallDate()));

            //  BUSINESS RULE: Calculate Price
            if ("INTERNATIONAL".equalsIgnoreCase(dto.getCallType())) {
                record.setPrice(duration * 5.0); // 5 MAD per second
            } else {
                record.setPrice(duration * 1.0); // 1 MAD per second
            }

            return record;
        };
    }

    // 3. LOAD: Save to PostgreSQL in bulk
    @Bean
    public RepositoryItemWriter<CallRecord> writer(CallRecordRepository repository) {
        return new RepositoryItemWriterBuilder<CallRecord>()
                .repository(repository)
                .methodName("saveAll")
                .build();
    }

    // 4. THE STEP: Assembling the engine
    @Bean
    public Step processCdrStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               CallRecordRepository repository) {
        return new StepBuilder("processCdrStep", jobRepository)
                .<CallRecordDto, CallRecord>chunk(2, transactionManager) // Ignore the yellow strikethrough here, it's fine!
                .reader(reader())
                .processor(processor())
                .writer(writer(repository))
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .build();
    }

    // 5. THE JOB: The final pipeline
    @Bean
    public Job etlJob(JobRepository jobRepository, Step processCdrStep) {
        return new JobBuilder("telecomEtlJob", jobRepository)
                .start(processCdrStep)
                .build();
    }
}