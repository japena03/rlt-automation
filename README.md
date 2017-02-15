# rlt-automation

This is a setup for running tests from a selenium grid so that multiple nodes running various browsers can execute tests in parallel. 

Selenium Grid:

[Centos 7]
- Added seleniumgrid.xml to /etc/firewalld/services
- Added service to firewall and restarted firewall
	firewall-cmd --permanent --zone=public --add-service=seleniumgrid
	systemctl stop firewalld.service
	systemctl start firewalld.service


java -jar /home/alex/Work/rlt/automation/lib/selenium/3.0.1/selenium-server-standalone-3.0.1.jar -role hub -host 192.168.86.101

The console can then be accessed at 
http://192.168.86.101:4444/grid/console

Selenium Node:

(Note that the Dwebdriver flag must come before the jar parameter)

java -Dwebdriver.gecko.driver="D:\Git\rlt-automation\config\geckodriver.exe" -jar D:\Git\rlt-automation\lib\selenium\3.0.1\selenium-server-standalone-3.0.1.jar -role node -nodeConfig D:\Git\rlt-automation\config\nodeConfig.json -host 192.168.86.100

[Extent Reports]
The file Extent.html is produced under test-output directory with all test results. A web server may be installed and pointed to that file to access the results
over a browser.

[Running tests locally]
To run tests locally in the BaseTest class the following 2 lines should be commented it in:

	System.setProperty("webdriver.gecko.driver", "D:\\Git\\rlt-automation\\config\\geckodriver.exe");
	driver = new FirefoxDriver();
	

and the following line should be commented it out:

	driver = new RemoteWebDriver(new URL(hubUrl), capability);
	
before submitting make sure to revert this change otherwise tests will not run from the grid.

When debugging locally the node should not be running. 