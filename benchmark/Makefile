all: dist 

.PHONY: run 
run:
	@export GEM_HOME=./target/rubygems; mvn exec:java

.PHONY: compile
compile:
	@mvn compile

.PHONY: checkstyle
checkstyle:
	@mvn checkstyle:checkstyle 

.PHONY: clean
clean:
	@mvn clean

