#/bin/bash

set -xeuo pipefail

rsync -av --exclude="*.java" ./src/hust/cs/javacourse/search/ ./bin/production/SearchEngineForStudent/hust/cs/javacourse/search/
