DROP DATABASE IF EXISTS `Acme-Comics`;
CREATE DATABASE `Acme-Comics`;

-- This creates the user, if it does not exist, which is required to
-- portably implement "DROP USER IF EXISTS".
SET @mode = @@SESSION.sql_mode;
SET SESSION sql_mode = REPLACE(REPLACE(@mode, 'NO_AUTO_CREATE_USER', ''), ',,', ',');
GRANT USAGE ON `Acme-Comics`.* TO 'acme-user'@'%' IDENTIFIED BY 'a';
GRANT USAGE ON `Acme-Comics`.* TO 'acme-manager'@'%' IDENTIFIED BY 'a';
SET SESSION sql_mode = @mode;

DROP DATABASE IF EXISTS `Acme-Comics`;
DROP USER 'acme-user'@'%';
DROP USER 'acme-manager'@'%';
