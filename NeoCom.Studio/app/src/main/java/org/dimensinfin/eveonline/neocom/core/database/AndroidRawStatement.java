package org.dimensinfin.eveonline.neocom.core.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import org.dimensinfin.eveonline.neocom.database.RawStatement;

/**
 * This is an special class to do the adaptation between the differences between the ormlite implementation on Java and on Android. For
 * example on Java the column index starts at 1 while on Android the column index starts at 0.
 */
public class AndroidRawStatement extends RawStatement {
	private Cursor cursor;

	public AndroidRawStatement( final SQLiteDatabase database, final String query, final String[] parameters ) throws
			SQLException {
		if (null != database) {
			cursor = database.rawQuery(query, parameters);
			if (null == cursor) throw new SQLException("Invalid statement when processing query: " + query);
		} else throw new SQLException("No valid connection to database to create statement. " + query);
	}

	@Override
	public boolean moveToFirst() {
		return cursor.moveToFirst();
	}

	@Override
	public boolean moveToLast() {
		return cursor.moveToLast();
	}

	@Override
	public boolean moveToNext() {
		return cursor.moveToNext();
	}

	@Override
	public boolean isFirst() {
		return cursor.isFirst();
	}

	@Override
	public boolean isLast() {
		return cursor.isLast();
	}

	@Override
	public String getString( int colindex ) {
		return cursor.getString(colindex - 1);
	}

	@Override
	public short getShort( int colindex ) {
		return cursor.getShort(colindex - 1);
	}

	@Override
	public int getInt( int colindex ) {
		return cursor.getInt(colindex - 1);
	}

	@Override
	public long getLong( int colindex ) {
		return cursor.getLong(colindex - 1);
	}

	@Override
	public float getFloat( int colindex ) {
		return cursor.getFloat(colindex - 1);
	}

	@Override
	public double getDouble( int colindex ) {
		return cursor.getDouble(colindex - 1);
	}

	public void close() {
		if (null != cursor) cursor.close();
	}
}
