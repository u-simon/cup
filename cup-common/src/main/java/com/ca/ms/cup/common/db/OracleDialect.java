
package com.ca.ms.cup.common.db;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class OracleDialect extends Dialect {

	/* (non-Javadoc)  
	 * @see org.mybatis.extend.interceptor.IDialect#getLimitString(java.lang.String, int, int)  
	 */
	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = sql.trim();
		StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);
		pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		pagingSelect.append(sql);
		pagingSelect.append(" ) row_ ) where rownum_ > ").append(offset).append(" and rownum_ <= ").append(offset + limit);
		return pagingSelect.toString();
	}

	@Override
	public String getOrderString(String originalSql, LinkedHashMap<String, String> taskItems) {
		String ex = "";
		if(taskItems!=null&&taskItems.size()>0){
			Iterator<Entry<String,String>> it = taskItems.entrySet().iterator();
			while(it.hasNext()){
				Entry<String,String> entry = it.next();
				ex = ex + "," + entry.getKey() + " " + entry.getValue();
			}
		}
		
		if(ex.trim().length()>0){
			if(ex.startsWith(",")){
				ex = ex.substring(1);
			}
			return originalSql + " task by " + ex;
		}
		
		return originalSql;
	}

	@Override
	public String getCountString(String sql) {
		// TODO Auto-generated method stub
		return null;
	}
}
