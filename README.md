# mq-crtGrpSub
This small java program creates a subscription, in a group, to eliminate duplicate subscription messages from overlapping subscriptions.

# Usage
This java application needs to be called on the same OS where the queue manager is located.  It can be called like:
- java -Djava.library.path=/opt/mqm/java/lib64 -cp .:/opt/mqm/java/lib/com.ibm.mq.jar:/opt/mqm/java/lib/com.ibm.mq.jmqi.jar com.ibm.sample.CreateGroupingSubscription TESTSUB99 TEST "" "/string/fred" T1 GRPNAME2

The parameters are all mandatory:
- the name of the subscription
- the name of the queue manager (connection is in bindings mode)
- the name of the TOPIC OBJECT if referring to one, otherwise use ""
- the name of the TOPIC string if referring to one, otherwise use ""
- the name of the destination queue which should already exist
- the name of the GROUP in which all subscriptions will have duplicates removed, the same GROUP name should be used for all possibly overlapping subscriptions.  Can only be up to 24 characters.

# Test
1. To test, create a queue into which the subscription messages should go 
    - DEFINE QL(T2)
2. Create 2 overlapping subscriptions using the java application:
    - java -Djava.library.path=/opt/mqm/java/lib64 -cp .:/opt/mqm/java/lib/com.ibm.mq.jar:/opt/mqm/java/lib/com.ibm.mq.jmqi.jar com.ibm.sample.CreateGroupingSubscription TESTSUB1 TEST "" "/ALIAS/#" T2 GROUPNAME
    - java -Djava.library.path=/opt/mqm/java/lib64 -cp .:/opt/mqm/java/lib/com.ibm.mq.jar:/opt/mqm/java/lib/com.ibm.mq.jmqi.jar com.ibm.sample.CreateGroupingSubscription TESTSUB2 TEST "" "/ALIAS/A1" T2 GROUPNAME
3. Now create a publication to topic /ALIAS/A1 and you will only get 1 subscription message 
