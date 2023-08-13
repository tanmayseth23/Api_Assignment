# Api_Assignment

Steps for running the framework.

1. Download the project code
2. Load the maven script (The IDE will load or provide a popup to load the same)
3. Install allure following the link: https://docs.qameta.io/allure/#_installing_a_commandline
For Mac users use: 
brew install allure
4. Verify allure is installed with : allure --version
5. Kindly check the java versions in pom.xml before running the project
6. Run the Tests through testng.xml by right clicking it

Right click on the suite xml and trigger with Run as ->testng suite

OR

Go to Project->Properties->Testng-> Set this xml as your template xml and then you can run as testng test
7. once the tests are run successfully ,run the below command
allure serve allure-results
8. The report should Auto open using the browser or use allure open command
