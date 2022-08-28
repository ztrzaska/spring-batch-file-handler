# Spring batch sequence job implementation

### Getting Started
Performing sequence of tasks with spring batch. Jobs are rely on loading csv files and can be started by rest endpoint.

### Configuring sequence of jobs

```
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
```



### Reference Documentation
For further reference, please consider the following sections:

* [Spring batch documentation](https://docs.spring.io/spring-batch/docs/current/reference/html/)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.0/maven-plugin/reference/html/)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.7.0/reference/htmlsingle/#data.sql.jpa-and-spring-data)

