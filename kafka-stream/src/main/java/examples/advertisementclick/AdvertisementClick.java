/*
 * Copyright Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.advertisementclick;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;


/**
 * End-to-end integration test that demonstrates how to perform a join between two KStreams.
 * <p>
 * Note: This example uses lambda expressions and thus works with Java 8+ only.
 */
public final class AdvertisementClick {

	private static final String adImpressionsTopic = "streams-advertisementClick-input-adImpressions";
	private static final String adClicksTopic = "streams-advertisementClick-input-adClicks";
	private static final String outputTopic = "streams-advertisementClick-output-topic";

	static Properties getStreamsConfig(final String[] args) throws IOException {
		final Properties props = new Properties();
		if (args != null && args.length > 0) {
			try (final FileInputStream fis = new FileInputStream(args[0])) {
				props.load(fis);
			}
			if (args.length > 1) {
				System.out.println("Warning: Some command line arguments were ignored. This demo only accepts an optional configuration file.");
			}
		}
		props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "stream-stream-join-lambda-advertisement-click");
		props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		props.putIfAbsent(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
		props.putIfAbsent(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		props.putIfAbsent(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

		// setting offset reset to earliest so that we can re-run the demo code with the same pre-loaded data
		// Note: To re-run the demo, you need to use the offset reset tool:
		// https://cwiki.apache.org/confluence/display/KAFKA/Kafka+Streams+Application+Reset+Tool
		props.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		return props;
	}

	static void createAdvertisementClickStream(final KStream<String, String> advertisementShown, KStream<String, String> advertisementClicked) {
		// In this example, we opt to perform an OUTER JOIN between the two streams.  We picked this
		// join type to show how the Streams API will send further join updates downstream whenever,
		// for the same join key (e.g. "newspaper-advertisement"), we receive an update from either of
		// the two joined streams during the defined join window.
		final KStream<String, String> showsAndClicks = advertisementShown.join(
				advertisementClicked,
				(impressionValue, clickValue) ->
						(clickValue == null) ? impressionValue + "/not-clicked-yet" : impressionValue + "/" + clickValue,
				// KStream-KStream joins are always windowed joins, hence we must provide a join window.
				JoinWindows.of(Duration.ofSeconds(5)),
				// In this specific example, we don't need to define join serdes explicitly because the key, left value, and
				// right value are all of type String, which matches our default serdes configured for the application.  However,
				// we want to showcase the use of `StreamJoinstreams-advertisementClick-output-topiced.with(...)` in case your code needs a different type setup.
				StreamJoined.with(
						Serdes.String(), /* key */
						Serdes.String(), /* left value */
						Serdes.String()  /* right value */
				)
		);

		// Write the results to the output topic.
		showsAndClicks.to(outputTopic);
	}

	public static void main(final String[] args) throws IOException {
		//
		// Step 1: Configure and start the processor topology.
		//
		final Properties props = getStreamsConfig(args);
		final StreamsBuilder builder = new StreamsBuilder();
		final KStream<String, String> advertisementShown = builder.stream(adImpressionsTopic);
		final KStream<String, String> advertisementClicked = builder.stream(adClicksTopic);
		createAdvertisementClickStream(advertisementShown, advertisementClicked);
		final KafkaStreams streams = new KafkaStreams(builder.build(), props);
		final CountDownLatch latch = new CountDownLatch(1);

		// attach shutdown handler to catch control-c
		Runtime.getRuntime().addShutdownHook(new Thread("streams-advertisement-click-shutdown-hook") {
			@Override
			public void run() {
				streams.close();
				latch.countDown();
			}
		});

		try {
			streams.start();
			latch.await();
		} catch (final Throwable e) {
			System.exit(1);
		}
		System.exit(0);
	}
}

