package sk.rxjrest.dao;

import org.springframework.stereotype.Repository;

import sk.rxjrest.dao.generic.GenericHibernateDao;
import sk.rxjrest.domain.visit.Visit;

@Repository
public class VisitDao extends GenericHibernateDao<Visit> {

	public VisitDao() {
        super(Visit.class);
    }

}
