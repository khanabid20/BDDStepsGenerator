package com.khanabid20.bdd.autogen.runner_stepfiles.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;

import org.apache.log4j.Logger;

/**
 * This class runs the whole project
 * 
 * @author abid.khan
 *
 */
public class App 
{
	static Logger log = Logger.getLogger(App.class);
	static String RUNNER_CLASS_OUTPUT_FOLDER = "\\Runner\\";
	static String STEPDEFINITION_OUTPUT_FOLDER = ".\\StepDefinition\\";
	
	static Process process = null;
	
    public static void main( String[] args ) throws IOException
    {
    	log.info("Seraching for all .feature file");
    	// Generating Runner Classes
    	for(String file: searchFeatureFiles(new File("."))){
//    		System.out.println(file);
    		generateRunner(file);
    	}
    }
    
    
    /**
     * This method takes feature file name as and generates runner class for the same,
     * runner template is stored in a string variable for later appending in the runner class file
     * 
     * @param file
     * @throws IOException
     */
    private static void generateRunner(String file) throws IOException {

    	new File("."+RUNNER_CLASS_OUTPUT_FOLDER).mkdir();
    	String runnerTemplate =
    			"import org.junit.runner.RunWith;\n"
    			+ "import cucumber.api.CucumberOptions;\n"
    			+ "import cucumber.api.junit.Cucumber;\n\n"
    			+ "@RunWith(Cucumber.class)\n"
    			+ "@CucumberOptions(features = \"Feature/"+file+".feature\", glue={\"stepDefinition\"}, monochrome = true)\n"
    			+ "public class "+file.charAt(0)+"".toUpperCase()+file.substring(1)+"Runner {\n\t\n"
    			+ "}";
    	
    	FileWriter fileWriter = new FileWriter("."+RUNNER_CLASS_OUTPUT_FOLDER+file+"TestRunner.java");
    	fileWriter.append(runnerTemplate);
    	fileWriter.close();
    	
    	// calling this method to generate the step file
    	generateStepFile(file.charAt(0)+"".toUpperCase()+file.substring(1));
	}

    /**
     * This method generates the step file, also got the template declared as string variable
     * 
     * @param stepFileName
     * @throws IOException
     */
	private static void generateStepFile(String stepFileName) throws IOException {

    	new File(STEPDEFINITION_OUTPUT_FOLDER).mkdir();
		StringBuilder steps = new StringBuilder();
		
		//using buffer reader as scanner run out of memory
		String line;             
		BufferedReader in = new BufferedReader(new InputStreamReader(runRunnerClass(stepFileName)));             
		while ((line = in.readLine()) != null) {
			process.destroy();	
			steps.append(line+"\n");
		}
		in.close();
/*
    	// Storing standard output into scanner (Scanner is faster than BufferedReader)
    	Scanner scan = new Scanner(runRunnerClass(stepFileName));
    	while(scan.hasNextLine()){
        	process.destroy();		//	important to destroy process, else it haults
//    		String str = scan.nextLine();
//    		System.out.println(str);
//    		steps.append(str+"\n");
        	steps.append(scan.nextLine()+"\n");
    	}
    	scan.close();
*/	
    	// Handling exception if steps are not generated
    	if(steps.toString().contains("@Given")){
			String stepFileTemplate = 
					"import cucumber.api.PendingException;\n"
					+ "import cucumber.api.java.en.Given;\n"
					+ "import cucumber.api.java.en.Then;\n"
					+ "import cucumber.api.java.en.When;\n\n"
					+ "class "+stepFileName+"StepDef{\n\n"
					+	steps.substring(steps.indexOf("@"))	+ "\n\n}";
			FileWriter stepFileWriter = new FileWriter(STEPDEFINITION_OUTPUT_FOLDER+stepFileName+"StepDef.java");
			stepFileWriter.append(stepFileTemplate);
			stepFileWriter.close();
			System.out.println(steps);
    	}
		else {
			log.error("\""+stepFileName + ".feature\" -> Check Feature file for some typing error..");
			StringBuilder errorString =  new StringBuilder();
			Scanner scan = new Scanner(process.getErrorStream());
			while (scan.hasNextLine()) 
				errorString.append(scan.nextLine()+"\n");	
			log.error(errorString.toString().substring(errorString.toString().lastIndexOf("Caused")));
			scan.close();
    	}
    		
	}

	
	/**
	 * This method is for compiling java class & running the generated class file 
	 * and returns the console output as InputStream to the Caller function
	 * 
	 * @param runner
	 * @return
	 * @throws IOException
	 */
	private static InputStream runRunnerClass(String runner) throws IOException {
		try {
			process = Runtime.getRuntime()
					.exec("cmd /K \"java -cp " + System.getProperty("java.class.path")
							+ " cucumber.api.cli.Main -m --glue \"stepDefinition\" "+runner+".feature\"");
		} 
		catch (Exception e) {
			System.out.println("Couldn't run Runner class..");
			e.printStackTrace();
		}
		return process.getInputStream();
	}

	/**
	 * 
	 * This method search the current directory for all .feature files & store the name in a list of string 
	 * and returns the same
	 * 
	 * @param fileDir
	 * @return
	 */
	static List<String> searchFeatureFiles(File fileDir){
    	List<String> fileNames = new ArrayList<String>();
		for(File file: fileDir.listFiles()){
			if((!file.isDirectory())&&file.getName().endsWith(".feature")){
				fileNames.add(file.getName().substring(0, file.getName().indexOf(".")));
			}
		}
		return fileNames;
    }
}

