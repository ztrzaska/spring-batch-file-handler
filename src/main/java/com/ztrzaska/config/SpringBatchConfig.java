package com.ztrzaska.config;

import com.ztrzaska.batch.UserJobExecutionDecider;
import com.ztrzaska.model.User;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;


@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {

    @Bean
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                   @Qualifier("csvFileReader1") ItemReader<User> userItemReader1,
                   @Qualifier("csvFileReader2") ItemReader<User> userItemReader2,
                   ItemProcessor<User, User> userItemProcessor,
                   ItemWriter<User> userItemWriter, UserJobExecutionDecider decider) {
        TaskletStep step1 =
                stepBuilderFactory.get("ETL-file-read1").<User, User>chunk(100)
                        .reader(userItemReader1)
                        .processor(userItemProcessor)
                        .writer(userItemWriter)
                        .listener(decider)
                        .build();

        TaskletStep step2 =
                stepBuilderFactory.get("ETL-file-read2").<User, User>chunk(100)
                        .reader(userItemReader2)
                        .processor(userItemProcessor)
                        .writer(userItemWriter)
                        .listener(decider)
                        .build();

        return jobBuilderFactory.get("ETL-job").incrementer(new RunIdIncrementer())
                .start(step1)
                .next(decider).on(BatchStatus.FAILED.name()).end()
                .from(decider).on(BatchStatus.COMPLETED.name()).to(step2)
                .end()
                .build();
    }

    @Bean
    public ItemReader<User> csvFileReader1(@Value("classpath:users1.csv") Resource usersFile) {
        return getItemReader(usersFile);
    }

    @Bean
    public ItemReader<User> csvFileReader2(@Value("classpath:users2.csv") Resource usersFile) {
        return getItemReader(usersFile);
    }

    private ItemReader<User> getItemReader(Resource usersFile) {
        return new FlatFileItemReaderBuilder<User>()
                .name("csv file reader")
                .resource(usersFile)
                .targetType(User.class)
                .linesToSkip(1)
                .delimited().delimiter(",").names(new String[]{"id", "name", "dept", "salary"})
                .build();
    }
}
