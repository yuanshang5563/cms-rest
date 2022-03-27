package org.ys.common.constant;

/**
 * 爬虫配置有关常量类
 */
public class CrawlerConstant {
	private CrawlerConstant() {}

	/**
	 * 忽略爬取的联赛名字
	 */
	public static final String CRAW_LEAGUE_MATCH_IGNORE_WORDS = "craw_league_match_ignore_words";

	/**
	 * 爬虫线程数
	 */
	public static final String CRAW_THREAD_COUNT = "crawler_thread_count";

	/**
	 *要获取多少个最近赛季的详情数据参数
	 */
	public static final String CRAW_DETAIL_SEASON_COUNT = "crawler_detail_season_count";
}
