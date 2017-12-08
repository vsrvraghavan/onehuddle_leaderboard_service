package com.onehuddle.leaderboard.redis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.onehuddle.leaderboard.pojo.LeaderData;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;

public class CompanyLeaderboard extends Leaderboard {

	
	public CompanyLeaderboard(String leaderboardName) {
		super(leaderboardName);
		// TODO Auto-generated constructor stub
	}

	public CompanyLeaderboard(String leaderboardName, String host, int port, int pageSize) {
		super(leaderboardName, host, port, pageSize);
		// TODO Auto-generated constructor stub
	}

	public CompanyLeaderboard(String leaderboardName, int pageSize, Jedis redisConnection) {
		super(leaderboardName, pageSize, redisConnection);
		// TODO Auto-generated constructor stub
		
	}
	
	
	
	/**
	 * Retrieve a page of leaders as a list of LeaderData in the named company
	 *
	 * @param companyName CompanyName
	 * @param currentPage Page
	 * @param useZeroIndexForRank Use zero-based index for rank
	 * @param pageSize Page size
	 * @return Page of leaders as a list of LeaderData in the named leaderboard
	 */
	public List<LeaderData> mergeScoresIn(String companyId, int currentPage, boolean useZeroIndexForRank, int pageSize){
				
		List<LeaderData> leaderList = new ArrayList<LeaderData>();		
		currentPage = (currentPage < 1) ? 1 : currentPage;		
		pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
		String[] company_games_leaderboards = getCompanyGamesLeaderBoardIn(companyId);			
		if(company_games_leaderboards.length == 0) {
			return leaderList;
		}else {
			_jedis.zunionstore(this._leaderboardName, company_games_leaderboards);			
			currentPage = (currentPage > totalPagesIn(this._leaderboardName, pageSize)) ? totalPagesIn(this._leaderboardName, pageSize) : currentPage;			
			int indexForRedis = currentPage - 1;
			int startingOffset = indexForRedis * pageSize;			
			startingOffset = (startingOffset < 0) ? 0 : startingOffset;			
			int endingOffset = (startingOffset + pageSize) - 1;
			Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores(this._leaderboardName, startingOffset, endingOffset);
			leaderList = massageLeaderData(this._leaderboardName, rawLeaderData, useZeroIndexForRank);
		}		
		return leaderList;		
	}
	
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public Long putCompanyGamesIn(String companyId, String gameId) {
		
		Long memberid = _jedis.sadd("company_"+companyId+"_games", gameId);		
		return memberid;
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public String[] getCompanyGamesIn(String companyId) {
		
		Set<String> games = _jedis.smembers("company_"+companyId+"_games");
		String[] company_games =  games.stream().toArray(String[]::new);
		return company_games;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public String[] getCompanyGamesLeaderBoardIn(String companyId) {
		
		List<String> games = _jedis.smembers("company_"+companyId+"_games").stream()
	    .map(s -> "company_"+companyId+"_game_"+ s + "_leaderboard")
	    .collect(Collectors.toList());		
		String[] company_games_leaderboard =  games.stream().toArray(String[]::new); //games.toArray(new String[0]);//games.stream().toArray(String[]::new);
		System.out.println(Arrays.toString(company_games_leaderboard));		
		return company_games_leaderboard;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public Long putCompanyDepartmentsIn(String companyId, String departmentId) {
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		if(companyId != null &&  departmentId != null) {			
			inserted_quantity = _jedis.sadd("company_"+companyId+"_departments", departmentId);
		}		
		return inserted_quantity;
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public String[] getCompanyDepartmentsIn(String companyId) {
		
		Set<String> departments = _jedis.smembers("company_"+companyId+"_departments");		
		String[] company_departments = departments.stream().toArray(String[]::new);		
		return company_departments;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @param memberId Player ID
	 * @return inserted members quantity
	 */
	
	public Long putCompanyDepartmentMembersIn(String companyId, String departmentId, String memberId) {
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		putCompanyDepartmentsIn(companyId, departmentId);
		inserted_quantity = _jedis.sadd("company_"+companyId+"_department_"+departmentId+"_members", memberId);				
		return inserted_quantity;
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @return list of members in the named company and department
	 */
	
	public String[] getCompanyDepartmentMembersIn(String companyId, String departmentId) {
		
		Set<String> department_members = _jedis.smembers("company_"+companyId+"_department_"+departmentId+"_members");		
		String[] company_department_members = department_members.stream().toArray(String[]::new);		
		return company_department_members;
	}
	
	
	
	
	
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public Long putCompanyGroupsIn(String companyId, String groupId) {
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		if(companyId != null &&  groupId != null) {
			inserted_quantity = _jedis.sadd("company_"+companyId+"_groups", groupId);
		}		
		return inserted_quantity;
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyName CompanyName
	 * @return list of games in the named company
	 */
	
	public String[] getCompanyGroupsIn(String companyId) {
		
		Set<String> groups = _jedis.smembers("company_"+companyId+"_groups");		
		String[] company_groups = groups.stream().toArray(String[]::new);		
		return company_groups;
	}
	
	
	
	/**
	 * Retrieve list of games in the named company
	 *
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @param memberId Player ID
	 * @return inserted members quantity
	 */
	
	public Long putCompanyGroupMembersIn(String companyId, String groupId, String memberId) {	
		
		Long inserted_quantity = Integer.valueOf(0).longValue();
		putCompanyGroupsIn(companyId, groupId);
		inserted_quantity = _jedis.sadd("company_"+companyId+"_group_"+groupId+"_members", memberId);		
		return inserted_quantity;		
	}
	
	
	/**
	 * Retrieve list of games in the named company
	 * @param companyId Company ID
	 * @param departmentId Department ID  
	 * @return list of members in the named company and department
	 */
	
	public String[] getCompanyGroupMembersIn(String companyId, String groupId) {
		
		Set<String> group_members = _jedis.smembers("company_"+companyId+"_group_"+groupId+"_members");		
		String[] company_group_members = group_members.stream().toArray(String[]::new);		
		return company_group_members;
	}
	
	
	
	
	
	/**
	 * Retrieve a page of leaders as a list of LeaderData in the named company
	 *
	 * @param companyName CompanyName
	 * @param currentPage Page
	 * @param useZeroIndexForRank Use zero-based index for rank
	 * @param pageSize Page size
	 * @return Page of leaders as a list of LeaderData in the named leaderboard
	 */
	public List<LeaderData> departmentScoresIn(String companyId, String departmentId, int currentPage, boolean useZeroIndexForRank, int pageSize){
				
		List<LeaderData> leaderList = new ArrayList<LeaderData>();	
		String[] company_games_leaderboards = getCompanyGamesLeaderBoardIn(companyId);			
		if(company_games_leaderboards.length != 0) {
			_jedis.zunionstore("company_"+companyId+"_leaderboard", company_games_leaderboards);
		}
		ZParams params = new ZParams();
		params.weightsByDouble(0,1);
		_jedis.zinterstore("company_"+companyId+"_department_"+departmentId+"_leaderboard", params, "company_"+companyId+"_department_"+departmentId+"_members", "company_"+companyId+"_leaderboard");
		currentPage = (currentPage < 1) ? 1 : currentPage;		
		pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;
		currentPage = (currentPage > totalPagesIn(this._leaderboardName, pageSize)) ? totalPagesIn(this._leaderboardName, pageSize) : currentPage;			
		int indexForRedis = currentPage - 1;
		int startingOffset = indexForRedis * pageSize;			
		startingOffset = (startingOffset < 0) ? 0 : startingOffset;			
		int endingOffset = (startingOffset + pageSize) - 1;					
		Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores("company_"+companyId+"_department_"+departmentId+"_leaderboard", startingOffset, endingOffset);			
		leaderList = massageLeaderData(this._leaderboardName, rawLeaderData, useZeroIndexForRank);		
		return leaderList;		
	}
	
	
	
	/**
	 * Retrieve a page of leaders as a list of LeaderData in the named company
	 *
	 * @param companyName CompanyName
	 * @param currentPage Page
	 * @param useZeroIndexForRank Use zero-based index for rank
	 * @param pageSize Page size
	 * @return Page of leaders as a list of LeaderData in the named leaderboard
	 */
	public List<LeaderData> groupScoresIn(String companyId, String groupId, int currentPage, boolean useZeroIndexForRank, int pageSize){
				
		List<LeaderData> leaderList = new ArrayList<LeaderData>();		
		String[] company_games_leaderboards = getCompanyGamesLeaderBoardIn(companyId);			
		if(company_games_leaderboards.length != 0) {
			_jedis.zunionstore("company_"+companyId+"_leaderboard", company_games_leaderboards);
		}
		ZParams params = new ZParams();
		params.weightsByDouble(0,1);
		_jedis.zinterstore("company_"+companyId+"_group_"+groupId+"_leaderboard", params, "company_"+companyId+"_group_"+groupId+"_members", "company_"+companyId+"_leaderboard");			
		currentPage = (currentPage < 1) ? 1 : currentPage;		
		pageSize = (pageSize < 1) ? DEFAULT_PAGE_SIZE : pageSize;					
		currentPage = (currentPage > totalPagesIn("company_"+companyId+"_group_"+groupId+"_leaderboard", pageSize)) ? totalPagesIn(this._leaderboardName, pageSize) : currentPage;			
		int indexForRedis = currentPage - 1;
		int startingOffset = indexForRedis * pageSize;			
		startingOffset = (startingOffset < 0) ? 0 : startingOffset;			
		int endingOffset = (startingOffset + pageSize) - 1;								
		Set<Tuple> rawLeaderData = _jedis.zrevrangeWithScores("company_"+companyId+"_group_"+groupId+"_leaderboard", startingOffset, endingOffset);			
		leaderList = massageLeaderData(this._leaderboardName, rawLeaderData, useZeroIndexForRank);	
		return leaderList;		
	}
	
	
	
	

}
