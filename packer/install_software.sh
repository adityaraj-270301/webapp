#!/bin/bash

# Update the package list
sudo apt-get update -y

# Install MariaDB
sudo apt-get install mariadb-server -y

# Start the MariaDB service
sudo service mysql start

# Set a root password for MariaDB (replace 'your_password' with your desired password)
sudo mysql -u root -e "ALTER USER 'root'@'localhost' IDENTIFIED BY 'Pass1234'; FLUSH PRIVILEGES;"