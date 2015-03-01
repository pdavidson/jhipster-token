--
-- Reset jhipstertoken keyspace
--

SOURCE 'drop-keyspace.cql'
SOURCE 'create-keyspace.cql'

USE jhipstertoken;

SOURCE 'create-tables.cql'
