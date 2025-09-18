# Dockerfile

# ... other Dockerfile content ...

COPY --from=builder /home/gradle/project/build/libs/cuoco-0.0.1-SNAPSHOT.jar app.jar

# ... other Dockerfile content ...