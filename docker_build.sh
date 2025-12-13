#!/bin/bash

docker buildx build --no-cache --platform linux/arm64 -t task_tracker .