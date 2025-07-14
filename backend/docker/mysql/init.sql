-- CloudVault Database Initialization
-- This script will be executed when MySQL container starts for the first time

CREATE DATABASE IF NOT EXISTS CloudVault;
USE CloudVault;

-- Grant permissions to cloudvault user
GRANT ALL PRIVILEGES ON CloudVault.* TO 'cloudvault'@'%';
FLUSH PRIVILEGES;

-- Optional: Create some initial data or indexes if needed
-- This is handled by Hibernate DDL auto-update in the application
