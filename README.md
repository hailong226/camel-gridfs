Camel :: Mongodb GridFS
=======================

This project is a Camel component of Mongodb GridFS.

To build this project use

    mvn install

## URL format

    gridfs:connectionBean?database=databaseName&bucket=bucket&operation=operationName[&moreOptions...]

## Endpoint options

## Configuration of database in Spring XML

## Sample route
    下面的定义是使用Spring XML的格式定义的

    <route>
      <from uri="direct:start" />
      <!-- using bean 'mongoBean' defined above -->
      <to uri="gridfs:mongoBean?database=${mongodb.database}&amp;operation=insert" />
      <to uri="direct:result" />
    </route>

## Mongodb GridFS operations - producer endpoints

### Query operations

#### findById

### Create/update operations

#### insert

### Delete operations

#### remove