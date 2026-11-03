## Dockerfile summary (Q&A)
    
    **What is a Dockerfile?**
    A recipe with steps to build an image.
    
    **What is an image? A container?**
    An image is a packed app plus everything it needs. A container is a running copy of that image.
    
    **What is a multi-stage build?**
    A Dockerfile with two `FROM` lines. Stage 1 builds the jar, and stage 2 runs it.
    
    **Why two stages?**
    The final image keeps only the jar and a small Java runtime. No Maven, no source code.
    
    **In what order does it run?**
    Top to bottom. The picture is drawn bottom to top only because layers stack up.
    
    **What is a layer?**
    The result of one instruction. Docker caches layers and reuses them if nothing changed.
    
    **Why copy `pom.xml` before `src`?**
    Dependencies download once and get cached, so code changes rebuild faster.
    
    **What does `FROM` do?**
    Picks the base image to start from.
    
    **What does `WORKDIR /app` do?**
    Sets the current folder inside the image, like `cd /app`.
    
    **What does `COPY` do?**
    Copies files from your computer into the image.
    
    **What does `COPY --from=build` do?**
    Takes a file (the jar) from stage 1 into stage 2.
    
    **What does `RUN` do?**
    Runs a command during the build, such as `mvn clean package`.
    
    **What does `EXPOSE 8080` do?**
    Documents the app's port. It doesn't open it. `-p 8080:8080` in `docker run` does that.
    
    **What does `ENTRYPOINT` do?**
    Sets the command that runs when the container starts: `java -jar app.jar`.
    
    **Build vs run?**
    `docker build` creates the image. `docker run` starts a container from it, and Docker runs the `ENTRYPOINT` for you.
    
    **Why JRE and not JDK in stage 2?**
    The JRE only runs Java and is smaller. The JDK is for building, and that's already done in stage 1.