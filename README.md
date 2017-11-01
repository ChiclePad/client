# ChiclePad backend
Java backend for the ChiclePad application. 

## Tech Stack
In order to run the backend you need these:
- [Oracle Java 9](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
- [Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org)

And some knowledge of [JUnit 5](http://junit.org/junit5/) and 
[JavaFX](http://www.oracle.com/technetwork/java/javase/overview/javafx-overview-2158620.html)

## Setup

Install dependencies on your system: 
#### Ubuntu
Oracle Java 9
```bash
$ sudo add-apt-repository ppa:webupd8team/java
$ sudo apt update
$ sudo apt install oracle-java9-installer
```

Maven
```bash
$ sudo apt install maven
```

PostgreSQL
```bash
$ sudo apt install postgresql
```

## How To Contribute

1. Fork the repository
2. Create new branch `$ git chceckout -b my_new_branch`
3. Commit your changes `$ git add *` `$ git commit`
4. Make sure you're up to date `$ git pull -r upstream devel` (where upstream is the main repoitory)
5. Upload to your fork `$ git push -u origin my_new_branch`
6. Submit a Pull Request
