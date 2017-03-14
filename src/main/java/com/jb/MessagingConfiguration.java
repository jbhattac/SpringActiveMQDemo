package com.jb;

import java.util.Arrays;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.network.jms.JmsConnector;
import org.apache.activemq.network.jms.JmsQueueConnector;
import org.apache.activemq.network.jms.OutboundQueueBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.SimpleMessageConverter;

@Configuration
public class MessagingConfiguration {

	private static final String LOCAL_BROKER_URL = "tcp://localhost:7171";
	private static final String REMOTE_BROKER_URL = "tcp://10.0.0.6:61616";

	private static final String ORDER_QUEUE = "order-queue";


	@Bean
	public ConnectionFactory connectionFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(LOCAL_BROKER_URL);
		connectionFactory.setTrustedPackages(Arrays.asList("com.jb"));
		return connectionFactory;
	}
	
	@Bean(name="remoteFactory")
	public QueueConnectionFactory remoteFactory() {
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
		connectionFactory.setBrokerURL(REMOTE_BROKER_URL);
		connectionFactory.setTrustedPackages(Arrays.asList("com.jb"));
		return connectionFactory;
	}

	@Bean
	public BrokerService getLocalBroker() throws Exception {
		BrokerService broker = new BrokerService();
		
		broker.setBrokerName("localBroker");
		broker.setTransportConnectorURIs(new String[]{LOCAL_BROKER_URL});
		broker.setPersistent(false);
		broker.setJmsBridgeConnectors(new JmsConnector[]{getJmsQueueConnector()});
		broker.start();
		return broker;
	}
	
	@Bean
	public JmsQueueConnector getJmsQueueConnector() throws Exception {
		JmsQueueConnector jmsConn = new JmsQueueConnector();
		jmsConn.setOutboundQueueConnectionFactory(remoteFactory());
		jmsConn.setOutboundQueueBridges(new OutboundQueueBridge[]{new OutboundQueueBridge(ORDER_QUEUE)});
		return jmsConn;
	}
	
	
	/*	@Bean
	public BrokerService getRemoteBroker() throws Exception {
		BrokerService broker = new BrokerService();
		broker.setBrokerName("bridgeBroker");
		broker.addConnector(REMOTE_BROKER_URL);
		broker.setPersistent(false);
		broker.setJmsBridgeConnectors(new JmsConnector[]{getJmsQueueConnector()});
		//broker.start();
		return broker;
	}

	
	 * Message listener container, used for invoking messageReceiver.onMessage
	 * on message reception.
	 * 
	 * @Bean public MessageListenerContainer getContainer(){
	 * DefaultMessageListenerContainer container = new
	 * DefaultMessageListenerContainer();
	 * container.setConnectionFactory(connectionFactory());
	 * container.setDestinationName(ORDER_RESPONSE_QUEUE);
	 * container.setMessageListener(messageReceiver); return container; }
	 */

	/*
	 * Used for Sending Messages.
	 */
	@Bean
	public JmsTemplate jmsTemplate() {
		JmsTemplate template = new JmsTemplate();
		template.setConnectionFactory(connectionFactory());
		template.setDefaultDestinationName(ORDER_QUEUE);
		return template;
	}

	@Bean
	MessageConverter converter() {
		return new SimpleMessageConverter();
	}

}