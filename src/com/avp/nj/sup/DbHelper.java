package com.avp.nj.sup;


import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.afc.cmn.DbUtil;
import com.afc.cmn.exceptions.AfcDBAccessException;

public class DbHelper {
	
	public static void  init() throws AfcDBAccessException {
		DbUtil.init();
	}
	
	public static void close() {
		DbUtil.close();
	}
	
    public static Session getCurrentSession() throws AfcDBAccessException {
    	return DbUtil.getCurrentSession();
    }

    public static void closeCurrentSession() {
    	DbUtil.closeCurrentSession();
    }
    
    public static void save(Object object) throws AfcDBAccessException {
    	try {
    		DbUtil.getCurrentSession().save(object);
    	} catch (HibernateException e) {
    		throw new AfcDBAccessException(e.getMessage(), e);
    	}
    }
}



