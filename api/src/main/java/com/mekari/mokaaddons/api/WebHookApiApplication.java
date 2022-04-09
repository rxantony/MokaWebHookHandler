package com.mekari.mokaaddons.api;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekari.mokaaddons.api.controller.WebhookController;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.ResourceUtils;

@SpringBootApplication(scanBasePackages = {"com.mekari.mokaaddons.*"})
public class WebHookApiApplication implements CommandLineRunner {

	private @Autowired WebhookController webhookController;
	private @Autowired ObjectMapper mapper;

	public static void main(String[] args) {
		SpringApplication.run(WebHookApiApplication.class, args);
	}

	@Override
	public void run(String... arg0) throws Exception {
		var maxTrhread = 20;
		if (arg0.length > 0)
			maxTrhread = Integer.parseInt(arg0[0]);

		var events = getItemEvents();
		var d = events.length / maxTrhread;
		var m = events.length % maxTrhread;

		for (var i = 0; i < (d * maxTrhread + m); i += maxTrhread) {
			var threadCount = i + (maxTrhread) > events.length ? m != 0 ? m : d : maxTrhread;
			var threads = new Thread[threadCount];

			for (var x = 0; x < threadCount; x++) {
				var event = events[i + x];
				Runnable r = () -> {
					try {
						//webhookController.handle(event.toString());
						webhookController.feedProduct(event.toString());
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				};
				threads[x] = new Thread(r);
			}

			for (var x = 0; x < threadCount; x++)
				threads[x].start();

			for (var x = 0; x < threadCount; x++)
				threads[x].join();
		}
	}

	private JsonNode[] getItemEvents() throws IOException {
		var file = ResourceUtils.getFile("classpath:item_event_varies.json");
		try (var in = new FileInputStream(file);) {
			var writer = new StringWriter();
			IOUtils.copy(in, writer, "UTF-8");
			var eventNodes = mapper.readTree(writer.toString());
			return StreamSupport
					.stream(Spliterators.spliteratorUnknownSize(eventNodes.iterator(), 0), false)
					.toArray(JsonNode[]::new);
		}
	}
}
