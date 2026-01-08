#!/bin/sh
set -e

DB_NAME="task_tracker_db"
COLLECTION="jobListing"

# Use mongosh without explicit host, Docker sets the default
mongosh <<EOF
use $DB_NAME

var collName = "$COLLECTION";

if (!db.getCollectionNames().includes(collName)) {
    print("Creating collection: " + collName);
    db.createCollection(collName);
} else {
    print("Collection already exists: " + collName);
}

// Create unique index
db[collName].createIndex({ jobId: 1 }, { unique: true, name: "jobId_unique_idx" });

print("Mongo initialization complete");
EOF
