**Use these commands instead of docker build commands!** ```docker build``` will not work, because the Dockerfile is generated runtime by ```./gradlew dockerBuildImage```. 

# To build everything locally (docker-compose build):

```
./gradlew composeBuild
```

# To start and run everything locally (docker-compose up):

```
./gradlew composeUp
```

**DON'T FORGET TO STOP THEM ;)**

```
./gradlew composeDown
```

# To build docker image:

```
./gradlew dockerBuildImage
```

# Pushing to Docker hub
- Configure version in build.gradle
```
./gradlew dockerPushImage -DDOCKER_HUB_USERNAME='username' -DDOCKER_HUB_PASSWORD='password'
```

# Deploy
- Merge to Master
- Checkout master
```
git tag 1.0.0 (specific version number)
git push --tags
```
- Finish upgrade in Rancher if all is good.