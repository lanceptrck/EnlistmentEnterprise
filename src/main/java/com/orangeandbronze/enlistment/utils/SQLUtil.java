package com.orangeandbronze.enlistment.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.orangeandbronze.enlistment.dao.DataAccessException;

public class SQLUtil {
	private final static Map<String, String> sqlCache = new HashMap<>();

	private static SQLUtil instance;

	private SQLUtil() {
	}

	public static SQLUtil getInstance() {
		if (instance == null) {
			instance = new SQLUtil();
		}
		return instance;
	}

	public String getSql(String sqlFile) {
		if (!sqlCache.containsKey(sqlFile)) {
			try (Reader reader = new BufferedReader(
					new InputStreamReader(getClass().getClassLoader().getResourceAsStream(sqlFile)))) {
				StringBuilder bldr = new StringBuilder();
				int i = 0;
				while ((i = reader.read()) > 0) {
					bldr.append((char) i);
				}
				sqlCache.put(sqlFile, bldr.toString());
				return bldr.toString();
			} catch (IOException e) {
				throw new DataAccessException("Problem while trying to read file '" + sqlFile + "' from classpath.", e);
			}
		}

		return sqlCache.get(sqlFile);
	}
}
