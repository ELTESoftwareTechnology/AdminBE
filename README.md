**Use these commands instead of docker build commands!** ```docker build``` will not work, because the Dockerfile is generated runtime by ```./gradlew dockerBuildImage```. 

To build everything locally (docker-compose build):

```
./gradlew composeBuild
```

To start and run everything locally (docker-compose up):

```
./gradlew composeUp
```

**DON'T FORGET TO STOP THEM ;)**

```
./gradlew composeDown
```

To build docker image:

```
./gradlew dockerBuildImage
```

Pushing to Docker hub
- Configure version in build.gradle
- Add credentials.json to root
```
{
  "username": "docker hub username",
  "password": "docker hub password"
}
```
- Execute in terminal:
```
./gradlew dockerPushImage
```