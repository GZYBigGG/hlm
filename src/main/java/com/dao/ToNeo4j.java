package com.dao;

import static org.neo4j.driver.v1.Values.parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Value;
import org.neo4j.driver.v1.types.Node;
import org.neo4j.driver.v1.types.Path;
import org.neo4j.driver.v1.types.Relationship;
import org.springframework.stereotype.Service;

@Service
public class ToNeo4j {
	Driver driver;		
	public ToNeo4j() {
		try {
			driver = GraphDatabase.driver("bolt://123.56.170.16:7687", AuthTokens.basic("neo4j", "123456"));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("连接失败");
		}	
		
	}
	
    @SuppressWarnings("unchecked")   
	public Map toMap(String name1,String name2) {
    	Map<String, Object> resultMap = new HashMap<>();
    	Session session = driver.session();
    	StatementResult result;
    	if(Objects.equals(name1, "all") && Objects.equals(name2, "all")) {
    		result = session.run("");
    				//"MATCH p=(n:Person)--() RETURN p");
    	}else {
    		result = session.run(
    				"MATCH p=shortestpath((n:Person)-[*..6]-(m:Person)) where n.name = {x} and m.name = {y} RETURN p",
    				parameters("x", name1,"y",name2));
    	}
    	Set<Long> s = new HashSet<Long>();
    	List<Object> nodeList = new ArrayList<>();
    	List<Object> linkList = new ArrayList<>();
    	while(result.hasNext()) {
    		Record record = result.next();
    		
    		//System.out.println(record);
    		List<Value> list = record.values();
    		//System.out.println(list);
    		for(Value v : list)
    		{
    			Path p = v.asPath();
    			//System.out.println(p);
    			for(Node n:p.nodes())
    			{	
    				
    				//nodes.append("{");
    				
    				if(!s.contains(n.id())) {
    					s.add(n.id());
    					Map subMap = new HashMap<>(n.asMap());
    					subMap.put("id", n.id());
    					nodeList.add(subMap);
    				}
    			}
    			
    			for(Relationship r:p.relationships())
    			{
    				Map<String,Object> subMap = new HashMap<>();
    				subMap.put("source", r.startNodeId());
    				subMap.put("target", r.endNodeId());
    				subMap.put("type", r.type());
    				linkList.add(subMap);
    			}
    			
    		}
    	}
    	resultMap.put("nodes", nodeList);
    	resultMap.put("links", linkList);
    	return resultMap;
    }
    @SuppressWarnings("unchecked")   
	public Map toAllMap() {
    	Map<String, Object> resultMap = new HashMap<>();
    	Session session = driver.session();
    	StatementResult result;

   		result = session.run(
    				"MATCH p=(n:Person)--() RETURN p");

    	Set<Long> s = new HashSet<Long>();
    	List<Object> nodeList = new ArrayList<>();
    	List<Object> linkList = new ArrayList<>();
    	while(result.hasNext()) {
    		Record record = result.next();
    		
    		//System.out.println(record);
    		List<Value> list = record.values();
    		//System.out.println(list);
    		for(Value v : list)
    		{
    			Path p = v.asPath();
    			//System.out.println(p);
    			for(Node n:p.nodes())
    			{	
    				
    				//nodes.append("{");
    				
    				if(!s.contains(n.id())) {
    					s.add(n.id());
    					Map subMap = new HashMap<>(n.asMap());
    					subMap.put("id", n.id());
    					nodeList.add(subMap);
    				}
    			}
    			
    			for(Relationship r:p.relationships())
    			{
    				Map<String,Object> subMap = new HashMap<>();
    				subMap.put("source", r.startNodeId());
    				subMap.put("target", r.endNodeId());
    				subMap.put("type", r.type());
    				linkList.add(subMap);
    			}
    			
    		}
    	}
    	resultMap.put("nodes", nodeList);
    	resultMap.put("links", linkList);
    	return resultMap;
    }
}
