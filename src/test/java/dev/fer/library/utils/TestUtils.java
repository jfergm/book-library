package dev.fer.library.utils;

import tools.jackson.databind.ObjectMapper;

public class TestUtils {
  public static String objectAsJson(final Object obj) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
