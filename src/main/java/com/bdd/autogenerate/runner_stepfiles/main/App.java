package com.bdd.autogenerate.runner_stepfiles.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class runs the whole project
 * 
 * @author abid.khan
 *
 */
public class App 
{
	static String RUNNER_CLASS_OUTPUT_FOLDER = "\\Runner\\";
	static String STEPDEFINITION_OUTPUT_FOLDER = ".\\StepDefinition\\";
	
	static Process process = null;
	
    public static void main( String[] args ) throws IOException
    {
    	
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
    	
    	FileWriter fileWriter = new FileWriter("."+RUNNER_CLASS_OUTPUT_FOLDER+file+"Runner.java");
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
/*    	String line;
    	BufferedReader in = new BufferedReader(
                new InputStreamReader(runRunnerClass(stepFileName, RUNNER_CLASS_OUTPUT_FOLDER)) );
        while ((line = in.readLine()) != null) {
          System.out.println(line);
        	steps.append(line);
        }
        in.close();
        */
//    	String steps = runRunnerClass(stepFileName, RUNNER_CLASS_OUTPUT_FOLDER);
//    	Scanner scan = new Scanner(runRunnerClass(stepFileName, RUNNER_CLASS_OUTPUT_FOLDER));
    	
    	// Storing standard output into scanner
    	//Which is generated step methods 
    	Scanner scan = new Scanner(runRunnerClass(stepFileName));
    	while(scan.hasNextLine()){
        	process.destroy();		/**important to destroy process, else it haults**/
//    		String str = scan.nextLine();
//    		System.out.println(str);
//    		steps.append(str+"\n");
        	steps.append(scan.nextLine()+"\n");
    	}
    	scan.close();
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
			/*
			 * (second argument >> String runDir)
			String workingDir = System.getProperty("user.dir") + runDir;
			System.out.println(workingDir);*/
			
			process = Runtime.getRuntime()
					.exec("cmd /K \"java -cp " + System.getProperty("java.class.path")
							+ " cucumber.api.cli.Main -m --glue \"stepDefinition\" "+runner+".feature\"");
/*			
			process = Runtime.getRuntime()
					.exec("cmd /K \"cd " + workingDir + " && javac -cp " + System.getProperty("java.class.path") + " "
							+ runner + "Runner.java && java -cp " + System.getProperty("java.class.path")
							+ " org.junit.runner.JUnitCore " + runner + "Runner\"");
*/
			System.out.println(System.getProperty("user.dir"));
			System.out.println(System.getProperty("java.class.path"));
			
//			process = Runtime.getRuntime()
//					.exec("javac -cp " + workingDir +" "+ workingDir + "\\*.java && java -cp " + workingDir +" "+ runner + "Runner");
//			Runtime.getRuntime().exec("cmd cd " + workingDir + " && javac " + runner + "Runner.java && java "
//					+ runner + "Runner");
//			System.out.println(runDir);
		
		} 
		catch (Exception e) {
			System.out.println("Couldn't run Runner class..");
			e.printStackTrace();
		}
//		System.out.println(IOUtils.toString(process.getInputStream()));
		return process.getInputStream();
	}

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

