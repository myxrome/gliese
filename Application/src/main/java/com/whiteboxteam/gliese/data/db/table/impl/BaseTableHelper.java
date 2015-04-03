package com.whiteboxteam.gliese.data.db.table.impl;

import com.whiteboxteam.gliese.data.db.table.TableHelper;

/**
 * Gliese Project.
 * User: Aleksey Zolotov
 * Date: 17.10.13
 * Time: 11:06
 */
public abstract class BaseTableHelper implements TableHelper {

    protected static final String CREATE_TABLE = "CREATE TABLE ";
    protected static final String COLUMNS_BEGIN = " ( ";
    protected static final String INTEGER_PK_TYPE = " INTEGER PRIMARY KEY ";
    protected static final String AUTOINCREMENT = " AUTOINCREMENT ";
    protected static final String INTEGER_TYPE = " INTEGER ";
    protected static final String TEXT_TYPE = " TEXT ";
    protected static final String COLUMNS_SEPARATOR = " , ";
    protected static final String COLUMNS_END = " ) ";
    protected static final String CREATE_INDEX = " CREATE INDEX ";
    protected static final String INDEX_SUFFIX = "_idx_";
    protected static final String INDEX_FOR_TABLE = " ON ";
    protected static final String FOREIGN_KEY = " FOREIGN KEY ";
    protected static final String REFERENCES = " REFERENCES ";
    protected static final String ON_DELETE_CASCADE = " ON DELETE CASCADE ";

}
