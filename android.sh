cd android-project

## Run tests
echo "Building android tests"
./gradlew test

## Build project
echo "Building android application"
./gradlew clean assembleDebug
