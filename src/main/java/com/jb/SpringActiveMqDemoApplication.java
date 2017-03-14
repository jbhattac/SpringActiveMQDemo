package com.jb;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import ch.qos.logback.classic.Logger;

@SpringBootApplication
public class SpringActiveMqDemoApplication implements CommandLineRunner {

	@Autowired
	MessageSender messageSender;

	static final Logger LOG = (Logger) LoggerFactory.getLogger(SpringActiveMqDemoApplication.class);
	@Override
	public void run(String... arg0) throws Exception {
		System.out.println("Sending a message.");

		String message ="Hello All";

		LOG.info("Application : sending order request {}", message);
		messageSender.sendMessage(message);
		LOG.info("+++++++++++++++++++++++++++++++++++++++++++++++++++++");

	}
	public static void main(String[] args) throws IOException {
		//InetAddress locIP = InetAddress.getByName("localhost");
		//ServerSocket s= new ServerSocket(61616, 0, locIP);
		//s.accept();

		
		SpringApplication.run(SpringActiveMqDemoApplication.class, args);
		
	}
	
	
	
}
