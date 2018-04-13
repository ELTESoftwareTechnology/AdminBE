# To build docker image:

```./gradlew dockerBuildImage```

# Pushing to Docker hub
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

# To build everything locally (docker-compose build):

```./gradlew composeBuild```

# To start everything locally (docker-compose up):

```./gradlew composeUp```

# DON'T FORGET TO STOP THEM ;)

```./gradlew composeDown```
