# Java Agent to mitigate Log4j vulnerability

# Ref: https://www.cisa.gov/uscert/ncas/current-activity/2021/12/13/cisa-creates-webpage-apache-log4j-vulnerability-cve-2021-44228

# How to run
- mvn clean install
- Try: java -javaagent:<path to target/JavaAgent-1.0-jar-with-dependencies.jar> <rest of JVM args> 