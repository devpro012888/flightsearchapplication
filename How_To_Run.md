```markdown
# Spring Boot Application - Getting Started in Visual Studio Code

This guide will help you run a Spring Boot application using **Visual Studio Code (VS Code)**.

## Prerequisites

Ensure the following software is installed:

- [Java Development Kit (JDK) 17+](https://adoptopenjdk.net/)
- [Maven](https://maven.apache.org/download.cgi) or [Gradle](https://gradle.org/)
- [Visual Studio Code](https://code.visualstudio.com/)
- **VS Code Extensions:**
  - Java Extension Pack (includes Language Support for Java, Debugger for Java, Maven for Java, etc.)
  - Spring Boot Extension Pack (optional but helpful)

## Project Setup

1. **Clone or Create a Spring Boot Project**

   If you already have a Spring Boot project, open it in VS Code.

   If not, you can generate one from [https://start.spring.io](https://start.spring.io) and unzip it.

2. **Open Project in VS Code**

```

File > Open Folder...

```

Select the root directory of your Spring Boot project.

3. **Build the Project**

If using Maven:

```

./mvnw clean install

```

If using Gradle:

```

./gradlew build

````

*(Windows users should use `mvnw.cmd` or `gradlew.bat` respectively.)*

4. **Run the Application**

- Open `src/main/java/com/example/YourApplication.java`.
- Click the **Run** or **Debug** icon at the top of the `main` method.
- Alternatively, open the terminal and run:

  ```
  ./mvnw spring-boot:run
  ```

  or

  ```
  ./gradlew bootRun
  ```

5. **Access the Application**

Once running, the application will be accessible at:

````

[http://localhost:8080](http://localhost:8080)

```

## Common Issues

- **"Could not find or load main class"**: Ensure you are opening the correct folder (root of the Spring Boot project).
- **Port already in use**: Stop any process using port 8080 or change the port in `application.properties`.

## VS Code Tips

- Use the **Spring Boot Dashboard** (via the Spring Boot Extension Pack) to manage and run your applications.
- Set breakpoints and debug your app easily with the Java Debugger.
```
