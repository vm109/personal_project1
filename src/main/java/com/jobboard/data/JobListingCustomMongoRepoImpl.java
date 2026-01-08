package com.jobboard.data;


import com.jobboard.models.dto.JobListing;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobListingCustomMongoRepoImpl implements JobListingCustomMongoRepo {
    private MongoTemplate mongoTemplate;

    public JobListingCustomMongoRepoImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void upsertIgnoreDuplicates(List<JobListing> jobs) {
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, JobListing.class);

        for (JobListing job : jobs) {
            Query query = Query.query(Criteria.where("jobId").is(job.getJobId()));

            bulkOperations.upsert(query, org.springframework.data.mongodb.core.query.Update.update("jobId", job.getJobId())
                    .set("title", job.getTitle())
                    .set("tagsAndSkills", job.getTagsAndSkills())
                    .set("jobDescription", job.getJobDescription())
                    .set("createdDate", job.getCreatedDate())
                    .set("minimumExperience", job.getMinimumExperience())
                    .set("maximumExperience", job.getMaximumExperience())
                    .set("companyName", job.getCompanyName())
            );
        }

        bulkOperations.execute();
    }
}
