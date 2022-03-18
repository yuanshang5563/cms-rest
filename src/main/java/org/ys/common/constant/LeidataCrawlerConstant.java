package org.ys.common.constant;

public class LeidataCrawlerConstant {
    private LeidataCrawlerConstant() {}

    /**
     * 雷达体育资料库url
     */
    public static final String LEIDATA_CRAWLER_STATS_URL = "http://web.leidata.com/stats/competition/";

    /**
     * 雷达体育api的url
     */
    public static final String LEIDATA_CRAWLER_API_URL = "http://web.leidata.com/api";

    /**
     * 雷达体育赛季url的中间词
     */
    public static final String LEIDATA_CRAWLER_SEASON_MIDDLE_WORD = "GetSeasonTreeByCompetitionId";

    /**
     * 雷达体育联赛url的中间词
     */
    public static final String LEIDATA_CRAWLER_LEAGUE_MATCH_MIDDLE_WORD = "competition";

    /**
     * 用于传递联赛id的key
     */
    public static final String LEIDATA_CRAWLER_FOOTBALL_LEAGUE_MATCH_ID = "footballLeagueMatchId";

    /**
     * 用于传递赛季id的key
     */
    public static final String LEIDATA_CRAWLER_FOOTBALL_SEASON = "footballSeason";

    /**
     * 雷达体育赛季轮数url的中间词
     */
    public static final String LEIDATA_CRAWLER_SEASON_ROUND_MIDDLE_WORD = "GetRoundList";

    /**
     * 雷达体育比分url的中间词
     */
    public static final String LEIDATA_CRAWLER_SEASON_SCORE_MIDDLE_WORD = "GetMatchesByCompetitionId";

    /**
     * 用于传递赛季类别id的key
     */
    public static final String LEIDATA_CRAWLER_FOOTBALL_SEASON_CATEGORY = "footballSeasonCategory";

    /**
     * 用于传递赛季比分的key
     */
    public static final String LEIDATA_CRAWLER_FOOTBALL_SCORE = "footballScore";

    /**
     * 雷达体育赛季比赛详情url的中间词
     */
    public static final String LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_MIDDLE_WORD = "GetMatchById";

    /**
     * 雷达体育赛季比赛详情url的中间词
     */
    public static final String LEIDATA_CRAWLER_SEASON_SCORE_DETAIL_STATS_MIDDLE_WORD = "stats";
}
