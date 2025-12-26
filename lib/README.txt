# Mockito and JUnit 5 dependencies for manual/classpath use
# If you use Maven or Gradle, add these to your build file instead
# Download jars from https://search.maven.org/ if needed
junit-jupiter-api-5.9.3.jar
junit-jupiter-engine-5.9.3.jar
mockito-core-5.2.0.jar
junit-platform-console-standalone-1.9.3.jar
jbcrypt-0.4.jar (org.mindrot.jbcrypt)

Download the above jars and place them in this `lib/` directory, or switch the project to Maven/Gradle and declare dependencies there. jBCrypt (jbcrypt-0.4) is required for secure password hashing used by `Account.java`.
