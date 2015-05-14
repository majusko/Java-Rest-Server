package sk.rxjrest.dao.transfer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class PerzistentRequest implements Serializable {

	private static final long serialVersionUID = 1L;
	private Map<String, Boolean> sortColumns;
	private int pageNumber;
	private int pageLimit;
	private Set<PerzistentCondition> conditions;
	private Set<String> fetchedAssociation = new HashSet<String>();
	private Map<String, String> alias = new HashMap<String, String>();
	
	protected PerzistentRequest() {
		super();
	}
	public PerzistentRequest(Set<PerzistentCondition> conditions) {
		this(new LinkedHashMap<String, Boolean>(){
			private static final long serialVersionUID = 1L;
		{
		     put("id", true);
		}}, conditions);
	}

	public PerzistentRequest(Map<String, Boolean> sortColumns, Set<PerzistentCondition> conditions) {
		this(sortColumns, 1, null, conditions);
	}
	
	public PerzistentRequest(Map<String, Boolean> sortColumns,
			Integer pageNumber, Integer pageLimit, Set<PerzistentCondition> conditions) {
		super();
		
		this.pageNumber = (pageNumber == null)? 0 : pageNumber ;
		this.pageLimit = (pageLimit == null)? 2147483647 : pageLimit ;
		this.conditions = conditions;
		
		for (final PerzistentCondition perzistentCondition : conditions) {
			setAssocAndAlias(perzistentCondition.getFieldName());
		}
		
		if(sortColumns != null){
			final Map<String, Boolean> sortColmnsRef = new LinkedHashMap<String, Boolean>();
			for (final Map.Entry<String, Boolean> sortColumn : sortColumns.entrySet()) {
				final String newSortColumn = setAssocAndAlias(sortColumn.getKey());	
				if(newSortColumn != null){
					sortColmnsRef.put(newSortColumn, sortColumn.getValue());
				}
			}
			this.sortColumns = sortColmnsRef;
		}
		
	}

	private String setAssocAndAlias(String fieldName){
		
		String result = null;
		
		if(fieldName != null){
		
			final Map<String,String> aliases = new HashMap<String, String>();
			final Set<String> assoc = new HashSet<String>();
			
			result = fieldName.replaceAll("\\.", "_");
			
			if (!StringUtils.isBlank(result)) {
	
				final String[] params = result.split("\\_");
				if (params != null && params.length > 0) {
	
					final List<String> paramsList = Lists.newArrayList(params);
					final int lastIndex = paramsList.size() - 1;
					final String tempSort = paramsList.get(lastIndex);
	
					paramsList.remove(lastIndex);
	
					String wholeBase = "";
					String wholeAlias = "";
	
					for (int i = 0; i < paramsList.size(); i++) {
	
						final String string = paramsList.get(i);
	
						if (i == 0) {
							wholeBase = string;
							wholeAlias = string;
						} else {
							wholeBase = wholeAlias + "." + string;
							wholeAlias = wholeAlias + "_" + string;
						}
						
						aliases.put(wholeBase, wholeAlias);
						assoc.add(wholeAlias);
						
					}
	
					if (StringUtils.isBlank(wholeAlias)) {
						result = tempSort;
					} else {
						result = wholeAlias + "." + tempSort;
					}
					
				}
			}
	
			final Map<String, String> tempAliases = this.getAlias();
			final Set<String> tempAssoc = this.getFetchedAssociation();
			
			tempAliases.putAll(aliases);
			tempAssoc.addAll(assoc);
			
			this.setAlias(tempAliases);
			this.setFetchedAssociation(tempAssoc);

		}
		
		return result;
		
	}
	
	public Map<String, Boolean> getSortColumns() {
		return sortColumns;
	}
	public void setSortColumns(Map<String, Boolean> sortColumns) {
		this.sortColumns = sortColumns;
	}
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageLimit() {
		return pageLimit;
	}
	public void setPageLimit(int pageLimit) {
		this.pageLimit = pageLimit;
	}
	public Set<PerzistentCondition> getConditions() {
		return conditions;
	}
	public void setConditions(Set<PerzistentCondition> conditions) {
		this.conditions = conditions;
	}

	public Set<String> getFetchedAssociation() {
		return fetchedAssociation;
	}

	public void setFetchedAssociation(Set<String> fetchedAssociation) {
		this.fetchedAssociation = fetchedAssociation;
	}

	public Map<String, String> getAlias() {
		return alias;
	}

	public void setAlias(Map<String, String> alias) {
		this.alias = alias;
	}
	
}
