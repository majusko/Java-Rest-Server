package sk.rxjrest.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.rxjrest.common.BigDecimalUtils;
import sk.rxjrest.dao.UserDao;
import sk.rxjrest.dao.VisitDao;
import sk.rxjrest.dao.generic.common.FieldType;
import sk.rxjrest.dao.generic.common.FilterOperator;
import sk.rxjrest.dao.transfer.PerzistentCondition;
import sk.rxjrest.dao.transfer.PerzistentCount;
import sk.rxjrest.dao.transfer.PerzistentRequest;
import sk.rxjrest.domain.visit.Visit;
import sk.rxjrest.dto.PagingListDto;
import sk.rxjrest.dto.visit.VisitDto;
import sk.rxjrest.dto.visit.VisitFormDto;
import sk.rxjrest.exception.RestException;

@Service
public class VisitService {

	@Autowired
	private VisitDao visitDao;

	@Autowired
	private UserDao userDao;

	public Long addVisit(VisitFormDto visitDto, Long userId) {
		return visitDao.create(new Visit(visitDto, userDao.findOne(userId)));
	}

	public void deleteVisit(Long id) {
		visitDao.deleteById(id);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public PagingListDto searchVisitsForUser(String searchString, Long userId, 
			boolean plain, int page, int resultsPerPage, String sort, Boolean ascSort) throws RestException {

		final Set<PerzistentCondition> conditions = new HashSet<>();
		final Map<String, Boolean> sortColumns = new LinkedHashMap<String, Boolean>();
	    final BigDecimal bigDecimalCondition = BigDecimalUtils.saveConvertStringToBigDecimal(searchString);

		sortColumns.put(sort, ascSort);
		
		if(bigDecimalCondition != null){
			conditions.add(new PerzistentCondition("temp", bigDecimalCondition, FieldType.BIG_DECIMAL, FilterOperator.EQ, true));
			conditions.add(new PerzistentCondition("humidity", bigDecimalCondition, FieldType.BIG_DECIMAL, FilterOperator.EQ, true));
			conditions.add(new PerzistentCondition("pressure", bigDecimalCondition, FieldType.BIG_DECIMAL, FilterOperator.EQ, true));
			conditions.add(new PerzistentCondition("windSpeed", bigDecimalCondition, FieldType.BIG_DECIMAL, FilterOperator.EQ, true));
			conditions.add(new PerzistentCondition("windDeg", bigDecimalCondition, FieldType.BIG_DECIMAL, FilterOperator.EQ, true));
		}
		
		conditions.add(new PerzistentCondition("city", searchString, FieldType.STRING, FilterOperator.RLIKE, true));
		conditions.add(new PerzistentCondition("country", searchString, FieldType.STRING, FilterOperator.RLIKE, true));
		conditions.add(new PerzistentCondition("user.id", userId, FieldType.LONG, FilterOperator.EQ, false));
		
		final PerzistentRequest request = new PerzistentRequest(sortColumns, page, resultsPerPage, conditions);
		final List<Visit> failures = visitDao.filterResults(request);
		final PerzistentCount failuresCount = visitDao.filterCount(request);
		final List<VisitDto> failuresDto = new ArrayList<VisitDto>();
		final PagingListDto failureListDto = new PagingListDto();

		for (Visit failure : failures) {
			failuresDto.add(new VisitDto(failure, plain));
		}

		failureListDto.setItems(failuresDto);
		failureListDto.setTotalPages(failuresCount.getAllPages());
		failureListDto.setTotalCount(failuresCount.getAllCount());

		return failureListDto;

	}

}
