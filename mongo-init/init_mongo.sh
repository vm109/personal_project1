#!/bin/sh
MONGO_URI="mongodb://localhost:27017"
DB_NAME="task_tracker_db"
COLLECTION="jobListing"

mongosh "$MONGO_URI/$DB_NAME" <<EOF

// 1️⃣ Create collection if not exists
if (!db.getCollectionNames().includes("$COLLECTION")) {
    print("Creating collection: $COLLECTION");
    db.createCollection("$COLLECTION");
} else {
    print("Collection already exists: $COLLECTION");
}

// 2️⃣ Create unique index on email
db.$COLLECTION.createIndex(
    { jobId: 1 },
    { unique: true, name: "jobId_unique_idx" }
);

print("Mongo initialization complete");

EOF