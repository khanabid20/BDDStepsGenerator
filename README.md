# BDDStepsGenerator

#The BDD itself an automation testing framework.
#Just to automate few things like generating Runner classes & Step files, i have created this project.

As you all component of BDD (Behavior Drivern Development) are 
1. Feature file (written in Gherkin language)
2. Runner class (while running this class, it generates methods for each steps written in feature file)
3. Lastly the Step file 

So instead of creating runner class & copy pasting the methods in step file, using this project will save few time as it does the same.

###################
### Running the jar 
###################
1. Copy the jar where all the feature files are
2. Double click on the jar 
3. It will create two folders (`Runner` -> For runner classes, `StepDefinition` -> For Step files)


**NOTE::** 
  1. ThereThere is a folder "Feature" having two feature files, just copy the jar there & run it for having the idea how it works.
 2. This project contains both pom.xml & build.gradle, so it can be built using maven as well we gradle.
 3. *`mvn [clean] package`* to clean the project & make a jar ; `gradle [clean] jar` will do the same as maven.


**Intend of this project is just to demonstrate how we can automate the thing. And utilizes both gradle & maven tools (one can use any of them). **

**Hope you all find it helpful**

**_THANK YOU_**
