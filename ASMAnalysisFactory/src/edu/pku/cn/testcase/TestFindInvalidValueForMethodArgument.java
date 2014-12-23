/*
 * PKU Confidential
 * 
 * OCO Source Materials
 * 
 * PKU Software Lab
 * @author Administrator
 * @time 2008-12-24 ����03:38:46
 * @modifier: Administrator
 * @time 2008-12-24 ����03:38:46
 * @reviewer: Administrator
 * @time 2008-12-24 ����03:38:46
 * (C) Copyright PKU Software Lab. 2008
 * 
 * The source code for this program is not published or otherwise divested of
 * its trade secrets.
 * 
 */
package edu.pku.cn.testcase;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class TestFindInvalidValueForMethodArgument {

	public static void main(String[] args) {
		// Connection connection = null;

		try {
			// connection = DriverManager.getConnection("dddd");
			// Statement stmt = connection.createStatement ( ) ;
			//		         
			// ResultSet rs = stmt.executeQuery (
			// "SELECT col_blob FROM mysql_all_table" ) ;
			// PreparedStatement prestmt =
			// connection.prepareStatement("insert into tb_name(col1, col2, col3, col4)values(?,?,?,?)");

			String ss = "This is a test for substring";
			int m = 0;
			int p = 0;
			// int n = m + p;

			for (int i = 0; i < 23; i++) {
				if (i == 22)
					m = i;
			}

			String ss2 = ss.substring(m);
			String ss3 = ss.substring(p);
			// if ( rs.next ( ) ) {
			// Get the BLOB from the result set
			// Array array = rs.getArray(0);
			// InputStream ascii = rs.getAsciiStream(m);
			// BigDecimal bigDecimal = rs.getBigDecimal(n);
			// InputStream binaryStream = rs.getBinaryStream(p);
			// Blob blob = rs.getBlob(n);
			// boolean bool = rs.getBoolean(m);
			// byte byte2 = rs.getByte(n);
			// byte[] bytes = rs.getBytes(p);
			// Reader reader = rs.getCharacterStream(m);
			// Clob clob = rs.getClob(n);
			// Date date = rs.getDate(n);
			// Date date2 = rs.getDate(p, new GregorianCalendar());
			// double double2 = rs.getDouble(m);
			// float float2 = rs.getFloat(n);
			// int int3 = rs.getInt(p);
			// long long2 = rs.getLong(m);
			// Object object = rs.getObject(n);
			// Map<String, Class<?>>map = new HashMap<String, Class<?>>();
			// Object object2 = rs.getObject(p, map);
			// Ref ref = rs.getRef(p);
			// short short2 = rs.getShort(m);
			// String string = rs.getString(n);
			// Time time = rs.getTime(p);
			// Time time2 = rs.getTime(m, new GregorianCalendar());
			// Timestamp timestamp = rs.getTimestamp(n);
			// Timestamp timestamp2 = rs.getTimestamp(p, new
			// GregorianCalendar());
			// URL url = rs.getURL(n);
			//		             
			// rs.updateArray(m, array);
			// rs.updateAsciiStream(n, ascii);
			// rs.updateAsciiStream(n, ascii, n);
			// rs.updateAsciiStream(n, ascii, (long)n);
			// rs.updateBigDecimal(p, bigDecimal);
			// rs.updateBinaryStream(m, binaryStream, m);
			// rs.updateBinaryStream(m, binaryStream, (long)m);
			// rs.updateBinaryStream(m, binaryStream);
			// rs.updateBlob(n, blob);
			// rs.updateBoolean(m, bool);
			// rs.updateByte(m, byte2);
			// rs.updateBytes(m, bytes);
			// rs.updateCharacterStream(m, reader);
			// rs.updateCharacterStream(m, reader, m);
			// rs.updateCharacterStream(m, reader, (long)m);
			// rs.updateClob(m, clob);
			// rs.updateDate(m, (java.sql.Date) date);
			// rs.updateDouble(m, double2);
			// rs.updateFloat(m, float2);
			// rs.updateInt(m, int3);
			// rs.updateLong(m, long2);
			// rs.updateNull(0);
			// rs.updateObject(m, object);
			// rs.updateObject(m, object, m);
			// rs.updateRef(n, ref);
			// rs.updateShort(m, short2);
			// rs.updateString(m, string);
			// rs.updateTime(m, time);
			// rs.updateTimestamp(m, timestamp);
			//		             
			// int length = 5;
			// prestmt.setArray(m, array);
			// prestmt.setAsciiStream(n, ascii);
			// prestmt.setAsciiStream(m, ascii, m);
			// prestmt.setAsciiStream(n, ascii, (long)m);
			// prestmt.setBigDecimal(n, bigDecimal);
			// prestmt.setBinaryStream(m, binaryStream);
			// prestmt.setBinaryStream(m, binaryStream, m);
			// prestmt.setBinaryStream(m, binaryStream, (long)n);
			// prestmt.setBlob(m, blob);
			// prestmt.setBoolean(m, bool);
			// prestmt.setByte(n, byte2);
			// prestmt.setBytes(m, bytes);
			// prestmt.setCharacterStream(m, reader);
			// prestmt.setCharacterStream(m, reader, m);
			// prestmt.setCharacterStream(m, reader, (long)length);
			// prestmt.setClob(n, clob);
			// prestmt.setClob(m, reader);
			// prestmt.setClob(n, reader, (long)length);
			// prestmt.setDate(m, (java.sql.Date) date);
			// prestmt.setDate(m, (java.sql.Date)date, new GregorianCalendar());
			// prestmt.setDouble(m, double2);
			// prestmt.setFloat(m, float2);
			// prestmt.setInt(n, int3);
			// prestmt.setLong(m, (long)m);
			// prestmt.setNull(m, n);
			// prestmt.setNull(m, m, "Name");
			// prestmt.setObject(m, object);
			// prestmt.setObject(m, object, m);
			// prestmt.setObject(m, object,m, m);
			// prestmt.setRef(m, ref);
			// prestmt.setShort(m, short2);
			// prestmt.setString(m, string);
			// prestmt.setTime(m, time);
			// prestmt.setTime(m, time, new GregorianCalendar());
			// prestmt.setTimestamp(m, timestamp);
			// prestmt.setTimestamp(m, timestamp, new GregorianCalendar());
			// prestmt.setURL(m, url);
			// }
		} catch (Exception e) {
		}
	}
}

// end

