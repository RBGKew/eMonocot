package org.emonocot.harvest.integration;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.FacetField;
import org.emonocot.api.AnnotationService;
import org.emonocot.api.CommentService;
import org.emonocot.api.ResourceService;
import org.emonocot.api.UserService;
import org.emonocot.model.Annotation;
import org.emonocot.model.Comment;
import org.emonocot.model.auth.User;
import org.emonocot.model.registry.Resource;
import org.emonocot.pager.Page;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class NotifyingJobStatusListener extends JobExecutionListenerSupport {
	
	private static Logger logger = LoggerFactory.getLogger(NotifyingJobStatusListener.class);
	
	private static final String BUNDLE_NAME = "org.joda.time.format.messages";

	private ResourceService resourceService;

	private AnnotationService annotationService;

	private UserService userService;

	private CommentService commentService;

	private String systemUser;

	private PeriodFormatter periodFormatter;
	
	private JobRepository jobRepository;

	@Autowired
	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@Autowired
	public void setAnnotationService(AnnotationService annotationService) {
		this.annotationService = annotationService;
	}

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setCommentService(CommentService commentService) {
		this.commentService = commentService;
	}
	
	@Autowired
	public void setJobRepository(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	public void setSystemUser(String systemUser) {
		this.systemUser = systemUser;
	}
	
	public NotifyingJobStatusListener() {
		 ResourceBundle b = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ENGLISH);
         String[] variants = {
                 b.getString("PeriodFormat.space"), b.getString("PeriodFormat.comma"),
                 b.getString("PeriodFormat.commandand"), b.getString("PeriodFormat.commaspaceand")};
         this.periodFormatter = new PeriodFormatterBuilder()
             .appendYears()
             .appendSuffix(b.getString("PeriodFormat.year"), b.getString("PeriodFormat.years"))
             .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
             .appendMonths()
             .appendSuffix(b.getString("PeriodFormat.month"), b.getString("PeriodFormat.months"))
             .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
             .appendWeeks()
             .appendSuffix(b.getString("PeriodFormat.week"), b.getString("PeriodFormat.weeks"))
             .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
             .appendDays()
             .appendSuffix(b.getString("PeriodFormat.day"), b.getString("PeriodFormat.days"))
             .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
             .appendHours()
             .appendSuffix(b.getString("PeriodFormat.hour"), b.getString("PeriodFormat.hours"))
             .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
             .appendMinutes()
             .appendSuffix(b.getString("PeriodFormat.minute"), b.getString("PeriodFormat.minutes"))
             .appendSeparator(b.getString("PeriodFormat.commaspace"), b.getString("PeriodFormat.spaceandspace"), variants)
             .appendSeconds()
             .appendSuffix(b.getString("PeriodFormat.second"), b.getString("PeriodFormat.seconds"))
             .toFormatter();
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
				
		Period duration = new Period(jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime());
		StringBuffer exitDescription = new StringBuffer();

		if (jobExecution.getJobInstance().getJobParameters().getString("resource.identifier") != null) {
			long info = 0;
			long warn = 0;
			long error = 0;
			
			Resource resource = resourceService.find(jobExecution
					.getJobInstance().getJobParameters()
					.getString("resource.identifier"));
			Map<String, String> selectedFacets = new HashMap<String, String>();
			selectedFacets.put("base.class_s", "org.emonocot.model.Annotation");
			selectedFacets.put("annotation.job_id_l",
					new Long(resource.getLastHarvestedJobId()).toString());
			try {
				Page<Annotation> result = annotationService.search(null, null,
						1, 0, new String[] { "annotation.type_s" }, null,
						selectedFacets, null, "annotated-obj");
				FacetField annotationTypes = result
						.getFacetField("annotation.type_s");
				for (FacetField.Count value : annotationTypes.getValues()) {
					switch (value.getName()) {
					case "Info":
						info = value.getCount();
						break;
					case "Warn":
						warn = value.getCount();
						break;
					case "Error":
						error = value.getCount();
						break;
					default:
						break;
					}
				}
			} catch (SolrServerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			exitDescription.append("Harvested " + resource.getTitle() + " " + jobExecution.getExitStatus().getExitCode());
			exitDescription.append(". " + jobExecution.getStepExecutions().size() + " Steps Completed.");
			exitDescription.append(" Duration: " + periodFormatter.print(duration) + ".");			
			exitDescription.append(" Issues - Info: " + info + ", warnings: "
					+ warn + ", Errors: " + error);
			jobExecution.setExitStatus(new ExitStatus("WIBBLE",exitDescription.toString()));
			
			jobRepository.update(jobExecution);

			logger.info(jobExecution.getExitStatus().getExitCode() + " " + jobExecution.getExitStatus().getExitDescription());
			User user = userService.load(systemUser);
			// Create comment
			Comment comment = new Comment();
			comment.setIdentifier(UUID.randomUUID().toString());
			comment.setComment(exitDescription.toString());
			comment.setCreated(new DateTime());
			comment.setStatus(Comment.Status.PENDING);
			comment.setUser(user);
			comment.setAboutData(resource);
			comment.setCommentPage(resource);
			commentService.save(comment);
		} else {
			exitDescription.append(jobExecution.getJobInstance().getJobName()  + " " + jobExecution.getExitStatus().getExitCode());
			exitDescription.append(". " + jobExecution.getStepExecutions().size() + " Steps Completed.");
			exitDescription.append(" Duration: " + periodFormatter.print(duration) + ".");
			jobExecution.setExitStatus(new ExitStatus(jobExecution.getExitStatus().getExitCode(),exitDescription.toString()));
			jobRepository.update(jobExecution);

			logger.info(jobExecution.getExitStatus().getExitCode() + " " + jobExecution.getExitStatus().getExitDescription());
		}
	}
}