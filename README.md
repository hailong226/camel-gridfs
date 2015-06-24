Camel :: Mongodb GridFS
=======================

This project is a Camel component of Mongodb GridFS.

To build this project use

    mvn install

## URL format

    gridfs:connectionBean?database=databaseName&bucket=bucket&operation=operationName[&moreOptions...]

## Endpoint options

    GridFS Endpoint support the following options, the depending on whether they are acting like a Producer or as a Consumer (options vary based on the consumer type too).

## Configuration of database in Spring XML

    The following Spring XML creates a bean defining the connection to a MongoDB instance.

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
        <bean id="mongoBean" class="com.mongodb.Mongo">
            <constructor-arg name="host" value="${mongodb.host}" />
            <constructor-arg name="port" value="${mongodb.port}" />
        </bean>
    </beans>

## Sample route

    <route>
      <from uri="direct:start" />
      <!-- using bean 'mongoBean' defined above -->
      <to uri="gridfs:mongoBean?database=${mongodb.database}&amp;operation=insert" />
      <to uri="direct:result" />
    </route>

## Mongodb GridFS operations - producer endpoints

### Query operations Example

#### findById

    from("direct:findById")
            .to("gridfs:mongoBean?database={{mongodb.database}}&operation=findById")
            .to("mock:resultFindById");

    Headers:
            GridFSConstants.FILE_ID－ (ObjectId: String)

### Create/update operations

#### insert

    from("direct:insert")
        .to("gridfs:mongoBean?database={{mongodb.database}}&operation=insert");

    Insert with InputStream
    Body:
        InputStream Object
    
    Insert with byte array
    Body:
        byte[] Object
    
    Insert with File
    Body:
        File Object
    
    Insert with Path
    Header:
        Exchange.FILE_PATH - file path in local system.
        
    Normal Headers:
        Exchange.FILE_CONTENT_TYPE - file content type, default file content-type or application/octet-stream
        Exchange.FILE_NAME - file name
        GridFSConstants.FILE_CHUNK_SIZE - file chunk size, default use mongodb chunk size.

### Delete operations

#### remove

    from("direct:remove")
                .to("gridfs:mongoBean?database={{mongodb.database}}&operation=remove")
                .to("mock:resultFindById");
    
    Headers:
        GridFSConstants.FILE_ID－ (ObjectId: String)
