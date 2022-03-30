/******************* New Lambda Function class *******************/
package com.javainuse.config;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class LambdaFunction implements RequestHandler<Object, Object> {

    @Autowired()
    Job processJob;

    @Autowired()
    JobLauncher jobLauncher;

    static AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(LambdaFunction.class.getPackage().getName());

    public LambdaFunction() {
        ctx.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override()
    public Object handleRequest(Object input, Context context) {
        System.out.println("batch to serverless function 2");
        try {
            JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(processJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
        return "Batch job has been invoked";
    }
}