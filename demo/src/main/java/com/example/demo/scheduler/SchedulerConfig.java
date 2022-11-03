package com.example.demo.scheduler;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import com.example.demo.service.StockService;

@Configuration
public class SchedulerConfig {
	final ApplicationContext applicationContext;

	public SchedulerConfig(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Bean
	SpringBeanJobFactory createSpringBeanJobFactory() {

		return new SpringBeanJobFactory() {

			@Override
			protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {

				final Object job = super.createJobInstance(bundle);

				applicationContext.getAutowireCapableBeanFactory().autowireBean(job);

				return job;
			}
		};
	}

	@Bean
	public SchedulerFactoryBean createSchedulerFactory(SpringBeanJobFactory springBeanJobFactory, Trigger trigger) {

		SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
		schedulerFactory.setAutoStartup(true);
		schedulerFactory.setWaitForJobsToCompleteOnShutdown(true);
		schedulerFactory.setTriggers(trigger);

		springBeanJobFactory.setApplicationContext(applicationContext);
		schedulerFactory.setJobFactory(springBeanJobFactory);

		return schedulerFactory;
	}

	@Bean
	public SimpleTriggerFactoryBean createSimpleTriggerFactoryBean(JobDetail jobDetail) {
		SimpleTriggerFactoryBean simpleTriggerFactory = new SimpleTriggerFactoryBean();
//		Conversion base : 1 day = 86400000 ms
		Long oneDay = 86400000L; // ms
		simpleTriggerFactory.setJobDetail(jobDetail);
		simpleTriggerFactory.setStartDelay(0);
		simpleTriggerFactory.setRepeatInterval(oneDay);
		return simpleTriggerFactory;
	}

	@Bean
	public JobDetailFactoryBean createJobDetailFactoryBean() {

		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
		jobDetailFactory.setJobClass(StockService.class);
		return jobDetailFactory;
	}

}
