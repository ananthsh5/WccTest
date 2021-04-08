package com.app.wcc.config;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.partition.support.MultiResourcePartitioner;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.app.wcc.domain.PostCodeDetail;

/**
 * @author Ananth Shanmugam
 * Class to define configuration for spring batch
 */

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

//	@Value("classPath:/ukpostcodes.csv")
	@Value("${csvfile.path}")
	private Resource inputResource;

	@Bean
	public Job readCSVFileJob() {
		return jobBuilderFactory.get("readCSVFileJob").incrementer(new RunIdIncrementer()).start(masterStep()).build();
	}

	//setup the partitioner for the batch
	@Bean
	public Partitioner partitioner() {
		MultiResourcePartitioner partitioner = new MultiResourcePartitioner();
		ClassLoader cl = this.getClass().getClassLoader();
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(cl);
		Resource[] resources = new Resource[1];
		resources[0] = inputResource;
		partitioner.setResources(resources);
		partitioner.partition(10);
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(5);
		taskExecutor.afterPropertiesSet();
		return taskExecutor;
	}

	@Bean
	@Qualifier("masterStep")
	public Step masterStep() {
		return stepBuilderFactory.get("masterStep").partitioner(step()).partitioner("step", partitioner())
				.taskExecutor(taskExecutor()).build();
	}

	@Bean
	public Step step() {
		return stepBuilderFactory.get("step").<PostCodeDetail, PostCodeDetail>chunk(300000).reader(reader())
				.writer(writer()).build();
	}

	@Bean
	public ItemProcessor<PostCodeDetail, PostCodeDetail> processor() {
		return new DBLogProcessor();
	}

	// set the batch csv reader properties 
	@Bean
	public FlatFileItemReader<PostCodeDetail> reader() {
		FlatFileItemReader<PostCodeDetail> itemReader = new FlatFileItemReader<PostCodeDetail>();
		itemReader.setLineMapper(lineMapper());
		itemReader.setResource(inputResource);
		return itemReader;
	}

	// set the columns to read from csv file
	@Bean
	public LineMapper<PostCodeDetail> lineMapper() {
		DefaultLineMapper<PostCodeDetail> lineMapper = new DefaultLineMapper<PostCodeDetail>();
		DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
		lineTokenizer.setDelimiter(",");// id,postcode,latitude,longitude
		lineTokenizer.setNames(new String[] { "id", "postcode", "latitude", "longitude" });
		lineTokenizer.setIncludedFields(new int[] { 0, 1, 2, 3 });
		BeanWrapperFieldSetMapper<PostCodeDetail> fieldSetMapper = new BeanWrapperFieldSetMapper<PostCodeDetail>();
		fieldSetMapper.setTargetType(PostCodeDetail.class);
		lineMapper.setLineTokenizer(lineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		return lineMapper;
	}

	// to insert the records into database
	@Bean
	public JdbcBatchItemWriter<PostCodeDetail> writer() {
		JdbcBatchItemWriter<PostCodeDetail> itemWriter = new JdbcBatchItemWriter<PostCodeDetail>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setSql(
				"INSERT INTO post_code_detail (id, postcode, latitude, longitude) VALUES (:id,:postcode, :latitude, :longitude)");
		itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<PostCodeDetail>());
		return itemWriter;
	}

}