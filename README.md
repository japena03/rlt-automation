# rlt-automation

Selenium Grid:

[Centos 7]
- Added seleniumgrid.xml to /etc/firewalld/services
- Added service to firewall and restarted firewall
	firewall-cmd --permanent --zone=public --add-service=seleniumgrid
	systemctl stop firewalld.service
	systemctl start firewalld.service


java -jar lib/selenium-server-standalone-3.0.1.jar -role hub -host 192.168.86.101

The console can then be accessed at 
http://192.168.86.101:4444/grid/console

Selenium Node:

java -Dwebdriver.gecko.driver.exe -jar lib\selenium-server-standalone-3.0.1.jar -role node -nodeConfig config\nodeConfig.json -host 192.168.86.100