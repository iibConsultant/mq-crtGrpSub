package com.ibm.sample;

import java.util.Hashtable;

import com.ibm.mq.MQEnvironment;
import com.ibm.mq.MQException;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.MQTopic;
import com.ibm.mq.constants.CMQC;

/**
 * Java class to create a DURABLE subscription that will remove duplicate subscription messages from overlapping subscriptions.
 * Each overlapping subscription MUST define the same group name and must use the same destination queue.
 */
public class CreateGroupingSubscription {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		MQQueueManager QM = null;
		MQQueue destinationQ = null;
		MQTopic subscriber = null;
		
		if (args.length != 6) {
			System.out.println("Usage: CreateGroupingSubscription SUB_NAME QM_NAME TOP_OBJ TOP_STR Q_NAME GRP_NAME");
		}
		String subscriptionName = args[0];

		String QMName = args[1];
		String topicObject = args[2];
		String topicString = args[3]; 
		int options = CMQC.MQSO_CREATE | CMQC.MQSO_FAIL_IF_QUIESCING | CMQC.MQSO_DURABLE | CMQC.MQSO_GROUP_SUB | CMQC.MQSO_SET_CORREL_ID;
		String queueName = args[4];
		String groupName = args[5];
		
		Hashtable<java.lang.String,java.lang.Object> parameters = new Hashtable<java.lang.String,java.lang.Object>();
		parameters.put(CMQC.MQSUB_PROP_SUBSCRIPTION_CORRELATION_ID, groupName);

		// connect to the queue manager in bindings mode.
		MQEnvironment.properties.put(CMQC.TRANSPORT_PROPERTY,
				CMQC.TRANSPORT_MQSERIES_BINDINGS);

		try {
			// connect to the queue manager
			QM = new MQQueueManager(QMName);
			
			// access the destination queue - this should already exist
			destinationQ = QM.accessQueue(queueName, CMQC.MQOO_OUTPUT);
			
			// create the subscription with the correct attributes
			subscriber = QM.accessTopic(destinationQ, topicString, topicObject, options, null, subscriptionName, parameters);

		} catch (MQException e) {
			e.printStackTrace();
		} finally {
			if (QM != null) {
				try {
					QM.disconnect();
				} catch (MQException e) {
					e.printStackTrace();
				}
			}
			if (destinationQ != null) {
				try {
					destinationQ.close();
				} catch (MQException e) {
					e.printStackTrace();
				}
			}
			if (subscriber != null) {
				try {
					subscriber.close();
				} catch (MQException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
