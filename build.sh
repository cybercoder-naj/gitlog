./gradlew clean build

cd ./build/distributions || exit 1
tar -xvf gitlog-0.0.1.tar
cd gitlog-0.0.1/bin || exit 1
cp gitlog $HOME/.local/bin