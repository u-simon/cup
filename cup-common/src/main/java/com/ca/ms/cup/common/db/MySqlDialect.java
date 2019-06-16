/*
 * File : OracleDialect.java
 * date : Sep 18, 2012 9:54:04 AM
 */
package com.ca.ms.cup.common.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * [添加说明] <br>
 * User: yanghongfeng <br>
 * DateTime: Sep 18, 2012 9:54:04 AM <br>
 * Version 1.0
 */
public class MySqlDialect extends Dialect {

	protected static final String SQL_END_DELIMITER = ";";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.mybatis.extend.interceptor.IDialect#getLimitString(java.lang.String,
	 * int, int)
	 */
	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = trim(sql);

		StringBuffer sb = new StringBuffer(sql.length() + 20);
		sb.append(sql);
		if (offset > 0) {
			sb.append(" limit ").append(offset).append(',').append(limit)
					.append(SQL_END_DELIMITER);
		} else {
			sb.append(" limit ").append(limit).append(SQL_END_DELIMITER);
		}
		return sb.toString();
	}

	@Override
	public String getOrderString(String originalSql,
                                 LinkedHashMap<String, String> taskItems) {
		String ex = "";
		if (taskItems != null && taskItems.size() > 0) {
			Iterator<Entry<String, String>> it = taskItems.entrySet()
					.iterator();
			while (it.hasNext()) {
				Entry<String, String> entry = it.next();
				ex = ex + "," + entry.getKey() + " " + entry.getValue();
			}
		}

		if (ex.trim().length() > 0) {
			if (ex.startsWith(",")) {
				ex = ex.substring(1);
			}
			return originalSql + " order by " + ex;
		}

		return originalSql;
	}

	private String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0,
					sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}

	@Override
	public String getCountString(String sql) {

		sql = trim(sql);
		int formIndex = getAfterFormInsertPoint(sql);
		int taskIndex = getLastOrderInsertPoint(sql);
		// //如果SELECT 中包含 DISTINCT 只能在外层包含COUNT
		String select = sql.substring(0, formIndex);
		if (select.toLowerCase().indexOf("select distinct") != -1
				|| sql.toLowerCase().indexOf("group by") != -1) {
			return new StringBuffer(sql.length())
					.append("select count(1) count from (")
					.append(sql.substring(0, taskIndex)).append(" ) t")
					.toString();
		} else {
			return new StringBuffer(sql.length())
					.append("select count(1) count ")
					.append(sql.substring(formIndex, taskIndex)).toString();
		}
	}

	private static int getLastOrderInsertPoint(String querySelect) {
		int taskIndex = querySelect.toLowerCase().lastIndexOf("task by");
		if (taskIndex == -1) {
			taskIndex = querySelect.length();
		} else {
			if (!isBracketCanPartnership(querySelect.substring(taskIndex,
					querySelect.length()))) {
				throw new RuntimeException("My SQL 分页必须要有Order by 语句!");
			}
		}
		return taskIndex;
	}

	/**
	 * 得到SQL第一个正确的FROM的的插入点
	 */
	private static int getAfterFormInsertPoint(String querySelect) {
		String regex = "\\s+FROM\\s+";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(querySelect);
		while (matcher.find()) {
			int fromStartIndex = matcher.start(0);
			String text = querySelect.substring(0, fromStartIndex);
			if (isBracketCanPartnership(text)) {
				return fromStartIndex;
			}
		}
		return 0;
	}

	/**
	 * * 判断括号"()"是否匹配,并不会判断排列顺序是否正确 * * @param text 要判断的文本 * @return 如果匹配返回TRUE,
	 * 否则返回FALSE
	 */
	private static boolean isBracketCanPartnership(String text) {
		if (text == null
				|| (getIndexOfCount(text, '(') != getIndexOfCount(text, ')'))) {
			return false;
		}
		return true;
	}

	/** * 得到一个字符在另一个字符串中出现的次数 * * @param text 文本 * @param ch 字符 */
	private static int getIndexOfCount(String text, char ch) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			count = (text.charAt(i) == ch) ? count + 1 : count;
		}
		return count;
	}
}
