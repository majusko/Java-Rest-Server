package sk.rxjrest.dao.generic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.rxjrest.dao.transfer.PerzistentCount;
import sk.rxjrest.dao.transfer.PerzistentRequest;
import sk.rxjrest.dao.transfer.PerzistentResult;

public class GenericHibernateDao<T extends Serializable> {

	@Autowired
	private SessionFactory sessionFactory;
	
	private Class<T> clazz;
	 
    public GenericHibernateDao(Class<T> tClass) {
        this.clazz = tClass;
    }
	
	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public Class<T> getClazz() {
		return clazz;
	}
	
	protected final Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	protected final T listToOneResult(List<T> list) {
		
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
			
		return null;
		
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public T findOne(long id) {
		return (T) getCurrentSession().get(getClazz(), id);
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public List<T> findAll() {
		return getCurrentSession().createQuery("from " + getClazz().getName()).list();
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public Long create(T entity) {
		return (Long)getCurrentSession().save(entity);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void update(T entity) {
		getCurrentSession().update(entity);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void delete(T entity) {
		getCurrentSession().delete(entity);
	}

	@Transactional(readOnly=false, propagation=Propagation.REQUIRED)
	public void deleteById(long entityId) {
		final T entity = findOne(entityId);
		delete(entity);
	}
	
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public PerzistentResult filter(PerzistentRequest request) {
		
		final List<T> values = filterResults(request);
		final PerzistentCount allValuesCount = filterCount(request);
		
		return new PerzistentResult(values, allValuesCount);
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public List<T> filterResults(PerzistentRequest request) {
		
		List<T> results = new ArrayList<>();
		
		if(request.getPageNumber() > 0){
		
			final Criteria crit = GenericFilterBuilder.buildCriteria(getCurrentSession(), getClazz(), request);
			
			if(crit != null){
				results.addAll(crit.list());
			}
		}
		
		return results;
	}
	
	@Transactional(readOnly=true, propagation=Propagation.REQUIRED)
	public PerzistentCount filterCount(PerzistentRequest request) {
		
		Integer result = GenericFilterBuilder.buildCriteriaForCount(
				getCurrentSession(),
				getClazz(), 
				request.getConditions(),
				request.getAlias(),
				request.getFetchedAssociation());
		
		if(result == null) {
			result = 0;
		}
		
		int total = result / request.getPageLimit();
		
		if (result % 999999 != 0) {
			total = total + 1;
		}
		
		if (total == 0) {
			total = 1;
		}
		
		return new PerzistentCount(result, total);
		
	}
	
}
