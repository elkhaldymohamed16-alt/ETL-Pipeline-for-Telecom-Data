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

    @Bean
    public ItemProcessor<CallRecordDto, CallRecord> processor() {
        return dto -> {
            int duration = Integer.parseInt(dto.getDurationSeconds());

            if (duration == 0) {
                System.out.println("🚫 Dropped Missed Call from: " + dto.getCallerNumber());
                return null;
            }


            CallRecord record = new CallRecord();
            record.setCallerNumber(dto.getCallerNumber());
            record.setReceiverNumber(dto.getReceiverNumber());
            record.setDurationSeconds(duration);
            record.setCallType(dto.getCallType());
            record.setCallDate(LocalDate.parse(dto.getCallDate()));

            if ("INTERNATIONAL".equalsIgnoreCase(dto.getCallType())) {
                record.setPrice(duration * 5.0);
            } else {
                record.setPrice(duration * 1.0);
            }

            return record;
        };
    }

    @Bean
    public RepositoryItemWriter<CallRecord> writer(CallRecordRepository repository) {
        return new RepositoryItemWriterBuilder<CallRecord>()
                .repository(repository)
                .methodName("save")
                .build();
    }

    @Bean
    public Step processCdrStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               CallRecordRepository repository) {
        return new StepBuilder("processCdrStep", jobRepository)
                .<CallRecordDto, CallRecord>chunk(500, transactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer(repository))
                .faultTolerant()
                .skip(Exception.class)
                .skipLimit(10)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job etlJob(JobRepository jobRepository, Step processCdrStep) {
        return new JobBuilder("telecomEtlJob", jobRepository)
                .incrementer(new org.springframework.batch.core.launch.support.RunIdIncrementer())
                .start(processCdrStep)
                .build();
    }
}