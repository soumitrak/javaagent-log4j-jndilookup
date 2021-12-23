# Java Agent to mitigate Log4j vulnerability

# Ref: https://www.cisa.gov/uscert/ncas/current-activity/2021/12/13/cisa-creates-webpage-apache-log4j-vulnerability-cve-2021-44228

# How to run
- mvn clean install
- For static loading: java -javaagent:<path to JavaAgent-1.0-jar-with-dependencies.jar> <rest of JVM args> 
- For dynamically loading the agent to already running process:
  - File the process id of the target Java process to attach the agent
  - java -jar <path to JavaAgent-1.0-jar-with-dependencies.jar> <path to JavaAgent-1.0-jar-with-dependencies.jar> <PID>
  - If you get java.lang.VerifyError, then the target process should be running with -Xverify:none