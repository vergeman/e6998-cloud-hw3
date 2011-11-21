package org.akv2001.datastore;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;


/*
 * queries for all, top 5 hottest, coldest, oldest
 * we convert to ArrayLists since the List implementation in nucleus
 * is not serializable (and hence, not cacheable) 
 * 
 */
public class QueryManager {
	
	EntityManager em;
	
	public QueryManager () {
		em = EMF.get().createEntityManager();
	}
	
	public ArrayList<Woeid> getHottest(int num) {
		String max_temp = "SELECT a FROM Woeid a WHERE a.temp IS NOT NULL ORDER BY temp DESC";
		Query max_temp_qry = em.createQuery(max_temp);
		max_temp_qry.setMaxResults(num);
		@SuppressWarnings("unchecked")
		ArrayList<Woeid> max_temps = new ArrayList<Woeid>( (List<Woeid>) max_temp_qry.getResultList() );
		return max_temps;
	}
	
	public ArrayList<Woeid> getColdest(int num) {
		String min_temp = "SELECT a FROM Woeid a WHERE a.temp IS NOT NULL ORDER BY temp ASC";
		Query min_temp_qry = em.createQuery(min_temp);
		min_temp_qry.setMaxResults(num);
		@SuppressWarnings("unchecked")
		ArrayList<Woeid> min_temps = new ArrayList<Woeid> ( (List<Woeid>) min_temp_qry.getResultList() );
		return min_temps;
	}

	public ArrayList<Woeid> getAll() {
		String all = "SELECT a FROM Woeid a WHERE a.city IS NOT NULL ORDER BY city";
		Query all_qry = em.createQuery(all);
		@SuppressWarnings("unchecked")
		ArrayList<Woeid> all_res = new ArrayList<Woeid>( (List<Woeid>) all_qry.getResultList() );
		return all_res;
	}

	public Woeid getOldest() {
		String old = "SELECT a FROM Woeid a WHERE a.timestamp IS NOT NULL ORDER BY timestamp";
		Query old_qry = em.createQuery(old);
		old_qry.setMaxResults(1);
		@SuppressWarnings("unchecked")
		List<Woeid> old_res = (List<Woeid>) old_qry.getResultList();
		
		if (old_res.isEmpty()) {
			return null;
		}
		return old_res.get(0);
	}
}
