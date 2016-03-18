package com.avp.nj.sup.util;


import avp.clients.rtdb.Avpdb;
import avp.clients.rtdb.AvpdbPool;
import avp.clients.rtdb.AvpdbPoolConfig;

public class RTDBOperation {
	
	public void Demo() {
		Avpdb db = null;
		try {
			db = RTDBOperation.getInstance().getResource();
			// TODO: read data from RTDB with "db"
		} catch (Exception e) {
			// do exception handle.
		} finally {
			RTDBOperation.getInstance().releaseResource(db);
		}
	}
	
	public Avpdb getResource(){
		Avpdb db = null;
		try {
			if(initPool()) db = pool.getResource();
		} catch (Exception e) {
			e.printStackTrace();
			LogTracer.logError("getResource error; "+e.getMessage());
		}
		return db;
	}
	
	public void releaseResource(Avpdb db){
		try {
			if(db == null) return;
			if(pool != null) pool.returnResource(db);
		} catch (Exception e) {
			e.printStackTrace();
			LogTracer.logError("releaseResource error; "+e.getMessage());
		}
	}
		
	public boolean init(String ip, int port){
		try{
			this.ip = ip;
			this.port = port;
			return initPool();
		}catch(Exception e){
			e.printStackTrace();
			LogTracer.logError("init error; "+e.getMessage());
		}
		
		return false;
	}
	
	public static RTDBOperation getInstance(){
		return instance;
	}
	
	private boolean initPool(){
		boolean bOK = false;
		try{
			if(pool == null){
				if(ip != null && port > 0) {
					AvpdbPoolConfig config = new AvpdbPoolConfig();
					config.setMaxActive(100);
					config.setMaxIdle(20);
					config.setMaxWait(1000);
					pool = new AvpdbPool(config, ip, port);
					bOK = true;
				}
			} else {
				bOK = true;
			}
		}catch(Exception e){
			bOK = false;
			pool = null;
			e.printStackTrace();
			LogTracer.logError("initPool error; "+e.getMessage());
		}
		
		return bOK;
	}
	
	private RTDBOperation(){
	}
	
	private AvpdbPool pool = null;
	private String ip = null;
	private int port = 0;
	private static RTDBOperation instance = new RTDBOperation();
}

