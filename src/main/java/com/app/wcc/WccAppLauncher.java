package com.app.wcc;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Ananth Shanmugam
 * Class to define entry point for spring boot to run
 */
@SpringBootApplication
@EnableScheduling
public class WccAppLauncher {
	
	
    @Autowired
    JobLauncher jobLauncher;
      
    @Autowired
    Job job;


	public static void main(String[] args) {
		SpringApplication.run(WccAppLauncher.class, args);
	}

	//run the batch as soon as the spring boot starts
    @Scheduled(cron = "0 */1 * * * ?")
    public void perform() throws Exception
    {
        JobParameters parames = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();

        jobLauncher.run(job, parames);

    }

}
