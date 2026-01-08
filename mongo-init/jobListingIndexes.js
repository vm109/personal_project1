db = db.getSiblingDB("task_tracker_db");

db.jobListing.createIndex(
    { jobId: 1 },
    { unique: true, name: "jobId_unique_idx" }
);