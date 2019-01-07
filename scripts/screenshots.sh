ORIGINAL_PATH="pwd -P"

cd "$(dirname $0)/.."

./gradlew assembleDebug assembleAndroidTest

fastlane screengrab

cd $ORIGINAL_PATH