package com.example.cmiss.config;

import cma.cimiss.client.DataQueryClient;
import org.springframework.stereotype.Component;


@Component
public class CimissDataQueryClientBean extends DataQueryClient{

	public CimissDataQueryClientBean(){
		try {
			initResources();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
