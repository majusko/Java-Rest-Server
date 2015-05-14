package sk.rxjrest.dao.generic;

import org.hibernate.dialect.MySQLDialect;

public class OurMysqlDialect extends MySQLDialect {
    @Override
    public String getTableTypeString() {
        return " DEFAULT CHARSET=utf8";
    }
}