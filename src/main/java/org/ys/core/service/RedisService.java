package org.ys.core.service;

/**
 * redis缓存操作
 */
public interface RedisService {
    /**
     * 系统初始化
     */
    public void initSystemCach() throws Exception;

    /**
     * 刷新缓存
     * @throws Exception
     */
    public void refreshSystemCach() throws Exception;
}
