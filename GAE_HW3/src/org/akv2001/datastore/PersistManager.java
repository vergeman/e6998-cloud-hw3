package org.akv2001.datastore;

import javax.persistence.EntityManager;

public class PersistManager {
	EntityManager em;

	public PersistManager() {}

	public void save(Woeid w) {

		try {
				em = EMF.get().createEntityManager();
				em.persist(w);
				em.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public Woeid get(String key) {
		Woeid w = null;
		try {
			em = EMF.get().createEntityManager();

			w = em.find(Woeid.class, key);

			em.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return w;
	}
}
