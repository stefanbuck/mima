package ssol.tools.mima.ui

import ssol.tools.mima.{MiMaLib, Config, Settings, PathResolver}
import scala.io.Source
import scala.tools.nsc.util._

class CollectProblemsTest  {
  
  def runTest(testName: String, oldJarPath: String, newJarPath: String, oraclePath: String): Unit = {
    // load test setup
    Config.setup("scala ssol.tools.misco.MiMaLibUI <old-dir> <new-dir>", Array(oldJarPath, newJarPath))
    var scalaLib = "/Applications/dev-tools/eclipse/configuration/org.eclipse.osgi/bundles/390/1/.cp/lib/scala-library.jar"
    val cp = scalaLib :: ClassPath.split(Config.baseClassPath.asClasspathString)
    val cpString = ClassPath.join(cp : _*)
    Config.baseClassPath = new JavaClassPath(ClassPath.DefaultJavaContext.classesInPath(cpString), ClassPath.DefaultJavaContext)    
        
    val mima = new MiMaLib
    
    // SUT
    val problems = mima.collectProblems(oldJarPath, newJarPath).map(_.description)
    
    // compare result against oracle
    var expectedProblems = Source.fromFile(oraclePath).getLines.toList
    
    val unexpectedProblems = problems -- expectedProblems
    val unreportedProblem = expectedProblems -- problems
    
    val mess = new StringBuilder
    
    if(!unexpectedProblems.isEmpty)
      mess ++= "\tThe following problems were not expected\n" + unexpectedProblems.mkString("\t- ", "\n","")
    
    if(!mess.isEmpty) mess ++= "\n\n" 
    
    if(!unreportedProblem.isEmpty)
      mess ++=  "\tThe following expected problems were not reported\n" + unreportedProblem.mkString("\t- ", "\n","\n")
      
    if (!mess.isEmpty) 
      println("[error] Test '" + testName + "' failed.\n" + mess)
    else 
      println("[info] Test '" + testName + "' succeeded.")
  }
}