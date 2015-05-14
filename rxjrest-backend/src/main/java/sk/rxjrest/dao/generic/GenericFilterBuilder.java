package sk.rxjrest.dao.generic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import sk.rxjrest.dao.generic.common.FieldType;
import sk.rxjrest.dao.generic.common.FilterOperator;
import sk.rxjrest.dao.transfer.PerzistentCondition;
import sk.rxjrest.dao.transfer.PerzistentRequest;

public class GenericFilterBuilder {

	@SuppressWarnings("rawtypes")
	public static Criteria buildCriteria(Session session, Class queriedClass,
			PerzistentRequest request) {

		Criteria idCrit = buildGenericCriteriaForIds(session, queriedClass,
				request.getConditions(), request.getAlias());
		String firstSortPropertyColumn = null;

		final Map<String, Boolean> sortProperties = request.getSortColumns();

		try {
			Entry<String, Boolean> firstSortPropertyEntry = request
					.getSortColumns().entrySet().iterator().next();
			firstSortPropertyColumn = firstSortPropertyEntry.getKey();
		} catch (Exception ignore) {
			sortProperties.put("id", true);
			firstSortPropertyColumn = "id";
		}

		idCrit = applyOrder(idCrit, sortProperties);
		idCrit = applyPaging(idCrit, request.getPageLimit(),
				request.getPageNumber());
		idCrit = applyProjectionForIds(idCrit, firstSortPropertyColumn);

		final List results = (List) idCrit.list();
		final List<Long> idlist = new ArrayList<Long>();
		
		if(results == null || results.size() == 0){
			return null;
		}

		for (Iterator iditer = results.iterator(); iditer.hasNext();) {
			Object[] record = (Object[]) iditer.next();
			idlist.add((Long) record[0]);
		}

		Criteria crit = buildGenericCriteria(session, queriedClass,
				request.getConditions(), request.getFetchedAssociation(),
				request.getAlias());

		crit = applyOrder(crit, sortProperties);
		crit = applyIdRestriction(crit, idlist);

		return crit;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Integer buildCriteriaForCount(Session session,
			Class queriedClass, Set<PerzistentCondition> pConditions,
			Map<String, String> aliases, Set<String> fetchedAssociations) {

		Criteria idCrit = buildGenericCriteriaForIds(session, queriedClass,
				pConditions, aliases);

		idCrit = applyFetch(idCrit, fetchedAssociations, FetchMode.JOIN);
		idCrit = applyProjectionForIdsCount(idCrit);

		final Collection<Long> ids = idCrit.list();

		return ids.size();

	}

	@SuppressWarnings("rawtypes")
	public static Criteria buildGenericCriteria(Session session,
			Class queriedClass, Set<PerzistentCondition> pConditions,
			Set<String> fetchedAssociations, Map<String, String> aliases) {

		Criteria crit = session.createCriteria(queriedClass);

		crit = applyFetch(crit, fetchedAssociations, FetchMode.JOIN);
		crit = applyAliases(crit, aliases);
		crit = applyFilters(pConditions, crit);

		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

		return crit;

	}

	@SuppressWarnings("rawtypes")
	public static Criteria buildGenericCriteriaForIds(Session session,
			Class queriedClass, Set<PerzistentCondition> pConditions,
			Map<String, String> aliases) {

		Criteria crit = session.createCriteria(queriedClass);

		crit = applyAliases(crit, aliases);
		crit = applyFilters(pConditions, crit);

		return crit;

	}

	private static Criteria applyIdRestriction(Criteria crit, List<Long> ids) {

		if (ids != null) {
			crit.add(Restrictions.in("id", ids));
		}

		return crit;

	}

	private static Criteria applyProjectionForIds(Criteria crit,
			String sortProperty) {

		if (sortProperty == null) {
			sortProperty = "id";
		}

		crit.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.id()).add(Projections.property(sortProperty))));

		return crit;

	}

	private static Criteria applyProjectionForIdsCount(Criteria crit) {

		crit.setProjection(Projections.distinct(Projections.projectionList()
				.add(Projections.id())));

		return crit;

	}

	private static Criteria applyPaging(Criteria crit, Integer pageSize,
			Integer pageNumber) {

		if (pageNumber != null && pageSize != null) {
			crit.setFirstResult(pageSize * (pageNumber - 1)).setMaxResults(
					pageSize);
		}

		return crit;

	}

	@SuppressWarnings("rawtypes")
	private static Criteria applyFilters(Set<PerzistentCondition> pConditions,
			Criteria crit) {

		if (pConditions != null && pConditions.size() > 0) {

			final List<Criterion> orConditionList = new ArrayList<Criterion>();
			final List<Criterion> andConditionList = new ArrayList<Criterion>();

			for (PerzistentCondition perzistentCondition : pConditions) {

				final FilterOperator operator = perzistentCondition
						.getFieldOperator();
				final FieldType fieldType = perzistentCondition.getFieldType();
				final Object value = convertValue(
						perzistentCondition.getValue(), fieldType);
				final boolean isOr = perzistentCondition.isOr();
				final String fieldName = perzistentCondition.getFieldName();
				final int spaceIndex = fieldName.indexOf("#");

				String ourKey = null;
				Criterion criterion = null;

				if (spaceIndex != -1) {
					ourKey = fieldName.substring(0, spaceIndex);
				} else {
					ourKey = fieldName;
				}

				if (operator == FilterOperator.LIKE) {
					criterion = Restrictions.ilike(ourKey, "%" + value + "%");
				}
				if (operator == FilterOperator.RLIKE) {
					criterion = Restrictions.ilike(ourKey, value + "%");
				}
				if (operator == FilterOperator.EQ) {
					criterion = Restrictions.eq(ourKey, value);
				}
				if (operator == FilterOperator.ISNULL) {
					criterion = Restrictions.isNull(ourKey);
				}
				if (operator == FilterOperator.GE) {
					criterion = Restrictions.ge(ourKey, value);
				}
				if (operator == FilterOperator.GT) {
					criterion = Restrictions.gt(ourKey, value);
				}
				if (operator == FilterOperator.LE) {
					criterion = Restrictions.le(ourKey, value);
				}
				if (operator == FilterOperator.LT) {
					criterion = Restrictions.lt(ourKey, value);
				}
				if (operator == FilterOperator.IN) {
					criterion = Restrictions.in(ourKey, (Collection) value);
				}

				if (isOr) {
					orConditionList.add(criterion);
				} else {
					andConditionList.add(criterion);
				}

			}

			if (orConditionList.size() > 0) {
				crit.add(Restrictions.or(orConditionList
						.toArray(new Criterion[orConditionList.size()])));
			}
			if (andConditionList.size() > 0) {
				crit.add(Restrictions.and(andConditionList
						.toArray(new Criterion[andConditionList.size()])));
			}

		}

		return crit;

	}

	private static Criteria applyFetch(Criteria crit,
			Set<String> fetchedAssociations, FetchMode mode) {

		if (fetchedAssociations != null) {
			for (String assoc : fetchedAssociations) {
				crit.setFetchMode(assoc, mode);
			}
		}

		return crit;

	}

	private static Criteria applyAliases(Criteria crit,
			Map<String, String> aliases) {

		if (aliases != null) {

			for (Map.Entry<String, String> entry : aliases.entrySet()) {

				final String key = entry.getKey();
				final String value = entry.getValue();

				if (key != null && value != null) {
					crit = crit.createAlias(key, value,
							JoinType.LEFT_OUTER_JOIN);
				}

			}

		}

		return crit;

	}

	private static Criteria applyOrder(Criteria crit,
			Map<String, Boolean> sortProperties) {

		for (Entry<String, Boolean> sortPropertyMap : sortProperties.entrySet()) {

			final String sortProperty = sortPropertyMap.getKey();

			Boolean asc = sortPropertyMap.getValue();

			if (asc == null) {
				asc = true;
			}

			if (sortProperty != null) {
				if (asc) {
					crit.addOrder(Order.asc(sortProperty));
				} else {
					crit.addOrder(Order.desc(sortProperty));
				}
			} else {
				if (asc) {
					crit.addOrder(Order.asc("id"));
				} else {
					crit.addOrder(Order.desc("id"));
				}
			}

		}

		return crit;

	}

	private static Object convertValue(Object value, FieldType fieldType) {

		if (fieldType == FieldType.STRING) {
			return String.valueOf(value);
		}
		if (fieldType == FieldType.INTEGER) {
			return Integer.valueOf(String.valueOf(value));
		}
		if (fieldType == FieldType.FLOAT) {
			return Float.valueOf(String.valueOf(value));
		}
		if (fieldType == FieldType.LONG) {
			return Long.valueOf(String.valueOf(value));
		}
		if (fieldType == FieldType.DATE) {
			return (Date) value;
		}
		if (fieldType == FieldType.BIG_DECIMAL) {
			return new BigDecimal(String.valueOf(value));
		}

		return value;

	}

}
