#!/bin/bash

cd `dirname $0`

java -jar lib/selenium/3.0.1/selenium-server-standalone-3.0.1.jar -role hub -host 192.168.86.101
