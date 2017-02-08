# rlt-automation

Selenium Grid:

java -jar lib/selenium-server-standalone-3.0.1.jar -role hub -host 192.168.86.101

[Centos 7]
- Added seleniumgrid.xml to /etc/firewalld/services
- Added service to firewall and restarted firewall
	firewall-cmd --permanent --zone=public --add-service=seleniumgrid
	systemctl stop firewalld.service
	systemctl start firewalld.service

